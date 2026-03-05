package com.CoreCommerce.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class SalesStats {

    private String month;
    private Long sales;
    private String date;
    private Integer count;
    private String label;
    private Long value;
}
