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
	
	@ExceptionHandler(RuntimeException.class)
	public String RuntimeExceptionHandlerState(RuntimeException e , Model model) {
		model.addAttribute("errorMessage", e.getMessage());
		return "RuntimeAlert";
	}
}
