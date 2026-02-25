package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryAnswer {

    private Long id;
    private Long inquiryId;
    private String adminId;
    private String answer;
    private LocalDateTime createdAt;
}