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
import com.CoreCommerce.domain.Pagination;
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
	
//	@GetMapping
//    public String search(@RequestParam(defaultValue = "1") int pPage,@RequestParam(defaultValue = "1") int nPage,@RequestParam(defaultValue = "1") int iPage,HttpSession session,@RequestParam("keyword") String keyword, Model model) {
//
//		Member loginUser = (Member) session.getAttribute("loginUser");
//		
//		if(loginUser == null) {
//			return "redirect:/login";
//		}
//		
//		int size = 8;
//		int offset = (pPage - 1) * size;
//		
//        // 1. 상품 검색
//        List<Product> productList = productRepository.searchByKeyword(keyword,offset,size);
//        int pTotCount = productRepository.pTotCnt(keyword);
//        
//        
//        // 2. 공지사항 검색
//        List<Notice> noticeList = noticeRepository.searchByKeyword(keyword,offset,size);
//        int nTotCount = noticeRepository.nTotCnt(keyword);
//        
//        // 3. 1:1 문의 검색
//        List<Inquiry> inquiryList;
//        int iTotCount;
//        
//        if("MANAGER".equals(loginUser.getRole())) {
//        	inquiryList = inquiryRepository.searchByKeyword(keyword,offset,size);
//        	iTotCount = inquiryRepository.iTotCount(keyword);
//        }else {
//        	inquiryList = inquiryRepository.searchByKeywordAndMember(keyword,loginUser.getEmail(),offset,size);
//        	iTotCount = inquiryRepository.iTotCountForMember(keyword,loginUser.getEmail());
//        }
//        
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("productList", productList);
//        model.addAttribute("noticeList", noticeList);
//        model.addAttribute("inquiryList", inquiryList);
//        
//        // 페이징
//        model.addAttribute("pTotCount", new Pagination(pPage, size, pTotCount));
//        model.addAttribute("nTotCount", new Pagination(nPage, size, nTotCount));
//        model.addAttribute("iTotCount", new Pagination(iPage, size, iTotCount));
//
//        return "search/results"; // 통합검색 결과 JSP
//    }
	
	@GetMapping
	public String search(
	        @RequestParam(defaultValue = "1") int pPage,
	        @RequestParam(defaultValue = "1") int nPage,
	        @RequestParam(defaultValue = "1") int iPage,
	        @RequestParam("keyword") String keyword,
	        HttpSession session, Model model) {
	    
	    // 상품
	    int size = 8;
	    int pOffset = (pPage - 1) * size;
	    List<Product> productList = productRepository.searchByKeyword(keyword, pOffset, size);
	    int pTotCount = productRepository.pTotCnt(keyword);
	    model.addAttribute("productList", productList);
	    model.addAttribute("pTotCount", new Pagination(pPage, size, pTotCount));

	    // 공지
	    int nOffset = (nPage - 1) * size;
	    List<Notice> noticeList = noticeRepository.searchByKeyword(keyword, nOffset, size);
	    int nTotCount = noticeRepository.nTotCnt(keyword);
	    model.addAttribute("noticeList", noticeList);
	    model.addAttribute("nTotCount", new Pagination(nPage, size, nTotCount));

	    // 문의
	    int iOffset = (iPage - 1) * size;
	    List<Inquiry> inquiryList;
	    int iTotCount;
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if("MANAGER".equals(loginUser.getRole())) {
	        inquiryList = inquiryRepository.searchByKeyword(keyword, iOffset, size);
	        iTotCount = inquiryRepository.iTotCount(keyword);
	    } else {
	        inquiryList = inquiryRepository.searchByKeywordAndMember(keyword, loginUser.getEmail(), iOffset, size);
	        iTotCount = inquiryRepository.iTotCountForMember(keyword, loginUser.getEmail());
	    }
	    model.addAttribute("inquiryList", inquiryList);
	    model.addAttribute("iTotCount", new Pagination(iPage, size, iTotCount));

	    model.addAttribute("keyword", keyword);
	    return "search/results";
	}
	
}
