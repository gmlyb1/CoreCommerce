package com.CoreCommerce.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.CoreCommerce.domain.ChatMessage;
import com.CoreCommerce.domain.ChatRoom;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.ChatRepository;
import com.CoreCommerce.repository.ChatRoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;

	// =========================================
    // 1️⃣ 고객: 자신의 채팅방 목록
    // =========================================
    @GetMapping("/chat/rooms")
    public String customerRooms(Model model,HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if(loginUser == null) {
        	return "redirect:/";
        }
        String customerId = loginUser.getEmail();
       
    	//List<ChatRoom> rooms = chatRoomRepository.findByCustomerId(customerId);
    	List<ChatRoom> rooms = chatRoomRepository.findByCustomerId(customerId);
        model.addAttribute("rooms", rooms);
        model.addAttribute("customerId", customerId); // Thymeleaf에서 새 방 만들기용
        return "chat/list"; // 목록 페이지
    }
    
    @PostMapping("/chat/rooms/create")
    public String createRoom(ChatRoom chatRoom, HttpSession session) {
    	Member loginUser = (Member) session.getAttribute("loginUser");

    	if(loginUser == null) {
    		return "redirect:/";
    	}
    	
        chatRoom.setCustomerId(loginUser.getEmail()); // 세션 ID 사용
        chatRoom.setStatus("OPEN");
        chatRoom.setCreatedAt(LocalDateTime.now());

        chatRoomRepository.insertRoom(chatRoom); // void 반환
        Long roomId = chatRoom.getId();          // insert 후 MyBatis가 PK 채워줌

        return "redirect:/chat/room?roomId=" + roomId;
    }

    // =========================================
    // 3️⃣ 관리자: 모든 채팅방 목록
    // =========================================
    @GetMapping("/admin/chat/rooms")
    public String adminRooms(Model model,HttpSession session) {
    	
    	Member loginUser = (Member) session.getAttribute("loginUser");

    	if(loginUser != null && !"MANAGER".equals(loginUser.getRole())) {
    		return "redirect:/login";
    	}
    	
        List<ChatRoom> rooms = chatRoomRepository.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "admin/chat/list"; // 관리자 목록 페이지
    }

    // =========================================
    // 4️⃣ 채팅방 단건 입장 (고객/관리자 공용)
    // =========================================
    @GetMapping("/chat/room")
    public String room(@RequestParam("roomId") Long roomId, HttpSession session,
                       Model model) {
    	Member loginUser = (Member) session.getAttribute("loginUser");
    	if(loginUser == null) {
    		return "redirect:/";
    	}
    	String customerId = loginUser.getEmail();
    	
        ChatRoom room = chatRoomRepository.findById(roomId);
        
        model.addAttribute("room", room);
        model.addAttribute("customerId", customerId); // 고객이면 사용
        model.addAttribute("loginUser", loginUser);
        
        return "chat/room"; // 채팅방 페이지 (Thymeleaf)
    }
    
    @PostMapping("/chat/room/leave")
    public String leaveRoom(@RequestParam("roomId") Long roomId, HttpSession session) {
        ChatRoom room = chatRoomRepository.findById(roomId);
        if (room != null) {
            // 채팅방 상태를 CLOSE로 변경
            room.setStatus("CLOSE");
            room.setClosedAt(LocalDateTime.now());
            chatRoomRepository.updateStatus(room);

            // DB에서 메시지 삭제도 원하면
//            chatRepository.deleteByRoomId(roomId);
        }

        // 고객이면 목록으로, 관리자면 관리자 목록으로
        Member loginUser = (Member) session.getAttribute("loginUser");
        
        if ("MANAGER".equals(loginUser.getRole())) {
            return "redirect:/admin/chat/rooms";
        } else {
            return "redirect:/chat/rooms?customerId=" + loginUser.getEmail();
        }
    }
    
    @GetMapping("/chat/room/messages")
    @ResponseBody
    public List<ChatMessage> getRoomMessages(@RequestParam("roomId") Long roomId) {
        return chatRepository.findByRoomId(roomId);
    }
    
}
