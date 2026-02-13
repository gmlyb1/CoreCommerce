package com.CoreCommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
