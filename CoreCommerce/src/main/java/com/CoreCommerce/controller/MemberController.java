package com.CoreCommerce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CoreCommerce.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MemberController {

	 // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // src/main/resources/templates/login.html
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // src/main/resources/templates/register.html
    }
    
    @GetMapping("/member/profile")
    public String profilePage(HttpSession session, Model model) {
    	return "/member/profile";
    }
}
