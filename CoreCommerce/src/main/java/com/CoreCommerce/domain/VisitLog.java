package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class VisitLog {

	  private Long id;
	  private String sessionId;
	  private String ipAddress;
	  private String requestUrl;
	  private String userAgent;
	  private String referer;
	  private LocalDateTime visitTime;
}
