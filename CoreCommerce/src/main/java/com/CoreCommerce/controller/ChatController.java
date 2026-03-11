package com.CoreCommerce.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.CoreCommerce.domain.ChatMessage;
import com.CoreCommerce.domain.TypingMessage;
import com.CoreCommerce.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatRepository chatRepository;
	private final SimpMessagingTemplate messagingTemplate;
	
	 @MessageMapping("/chat.sendMessage")
	    public void sendMessage(ChatMessage message) {

	        // 1️⃣ 메시지 DB 저장
	        message.setCreatedAt(LocalDateTime.now());
	        chatRepository.insertMessage(message);

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
