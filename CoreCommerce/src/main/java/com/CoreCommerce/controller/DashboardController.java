package com.CoreCommerce.controller;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardController {

	 private final DashboardService dashboardService;
	 
	 @GetMapping("/dashboard")
	 public String dashboard(Model model,HttpSession session){
		 Member loginUser = (Member) session.getAttribute("loginUser");
		 String role = loginUser.getRole();
		if (!role.equals("MANAGER") && !role.equals("PRODUCTER")) {
		    return "redirect:/login";
		}
		 
        model.addAttribute("totalMember", dashboardService.countMember());
        model.addAttribute("totalOrder", dashboardService.countOrder());
        model.addAttribute("todaySales", dashboardService.todaySales());
        model.addAttribute("totalSales", dashboardService.totalSales());

        model.addAttribute("productStats", dashboardService.productSalesStats());
        model.addAttribute("statusStats", dashboardService.orderStatusStats());
        model.addAttribute("dailySalesStats", dashboardService.dailySalesStats());
        model.addAttribute("monthlySalesStats2", dashboardService.monthlySalesStats());

        return "admin/dashboard";
    }
	 
}
