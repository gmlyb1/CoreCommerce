package com.CoreCommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.repository.OrderRepository;
import com.CoreCommerce.repository.PaymentRepository;
import com.CoreCommerce.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentRepository paymentRepository;
	private final OrderService orderService;
	
	@GetMapping("/payment")
	public String paymentPage(@RequestParam Long orderId, Model model) {

	    Order order = orderService.getOrder(orderId);
	    model.addAttribute("order", order);

	    return "payment/payment";   // templates/payment/payment.html
	}
	
    @GetMapping("/payment/success")
    public String success(@RequestParam String paymentKey,
                          @RequestParam Long orderId,
                          @RequestParam int amount) {

    	paymentRepository.confirmPayment(paymentKey, orderId, amount);
        return "payment/success";
    }

    @GetMapping("/payment/fail")
    public String fail() {
        return "payment/fail";
    }
}
