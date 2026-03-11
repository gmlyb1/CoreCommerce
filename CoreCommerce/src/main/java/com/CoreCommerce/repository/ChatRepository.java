package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.ChatMessage;

@Mapper
public interface ChatRepository {

	void insertMessage(ChatMessage message);

	void deleteByRoomId(Long roomId);

	List<ChatMessage> findByRoomId(Long roomId);

	
}
