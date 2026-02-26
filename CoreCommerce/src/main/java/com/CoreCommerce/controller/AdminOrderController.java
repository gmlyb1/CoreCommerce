package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.repository.OrderRepository;
import com.CoreCommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;


    // 주문 목록
    @GetMapping
    public String orderList(
            @RequestParam(defaultValue = "1") int page,
            Model model, HttpSession session){

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	
    	if(!loginUser.getRole().equals("MANAGER") && !loginUser.getRole().equals("PRODUCTER")) {
    		return "redirect:/";
    	}
    	
        int size = 10;

        int totalCount = orderRepository.countAll(); // ✅ 전체 주문 개수

        Pagination pagination = new Pagination(page, size, totalCount);

        List<Order> orders =
                orderRepository.findPaging(
                        pagination.getOffset(),
                        pagination.getSize()
                );

        model.addAttribute("orders", orders);
        model.addAttribute("pagination", pagination);

        return "admin/order-list";
    }

    // 상태 변경
    @PostMapping("/status")
    @ResponseBody
    public String updateStatus(@RequestParam Long orderId,
                               @RequestParam String status,
                               @RequestParam(required = false) String courier,
                               @RequestParam(required = false) String trackingNumber){

        orderRepository.updateStatus(orderId, status, courier, trackingNumber);

        return "OK";
    }
}