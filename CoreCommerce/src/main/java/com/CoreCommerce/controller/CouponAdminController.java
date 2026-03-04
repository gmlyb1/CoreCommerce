package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CoreCommerce.domain.Coupon;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.service.CouponService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponAdminController {

	private final CouponService couponService;
	
	 // ✅ 쿠폰 목록 화면
    @GetMapping
    public String couponList(Model model,HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        List<Coupon> list = couponService.findAll();
        model.addAttribute("coupons", list);

        return "admin/coupon/coupon-list";
    }

    // ✅ 쿠폰 등록 화면
    @GetMapping("/create")
    public String createForm(HttpSession session) {
    	
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        return "admin/coupon/coupon-create";
    }

    // ✅ 쿠폰 등록 처리
    @PostMapping("/create")
    public String create(@ModelAttribute Coupon coupon,HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        couponService.insertCoupon(coupon);

        return "redirect:/admin/coupons";
    }

    // ✅ 쿠폰 수정 화면
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        Coupon coupon = couponService.findById(id);
        model.addAttribute("coupon", coupon);

        return "admin/coupon/coupon-edit";
    }

    // ✅ 쿠폰 수정 처리
    @PostMapping("/edit")
    public String update(@ModelAttribute Coupon coupon,HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        couponService.updateCoupon(coupon);

        return "redirect:/admin/coupons";
    }

    // ✅ 쿠폰 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(!"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        couponService.deleteCoupon(id);

        return "redirect:/admin/coupons";
    }
}
