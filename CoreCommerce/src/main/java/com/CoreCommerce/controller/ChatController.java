package com.CoreCommerce.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.CoreCommerce.domain.ChatMessage;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Notification;
import com.CoreCommerce.domain.TypingMessage;
import com.CoreCommerce.repository.ChatRepository;
import com.CoreCommerce.repository.ChatRoomRepository;
import com.CoreCommerce.repository.MemberRepository;
import com.CoreCommerce.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatRoomRepository chatRoomRepository;
	private final NotificationRepository notificationRepository;
	
	 @MessageMapping("/chat.sendMessage")
	    public void sendMessage(ChatMessage message) {

		    // 1️⃣ 메시지 DB 저장
		    message.setCreatedAt(LocalDateTime.now());
		    chatRepository.insertMessage(message);
		    
		    List<Member> targets = memberRepository.findByRoles(Arrays.asList("MANAGER", "PRODUCTER"));

	        for (Member target : targets) {
	            // 3️⃣ Notification 생성
	            Notification note = new Notification();
	            note.setUserId(target.getEmail());
	            note.setType("CHAT");
	            note.setContent("고객 '" + message.getSender() + "'이 메시지를 보냈습니다: " + message.getMessage());
	            //note.setLink("/admin/chat/rooms/" + message.getRoomId());
	            note.setLink("/chat/room?roomId=" + message.getRoomId() + "&customerId=" + message.getSender());
	            note.setCreatedAt(LocalDateTime.now());
	            note.setRead(false);
	            notificationRepository.insert(note);

	            // 4️⃣ 실시간 WebSocket 알림 전송
	            messagingTemplate.convertAndSend(
	                    "/topic/notifications/" + target.getId(),
	                    note
	            );
	        }

	        // 2️⃣ 해당 방(roomId) 구독자에게만 브로드캐스트
	        messagingTemplate.convertAndSend(
	                "/topic/room/" + message.getRoomId(),
	                message
	        );
	    }
	 
	 @MessageMapping("/chat.typing")
	 public void typingStatus(TypingMessage typing) {
		 
	     // 해당 방 구독자에게 브로드캐스트
	     messagingTemplate.convertAndSend(
	         "/topic/room/" + typing.getRoomId() + "/typing",
	         typing
	     );
	 }
	
}
