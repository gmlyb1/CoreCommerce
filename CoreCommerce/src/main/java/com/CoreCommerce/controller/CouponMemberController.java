package com.CoreCommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Coupon;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.MemberCoupon;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.service.CouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponMemberController {

	private final CouponService couponService;

	@GetMapping("/list")
	public String couponPage(HttpSession session, Model model,@RequestParam(defaultValue = "1") int page){

	    Member loginUser =
	            (Member) session.getAttribute("loginUser");

	    if(loginUser == null){
	        return "redirect:/login";
	    }
	    
	    int size = 10;
    	int offset = (page -1) * size;

	    // ✅ 전체 공개 쿠폰 조회
	    List<Coupon> coupons = couponService.findAll(offset,size);
	    int totalCount = couponService.countFindAll();

	    // ✅ 사용자가 이미 받은 쿠폰
	    List<MemberCoupon> myCoupons =
	            couponService.findMemberCoupons(loginUser.getId());

	    List<Long> myCouponIds =
	            myCoupons.stream()
	                    .map(MemberCoupon::getCouponId)
	                    .collect(Collectors.toList());

	    model.addAttribute("myCouponIds", myCouponIds);
	    model.addAttribute("coupons", coupons);
	    model.addAttribute("myCoupons", myCoupons);
	    model.addAttribute("pagination", new Pagination(page, size, totalCount));
	    
	    return "coupons/list";
	}
	
    // ✅ 특정 회원에게 쿠폰 지급
    @PostMapping("/receive")
    public String receiveCoupon(
            @RequestParam Long couponId,
            HttpSession session) {

    	Member loginUser = (Member) session.getAttribute("loginUser");
    	
    	if(loginUser == null) {
    		return "redirect:/login";
    	}
    	
    	couponService.issueCoupon(loginUser.getId(), couponId);

        return "redirect:/coupons/list";
    }
}
