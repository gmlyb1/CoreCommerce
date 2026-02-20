package com.CoreCommerce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/create")
	@ResponseBody
	public Long createOrder(HttpSession session) {
		
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			throw new RuntimeException("로그인 필요");
		}
		
		
		return orderService.createOrder(loginUser.getId());
	}
}
