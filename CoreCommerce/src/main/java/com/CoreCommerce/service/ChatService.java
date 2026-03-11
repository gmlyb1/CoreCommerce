package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.ChatMessage;
import com.CoreCommerce.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatRepository chatRepository; 

	public void insertMessage(ChatMessage message) {
		chatRepository.insertMessage(message);
	}
	
	public void deleteByRoomId(Long roomId) {
		chatRepository.deleteByRoomId(roomId);
	}
	
	public List<ChatMessage> findByRoomId(Long roomId) {
		return chatRepository.findByRoomId(roomId);
	}

}
