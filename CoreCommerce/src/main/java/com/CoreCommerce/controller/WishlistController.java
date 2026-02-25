package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.WishlistRepository;
import com.CoreCommerce.service.WishlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;
	
	@PostMapping("/toggle")
	@ResponseBody
	public ResponseEntity<?> toggleWishlist(
	        @RequestParam Long productId,
	        HttpSession session) {

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LOGIN_REQUIRED");
	    }

	    boolean liked = wishlistService.toggleWishlist(
	            loginUser.getId(),   // ⚠ email 아님
	            productId
	    );

	    return ResponseEntity.ok(liked);
	}
	
	@GetMapping("/list")
	public String wishlistList(HttpSession session, Model model) {

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return "redirect:/login";
	    }

	    List<Product> list = wishlistService.getWishlist(loginUser.getId());
	    model.addAttribute("products", list);
	    model.addAttribute("loginUser", loginUser);

	    return "wishlist/list";
	}
}
