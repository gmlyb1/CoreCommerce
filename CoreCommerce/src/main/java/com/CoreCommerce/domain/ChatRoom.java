package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ChatRoom {

	private Long id;             // 채팅방 ID
    private String customerId;     // 고객 ID
    private Long managerId;      // 관리자 ID (배정된 경우)
    private String status;       // OPEN / IN_PROGRESS / CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
}
