package com.CoreCommerce.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping("/")
	public String main(Model model, HttpSession session) throws Exception{
	    Member loginMember = (Member) session.getAttribute("loginUser");
	    
	    if (loginMember != null) {
	        model.addAttribute("loginUser", loginMember);
	    }

	    return "index";
	}
}
