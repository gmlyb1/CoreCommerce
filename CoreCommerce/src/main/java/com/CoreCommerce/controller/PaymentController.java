package com.CoreCommerce.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
import com.CoreCommerce.domain.Order;
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
	private final OrderService orderService;
	private final CouponService couponService;
	
	@GetMapping
    public String paymentPage(HttpSession session,@RequestParam Long orderId, Model model) {
        Member loginUser = (Member) session.getAttribute("loginUser");
		Order order = orderService.getOrder(orderId);

		List<MemberCoupon> myCoupons = couponService.findMemberCoupons(loginUser.getId())
				.stream()
				.filter(mc -> mc.isUsed() == false)
				.collect(Collectors.toList());
		
		model.addAttribute("order", order);
		model.addAttribute("myCoupons", myCoupons);
        return "payment/payment";
    }

	@GetMapping("/success")
	public String success(@RequestParam String paymentKey,
	                      @RequestParam String orderId,
	                      @RequestParam int amount,
	                      @RequestParam(required = false) Long memberCouponId,
	                      Model model) {

	    String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

	    String encodedKey = Base64.getEncoder()
	            .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "Basic " + encodedKey);
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    Map<String, Object> body = new HashMap<>();
	    body.put("paymentKey", paymentKey);
	    body.put("orderId", orderId);
	    body.put("amount", amount);

	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.postForEntity(
	            "https://api.tosspayments.com/v1/payments/confirm",
	            request,
	            String.class
	    );
	    
//	    Long realOrderId = Long.parseLong(orderId.replace("ORDER_", ""));
	    Long realOrderId = Long.parseLong(orderId.replace("ORD_", ""));
	    
	    orderService.completeOrder(realOrderId);

	    if(memberCouponId != null) {
	    	couponService.useCoupon(memberCouponId, realOrderId);
	    }
	    
	    // ✅ 주문 조회 후 모델에 추가
	    Order order = orderService.getOrder(realOrderId);
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

	        finalPrice = order.getTotalPrice() - discount;

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
