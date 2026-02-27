package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.domain.VisitLog;
import com.CoreCommerce.repository.VisitLogRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/visitLog")
@RequiredArgsConstructor
public class VisitLogController {

	@Autowired
	private VisitLogRepository visitLogRepository;
	
	@GetMapping("/list")
	public String visitList(@RequestParam(defaultValue = "1") int page,
	                        Model model,
	                        HttpSession session){

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    // 🔴 로그인 안 되어 있으면
	    if(loginUser == null){
	        return "redirect:/login";
	    }

	    // 🔴 관리자 체크 (Null 안전)
	    if(!"MANAGER".equals(loginUser.getRole())){
	        return "redirect:/";
	    }

	    int size = 10;
	    int totalCount = visitLogRepository.countAll();

	    Pagination pagination = new Pagination(page, size, totalCount);

	    List<VisitLog> list =
	            visitLogRepository.findAll(
	                    pagination.getOffset(),
	                    pagination.getSize()
	            );

	    model.addAttribute("list", list);
	    model.addAttribute("pagination", pagination);

	    return "admin/visitLog/list";
	}
	
}
