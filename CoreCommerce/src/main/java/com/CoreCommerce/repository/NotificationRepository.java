package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Notification;

@Mapper
public interface NotificationRepository {

	 List<Notification> findByUserIdOrderByCreatedAtDesc(
		        @Param("userId") String userId,
		        @Param("size") int size,
		        @Param("offset") int offset
		    );

		    // 타입별 알림 조회
		    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(
		        @Param("userId") String userId,
		        @Param("type") String type,
		        @Param("size") int size,
		        @Param("offset") int offset
		    );

		    // 타입별 알림 개수 조회
		    int findByUserIdAndTypeCount(
		        @Param("userId") String userId,
		        @Param("type") String type
		    );

		    // 전체 알림 개수 조회
		    int findByUserIdCount(
		        @Param("userId") String userId
		    );

		    // 읽음 처리 (타입별)
		    void markAsReadByType(
		        @Param("userId") String userId,
		        @Param("type") String type
		    );

		    // 알림 클릭 시 읽음 처리
		    void updateIsRead(
		        @Param("id") Long id
		    );

		    // 알림 조회
		    Notification findById(
		        @Param("id") Long id
		    );

		    // 알림 저장
		    void insert(Notification note);

}
