package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Popup {

    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean isActive;
    private LocalDateTime createdAt;
}
