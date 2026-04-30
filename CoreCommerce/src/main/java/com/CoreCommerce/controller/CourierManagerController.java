package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Courier;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.repository.CourierRepository;
import com.CoreCommerce.service.CouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/courier")
public class CourierManagerController {

	private final CourierRepository courierRepository;
	
	@GetMapping("/list")
	public String courierManagelistPage(@RequestParam(defaultValue = "1") int page,HttpSession session,Model model,Courier courier) throws Exception{
		
		Member loginUser =
	            (Member) session.getAttribute("loginUser");

	    if(loginUser == null){
	        return "redirect:/login";
	    }
	    
	    int size = 10;
    	int offset = (page -1) * size;
		
		List<Courier> courierList = courierRepository.selectCourierList(offset,size);
		int totalCount = courierRepository.countFindAll();
		
		model.addAttribute("courierList", courierList);
		model.addAttribute("pagination", new Pagination(page, size, totalCount));
		
		return "/admin/courier/list";
	}
	
	@GetMapping("/create")
	public String createPage(Courier courier) throws Exception{
		
		return "/admin/courier/create";
	}
	
	@PostMapping("/create")
	public String insertCourier(Courier courier) throws Exception{
		
		courierRepository.insertCourier(courier);
		
		return "redirect:/admin/courier/list";
	}
	
	@PostMapping("/delete/{courier_no}")
	public String deleteCourier(@PathVariable Long courier_no,Courier courier) throws Exception{
		
		courierRepository.deleteCourier(courier_no);
		
		return "redirect:/admin/courier/list";
	}
	
	
	
}
