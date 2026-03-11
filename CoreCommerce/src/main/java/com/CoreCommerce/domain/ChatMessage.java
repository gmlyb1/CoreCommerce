package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;

import com.CoreCommerce.service.ChatService;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ChatMessage {

	private Long id;
	private Long roomId;
	private String sender; // CUSTOMER / MANAGER
	private String message;
	private LocalDateTime createdAt;
}
