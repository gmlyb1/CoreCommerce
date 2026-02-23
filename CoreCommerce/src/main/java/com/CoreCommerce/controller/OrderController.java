package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Order;
import com.CoreCommerce.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/orders")
	public String orderList(Model model,HttpSession session) {

	    Member loginUser = (Member) session.getAttribute("loginUser");
	    
	    if(loginUser == null) {
	    	return "redirect:/login";
	    }
	    
	    List<Order> orders = orderService.findByMemberId(loginUser.getId());
	    model.addAttribute("orders", orders);

	    return "order/list";
	}
	
	@GetMapping("/orders/{id}")
	public String orderDetail(@PathVariable Long id,
	                          HttpSession session,
	                          Model model) {

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return "redirect:/login";
	    }

	    Order order = orderService.getOrder(id);

	    // 🔥 본인 주문만 조회 가능하게 보안 처리
	    if (order == null || !order.getMemberId().equals(loginUser.getId())) {
	        return "redirect:/orders";
	    }

	    model.addAttribute("order", order);

	    return "order/detail";
	}
	
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
