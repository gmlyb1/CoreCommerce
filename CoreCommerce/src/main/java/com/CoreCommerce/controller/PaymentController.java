package com.CoreCommerce.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.MemberCoupon;
import com.CoreCommerce.domain.Notification;
import com.CoreCommerce.domain.Order;
import com.CoreCommerce.repository.NotificationRepository;
import com.CoreCommerce.repository.OrderRepository;
import com.CoreCommerce.repository.PaymentRepository;
import com.CoreCommerce.service.CouponService;
import com.CoreCommerce.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

	private final PaymentRepository paymentRepository;
	private final NotificationRepository notificationRepository;
	private final OrderService orderService;
	private final CouponService couponService;
	
	
	@GetMapping
    public String paymentPage(HttpSession session,@RequestParam Long orderId, Model model) {
        Member loginUser = (Member) session.getAttribute("loginUser");
		Order order = orderService.getOrder(orderId);
		
		List<MemberCoupon> myCoupons = couponService.findMemberCoupons(loginUser.getId())
		        .stream()
		        .filter(mc -> !mc.isUsed() && mc.getCoupon().isActive()) // used=false AND coupon.active=true
		        .collect(Collectors.toList());
		
		model.addAttribute("order", order);
		model.addAttribute("myCoupons", myCoupons);
        return "payment/payment";
    }

	
	@GetMapping("/success")
	public String success(@RequestParam String paymentKey,
	                      @RequestParam String orderId,
	                      @RequestParam int amount,
	                      @RequestParam(required = false) String memberCouponId, // String으로 받음
	                      Model model,
	                      HttpSession session) {

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    // 1️⃣ memberCouponId를 Long으로 안전하게 변환
	    Long couponId = null;
	    if(memberCouponId != null && !memberCouponId.equals("null") && !memberCouponId.isEmpty()) {
	        try {
	            couponId = Long.valueOf(memberCouponId);
	        } catch(NumberFormatException e) {
	            couponId = null; // 안전하게 null 처리
	        }
	    }

	    // 2️⃣ 결제 확인
	    String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
	    String encodedKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Basic " + encodedKey);
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    Map<String, Object> body = new HashMap<>();
	    body.put("paymentKey", paymentKey);
	    body.put("orderId", orderId);
	    body.put("amount", amount);

	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.postForEntity("https://api.tosspayments.com/v1/payments/confirm", request, String.class);

	    // 3️⃣ 주문 ID 처리
	    Long realOrderId = Long.parseLong(orderId.replace("ORD_", ""));
	    orderService.completeOrder(realOrderId);
	    Order order = orderService.getOrder(realOrderId);
	    
	    // 4️⃣ 알림
	    Notification note = new Notification();
	    note.setUserId(loginUser.getEmail());
	    note.setType("ORDER");
	    note.setContent("주문 #" + order.getId() + " 결제가 완료되었습니다.");
	    note.setLink("/order/mypage/" + order.getId());
	    note.setCreatedAt(LocalDateTime.now());
	    note.setRead(false);
	    notificationRepository.insert(note);
	    
	    int finalPrice = order.getTotalPrice(); // 기본값
	    if(couponId != null) {
	        // DB에서 바로 쿠폰 적용 후 계산
	        int discount = couponService.calculateDiscount(couponId, order.getTotalPrice());
	        if(discount < 0) discount = 0;

	        finalPrice = order.getTotalPrice() - discount;
	        if(finalPrice < 0) finalPrice = 0;

	        couponService.useCoupon(couponId, realOrderId);

	        // DB 업데이트
	        orderService.updateDiscount(realOrderId, couponId, discount, finalPrice);
	    }

	    orderService.updateFinalPrice(realOrderId, finalPrice);
	    
	    order = orderService.getOrder(realOrderId);
	    
	    // 6️⃣ 모델 추가
	    model.addAttribute("order", order);

	    return "payment/success";
	}
	
	@PostMapping("/apply-coupon")
	@ResponseBody
	public Map<String, Object> applyCoupon(
	        @RequestParam Long orderId,
	        @RequestParam(required = false) Long memberCouponId
	){

	    Order order = orderService.getOrder(orderId);
	    
	    int discount = 0;
	    int finalPrice = order.getTotalPrice();

	    if(memberCouponId != null){

	        discount = couponService.calculateDiscount(
	        		memberCouponId,
	                order.getTotalPrice()
	        );
	        
	        if(discount < 0) {
	        	discount = 0;
	        }

	        finalPrice = order.getTotalPrice() - discount;

	        if(finalPrice < 0) {
	        	finalPrice = 0;
	        }
	        
	        // 🔥 쿠폰 사용 처리
//	        couponService.useCoupon(memberCouponId, orderId);
	    }

	    Map<String, Object> result = new HashMap<>();
	    result.put("discount", discount);
	    result.put("finalPrice", finalPrice);
	    
	    return result;
	}
	

    // 결제 실패
    @GetMapping("/fail")
    public String fail() {
        return "payment/fail";
    }
}
