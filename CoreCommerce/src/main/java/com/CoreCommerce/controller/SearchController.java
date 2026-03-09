package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Inquiry;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Notice;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.InquiryRepository;
import com.CoreCommerce.repository.NoticeRepository;
import com.CoreCommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

	private final ProductRepository productRepository;
	private final NoticeRepository noticeRepository;
	private final InquiryRepository inquiryRepository;
	
	@GetMapping
    public String search(HttpSession session,@RequestParam("keyword") String keyword, Model model) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		}
		
        // 1. 상품 검색
        List<Product> productList = productRepository.searchByKeyword(keyword);

        // 2. 공지사항 검색
        List<Notice> noticeList = noticeRepository.searchByKeyword(keyword);

        // 3. 1:1 문의 검색
//        List<Inquiry> inquiryList = inquiryRepository.searchByKeyword(keyword);
        List<Inquiry> inquiryList;
        
        if("MANAGER".equals(loginUser.getRole())) {
        	inquiryList = inquiryRepository.searchByKeyword(keyword);
        }else {
        	inquiryList = inquiryRepository.searchByKeywordAndMember(keyword,loginUser.getEmail());
        }
        
        model.addAttribute("keyword", keyword);
        model.addAttribute("productList", productList);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("inquiryList", inquiryList);

        return "search/results"; // 통합검색 결과 JSP
    }
	
}
