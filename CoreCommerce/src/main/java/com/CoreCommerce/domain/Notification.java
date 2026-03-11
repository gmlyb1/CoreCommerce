package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class Notification {

	private Long id;
    private String userId;
    private String type;
    private String content;
    private String link;
    private LocalDateTime createdAt;
    private boolean isRead;
}
