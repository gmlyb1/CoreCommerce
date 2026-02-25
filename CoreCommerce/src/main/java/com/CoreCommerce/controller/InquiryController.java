package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CoreCommerce.domain.Inquiry;
import com.CoreCommerce.domain.InquiryAnswer;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.InquiryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {
	
	@Autowired
	private InquiryRepository inquiryRepository;

	// 내 문의 목록
	 @GetMapping("/list")
	 public String inquiryList(HttpSession session, Model model, Inquiry inquiry) {
	
		 Member loginUser = (Member) session.getAttribute("loginUser");

		    if (loginUser == null) {
		        return "redirect:/login";
		    }

		    List<Inquiry> list;

		    // ✅ 관리자면 전체 조회
		    if ("MANAGER".equals(loginUser.getRole())) {
		        list = inquiryRepository.findAll();
		    } 
		    // ✅ 일반 사용자면 본인 글만 조회
		    else {
		    	inquiry.setMemberId(loginUser.getEmail());
		        list = inquiryRepository.findByMemberId(inquiry);
		    }

		    model.addAttribute("list", list);
	
	     return "inquiry/list";
	 }
	
	 // 문의 작성 폼
	 @GetMapping("/write")
	 public String writeForm() {
	     return "inquiry/write";
	 }
	
	 // 문의 저장
	 @PostMapping("/write")
	 public String writeInquiry(Inquiry inquiry,
	                            HttpSession session) {
	
	     Member loginUser = (Member) session.getAttribute("loginUser");
	     inquiry.setMemberId(loginUser.getEmail());
	
	     inquiryRepository.saveInquiry(inquiry);
	
	     return "redirect:/inquiry/list";
	 }
	
	 // 문의 상세 (답변 포함 조회)
	 @GetMapping("/{id}")
	 public String detail(@PathVariable Long id,
	                      Model model,HttpSession session ) {
	
		 Member loginUser = (Member) session.getAttribute("loginUser");
		 
	     Inquiry inquiry = inquiryRepository.findById(id);
	     InquiryAnswer answer = inquiryRepository.findAnswerByInquiryId(id);
	
	     model.addAttribute("inquiry", inquiry);
	     model.addAttribute("answer", answer);
	     model.addAttribute("loginUser", loginUser);
	
	     return "inquiry/detail";
	 }
	
	 // 관리자 문의 목록
	 @GetMapping("/admin/list")
	 public String adminList(Model model) {
	
	     List<Inquiry> list = inquiryRepository.findAll();
	
	     model.addAttribute("list", list);
	
	     return "admin/inquiry/list";
	 }
	
	 @PostMapping("/admin/answer")
	 public String saveAnswer(InquiryAnswer answer,
	                          HttpSession session) {

	     Member admin = (Member) session.getAttribute("loginUser");

	     if (admin == null || !"MANAGER".equals(admin.getRole())) {
	         return "redirect:/";
	     }

	     answer.setAdminId(admin.getEmail());

	     inquiryRepository.saveAnswer(answer);
	     inquiryRepository.updateStatusToDone(answer.getInquiryId());

	     return "redirect:/inquiry/" + answer.getInquiryId();
	 }
	 
	 @GetMapping("/edit/{id}")
	 public String editForm(@PathVariable Long id,
	                        HttpSession session,
	                        Model model) {

	     Member loginUser = (Member) session.getAttribute("loginUser");

	     if (loginUser == null) {
	         return "redirect:/login";
	     }

	     Inquiry inquiry = inquiryRepository.findById(id);
	     
	     // 본인 글만 수정 가능
	     if (!inquiry.getMemberId().equals(loginUser.getEmail())) {
	         return "redirect:/inquiry/list";
	     }

	     model.addAttribute("inquiry", inquiry);

	     return "inquiry/edit";
	 }
	 
	 @PostMapping("/edit")
	 public String updateInquiry(Inquiry inquiry,
	                             HttpSession session) {

	     Member loginUser = (Member) session.getAttribute("loginUser");

	     if (loginUser == null) {
	         return "redirect:/login";
	     }

	     inquiry.setMemberId(loginUser.getEmail());

	     inquiryRepository.updateInquiry(inquiry);

	     return "redirect:/inquiry/" + inquiry.getId();
	 }
	 
	 @PostMapping("/delete/{id}")
	 public String deleteInquiry(@PathVariable Long id,
	                             HttpSession session) {

	     Member loginUser = (Member) session.getAttribute("loginUser");

	     if (loginUser == null) {
	         return "redirect:/login";
	     }

	     Inquiry inquiry = inquiryRepository.findById(id);

	     if (!inquiry.getMemberId().equals(loginUser.getEmail())) {
	         return "redirect:/inquiry/list";
	     }

	     inquiryRepository.deleteInquiry(id);

	     return "redirect:/inquiry/list";
	 }
	 
	 @PostMapping("/admin/answer/edit")
	 public String updateAnswer(InquiryAnswer answer,
	                            HttpSession session) {

	     Member admin = (Member) session.getAttribute("loginUser");

	     if (admin == null || !"MANAGER".equals(admin.getRole())) {
	         return "redirect:/";
	     }

	     inquiryRepository.updateAnswer(answer);

	     return "redirect:/inquiry/" + answer.getInquiryId();
	 }
	 
	 @PostMapping("/admin/answer/delete/{inquiryId}")
	 public String deleteAnswer(@PathVariable Long inquiryId,
	                            HttpSession session) {

	     Member admin = (Member) session.getAttribute("loginUser");

	     if (admin == null || !"MANAGER".equals(admin.getRole())) {
	         return "redirect:/";
	     }

	     inquiryRepository.deleteAnswer(inquiryId);

	     inquiryRepository.updateStatusToDone(inquiryId); 
	     // 필요하면 WAIT로 변경하는 메서드 따로 만들어도 됨

	     return "redirect:/inquiry/" + inquiryId;
	 }
	 
	 @GetMapping("/admin/answer/edit/{inquiryId}")
	 public String editAnswerForm(@PathVariable Long inquiryId,
	                              HttpSession session,
	                              Model model) {

	     Member admin = (Member) session.getAttribute("loginUser");

	     if (admin == null || !"MANAGER".equals(admin.getRole())) {
	         return "redirect:/";
	     }

	     InquiryAnswer answer =
	         inquiryRepository.findAnswerByInquiryId(inquiryId);

	     model.addAttribute("answer", answer);

	     return "/inquiry/answerEdit";
	 }
	
}
