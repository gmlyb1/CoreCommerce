package com.CoreCommerce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.CoreCommerce.domain.Member;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ModelAttribute("loginUser")
	public Member loginUser(HttpSession session) {
		return (Member) session.getAttribute("loginUser");
	}
}
