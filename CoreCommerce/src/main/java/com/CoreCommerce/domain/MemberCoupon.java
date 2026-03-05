package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class MemberCoupon {

 	private Long id;
    private Long memberId;
    private Long couponId;
    private boolean used;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    private Long orderId;
    private Coupon coupon;
}
