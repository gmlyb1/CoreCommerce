package com.CoreCommerce.domain;

import java.time.LocalDate;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class Coupon {

		private Long id;
	    private String name;
	    private String type;
	    private int discountValue;
	    private int minOrderPrice;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private boolean active;
	    private LocalDate createdAt;
	    private int totalQuantity;   // 총 발급 수량
	    private int issuedQuantity;
	    private int usedQuantity;
}
