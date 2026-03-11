package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Notification;

@Mapper
public interface NotificationRepository {

	List<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId")String userId);
    void markAsRead(Long userId);
	void insert(Notification note);
	List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(@Param("userId") String userId, String type);
	void markAsReadByType(@Param("userId") String userId, @Param("type") String type);

}
