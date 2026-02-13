package com.CoreCommerce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

@Controller
public class MainController {

	@Autowired
	private MemberRepository memberRepository;
	
	@GetMapping("/")
	public String main(Model model, HttpServletRequest request) {
		
		// JWT 토큰으로 로그인 여부 확인 (예: 세션이나 헤더)
        String email = (String) request.getSession().getAttribute("loginEmail");
        if(email != null) {
            Member user = memberRepository.findByEmail(email).orElse(null);
            model.addAttribute("loginUser", user);
        }
		
		
		return "index";
	}
}
