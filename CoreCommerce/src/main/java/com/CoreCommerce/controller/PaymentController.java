package com.CoreCommerce.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.repository.OrderRepository;
import com.CoreCommerce.repository.PaymentRepository;
import com.CoreCommerce.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

	private final PaymentRepository paymentRepository;
	private final OrderService orderService;
	
	@GetMapping
    public String paymentPage(@RequestParam Long orderId, Model model) {
        Order order = orderService.getOrder(orderId);
        model.addAttribute("order", order);
        return "payment/payment";
    }

	@GetMapping("/success")
	public String success(@RequestParam String paymentKey,
	                      @RequestParam String orderId,
	                      @RequestParam int amount,
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

	    Long realOrderId = Long.parseLong(orderId.replace("ORDER_", ""));

	    orderService.completeOrder(realOrderId);

	    // ✅ 주문 조회 후 모델에 추가
	    Order order = orderService.getOrder(realOrderId);
	    model.addAttribute("order", order);

	    return "payment/success";
	}

    // 결제 실패
    @GetMapping("/fail")
    public String fail() {
        return "payment/fail";
    }
}
