package com.CoreCommerce.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

@Controller
@RequestMapping("/admin/member")
public class MemberManageController {

	@Autowired
	private MemberRepository memberRepository;
	
	 @GetMapping("/list")
    public String AdminMemberListPage(HttpSession session,Model model, Member member) {
	 
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if(!loginUser.getRole().equals("MANAGER")) {
			return "redirect:/";
		}
		 
	 	List<Member> memberList = memberRepository.findAll(member);
	 	model.addAttribute("members", memberList);
	 	model.addAttribute("search", member);
	 	
    	return "/admin/member/list";
    }
	 
	@PostMapping("/role")
    public String changeRole(@RequestParam Long memberId,
                             @RequestParam String role) {

		
		role = role.replace(",", "");
		
		memberRepository.changeRole(memberId, role);

        return "redirect:/admin/member/list";
    }
	
	@PostMapping("/lock")
	public String lockAccount(@RequestParam Long memberId,
	                          @RequestParam int days) {

		LocalDateTime lockedUntil = null;

	    // 🔥 0이면 영구 잠금
	    if (days > 0) {
	        lockedUntil = LocalDateTime.now().plusDays(days);
	    }

	    memberRepository.lockAccount(memberId, true, lockedUntil);

	    return "redirect:/admin/member/list";
	}
}
