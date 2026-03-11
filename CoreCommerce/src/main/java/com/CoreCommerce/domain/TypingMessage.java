package com.CoreCommerce.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class TypingMessage {

	private Long roomId;
    private String sender;
    private boolean typing;
}
