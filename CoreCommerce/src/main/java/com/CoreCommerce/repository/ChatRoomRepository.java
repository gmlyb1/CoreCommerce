package com.CoreCommerce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.ChatRoom;

@Mapper
public interface ChatRoomRepository {

	List<ChatRoom> findByCustomerId(String customerId);

	void insertRoom(ChatRoom chatRoom);

    List<ChatRoom> findAllRooms();

    ChatRoom findById(Long id);

    void updateStatus(ChatRoom room);

	void deleteClosedRoomsOlderThan(@Param("dateTime")LocalDateTime fiveMinutesAgo);

	List<ChatRoom> findClosedRoomsBefore(LocalDateTime targetTime);

	void deleteRoom(Long roomId);



}
