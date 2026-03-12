package com.CoreCommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Notification;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.repository.NotificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notifications")
public class NotificationController {
	
	private final NotificationRepository notificationRepository;

    @GetMapping
    public String listNotifications(@RequestParam(defaultValue ="1") int page,HttpSession session, Model model,@RequestParam(required = false) String type) {
    	 	Member loginUser = (Member) session.getAttribute("loginUser");

    	 	int size = 10;
    	 	int offset = (page - 1 ) * size;
    	 	int totalCount;
//    	    if (loginUser == null || 
//    	        !(loginUser.getRole().equals("MANAGER") || loginUser.getRole().equals("PRODUCTER"))) {
//    	        return "redirect:/login";
//    	    }

    	    List<Notification> notifications;
    	    if (type != null && !type.isEmpty()) {
    	        notifications = notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(loginUser.getEmail(), type,size,offset);
    	        totalCount = notificationRepository.findByUserIdAndTypeCount(loginUser.getEmail(),type);
    	    } else {
    	        notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(loginUser.getEmail(),size,offset);
    	        totalCount = notificationRepository.findByUserIdCount(loginUser.getEmail());
    	    }
    	    
    	    model.addAttribute("notifications", notifications);
    	    model.addAttribute("currentType", type); // UI에서 현재 선택된 버튼 표시용
    	    model.addAttribute("pagination", new Pagination(page, size, totalCount));
    	    // 읽음 처리 (전체 or 필터링 타입)
    	    notificationRepository.markAsReadByType(loginUser.getEmail(), type);

    	    return "admin/notifications/list";
    }
}
