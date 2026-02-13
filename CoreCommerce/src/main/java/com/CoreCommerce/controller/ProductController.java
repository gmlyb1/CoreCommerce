package com.CoreCommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.ProductRepository;


@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/products")
	public String productList(Model model) {
		
		List<Product> list = productRepository.findAll();
		model.addAttribute("products", list);
		return "product/list";
	}
}
