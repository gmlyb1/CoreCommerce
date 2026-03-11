package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Notification;
import com.CoreCommerce.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	
	public List<Notification> findByUserIdOrderByCreatedAtDesc(String userId) {
		return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}
	
	public void markAsRead(Long userId) {
		notificationRepository.markAsRead(userId);
	}
	
	public void insert(Notification note) {
		notificationRepository.insert(note);
	}
	
	public List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(String userId, String type) {
		return notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type);
	}
	
	public void markAsReadByType(String userId, String type) {
		notificationRepository.markAsReadByType(userId, type);
	}
}
