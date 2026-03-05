package com.CoreCommerce.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Notice;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.service.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Controller
@RequestMapping("/notice")
public class NoticeController {

	private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 목록
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       Model model) {

        int size = 10; 
        int offset = (page - 1) * size;   // 🔥 여기서 계산

        List<Notice> notices =
                noticeService.findPaged(offset, size);

        int totalCount = noticeService.countAll();
        
        model.addAttribute("notices", notices);
        model.addAttribute("pagination", new Pagination(page, size, totalCount));

        return "notice/list";
    }

    // 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findById(id));
        return "notice/detail";
    }

    // 관리자 작성 페이지
    @GetMapping("/admin/write")
    public String writeForm(HttpSession session) {
    	
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	
    	if(!loginUser.getRole().equals("MANAGER")) {
    		return "redirect:/";
    	}
    	
        return "notice/write";
    }
    
    @PostMapping("/write")
    public String write(Notice notice, HttpSession session) {

        Member loginUser =
            (Member) session.getAttribute("loginUser");

        notice.setWriter(loginUser.getName());

        if (notice.getIsPinned() == null) {
            notice.setIsPinned(false);
        }

        noticeService.insert(notice);

        return "redirect:/notice";
    }
    
    @GetMapping("/admin/edit/{id}")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getRole().equals("MANAGER")) {
            return "redirect:/login";
        }

        Notice notice = noticeService.findById(id);
        model.addAttribute("notice", notice);

        return "notice/edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute Notice notice,
                       HttpSession session) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getRole().equals("MANAGER")) {
            return "redirect:/login";
        }

        notice.setId(id);
        noticeService.update(notice);

        return "redirect:/notice/" + id;
    }

    /* =========================
       삭제
    ========================== */
    @PostMapping("/admin/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getRole().equals("MANAGER")) {
            return "redirect:/login";
        }

        noticeService.delete(id);

        return "redirect:/notice";
    }
}
