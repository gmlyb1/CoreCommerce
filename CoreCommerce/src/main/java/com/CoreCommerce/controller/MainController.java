package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Popup;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.domain.VisitLog;
import com.CoreCommerce.repository.BannerRepository;
import com.CoreCommerce.repository.MemberRepository;
import com.CoreCommerce.repository.PopupRepository;
import com.CoreCommerce.repository.ProductRepository;
import com.CoreCommerce.repository.VisitLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PopupRepository popupRepository;
	
	@Autowired
	private BannerRepository bannerRepository;
	
	@Autowired
	private VisitLogRepository visitLogRepository;
	
	@GetMapping("/")
	public String main(VisitLog log, HttpServletRequest request,Model model, HttpSession session) throws Exception{
	    
		log.setSessionId(request.getSession().getId());
		log.setIpAddress(request.getRemoteAddr());
		log.setRequestUrl(request.getRequestURI());
		log.setUserAgent(request.getHeader("User-agent"));
		log.setReferer(request.getHeader("Referer"));
		visitLogRepository.insert(log);
		
		Member loginMember = (Member) session.getAttribute("loginUser");
	    
	    if (loginMember != null) {
	        model.addAttribute("loginUser", loginMember);
	    }
	    
	    model.addAttribute("MainPageList", productRepository.findMainPage());
	    model.addAttribute("AllProductList", productRepository.findAll());
	    model.addAttribute("offerProductList", productRepository.findOfferProductList());
	    model.addAttribute("popup", popupRepository.findActivePopup());
	    model.addAttribute("bannerList", bannerRepository.getAllBanners());
	    
	    return "index";
	}
}
