package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Notification;

@Mapper
public interface NotificationRepository {

	List<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId")String userId,@Param("size")int size, @Param("offset") int offset);
    void markAsRead(Long userId);
	void insert(Notification note);
	List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(@Param("userId") String userId, String type,@Param("size")int size, @Param("offset") int offset);
	void markAsReadByType(@Param("userId") String userId, @Param("type") String type);
	int findByUserIdAndTypeCount(@Param("userId")String email,@Param("type")String type);
	int findByUserIdCount(String email);

}
