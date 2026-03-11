package com.CoreCommerce.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.CoreCommerce.domain.ChatRoom;
import com.CoreCommerce.repository.ChatRepository;
import com.CoreCommerce.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ChatCleanupScheduler {

	 private final ChatRoomRepository chatRoomRepository;
	 private final ChatRepository chatRepository;

	    // 5분마다 실행
	    @Scheduled(cron = "0 */5 * * * *")
	    public void deleteClosedChatRooms() {

	    	System.out.println("채팅방 클린 시스템 작동!!!!!!!!!!!!!");
	    	
	        LocalDateTime targetTime = LocalDateTime.now().minusMinutes(5);

	        List<ChatRoom> rooms = chatRoomRepository.findClosedRoomsBefore(targetTime);

	        for(ChatRoom room : rooms) {
	            Long roomId = room.getId();
	            // 메시지 삭제
	            chatRepository.deleteByRoomId(roomId);
	            // 채팅방 삭제
	            chatRoomRepository.deleteRoom(roomId);
	            System.out.println("Deleted chat room : " + roomId);
	        }

	    }
}
