package com.CoreCommerce.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Banner {

    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private String imageUrl;
    private String linkUrl;
    private int sortOrder;
    private String useYn;
}
