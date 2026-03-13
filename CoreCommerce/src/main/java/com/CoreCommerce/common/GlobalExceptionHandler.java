package com.CoreCommerce.common;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "IllegalStateAlert";
    }
	
	@ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException e, Model model) {
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        return "error/500";
    }
	
//	@ExceptionHandler(RuntimeException.class)
//	public String RuntimeExceptionHandlerState(RuntimeException e , Model model) {
//		model.addAttribute("errorMessage", e.getMessage());
//		return "RuntimeAlert";
//	}
}
