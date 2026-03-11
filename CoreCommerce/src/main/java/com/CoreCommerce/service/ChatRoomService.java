package com.CoreCommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.ChatRoom;
import com.CoreCommerce.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	
	public List<ChatRoom> findByCustomerId(String customerId) {
		return chatRoomRepository.findByCustomerId(customerId);
	}

	public void insertRoom(ChatRoom chatRoom) {
		chatRoomRepository.insertRoom(chatRoom);
	}

	public List<ChatRoom> findAllRooms(){
		return chatRoomRepository.findAllRooms();
	}

	public ChatRoom findById(Long id) {
		return chatRoomRepository.findById(id);
	}

	public void updateStatus(ChatRoom room) {
		chatRoomRepository.updateStatus(room);
	}
	
	public void deleteClosedRoomsOlderThan(LocalDateTime fiveMinutesAgo) {
		chatRoomRepository.deleteClosedRoomsOlderThan(fiveMinutesAgo);
	}
	
	public List<ChatRoom> findClosedRoomsBefore(LocalDateTime targetTime) {
		return chatRoomRepository.findClosedRoomsBefore(targetTime);
	}
	
	public void deleteRoom(Long roomId) {
		chatRoomRepository.deleteRoom(roomId);
	}
	
}
