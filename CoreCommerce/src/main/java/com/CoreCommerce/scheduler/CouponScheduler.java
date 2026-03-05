package com.CoreCommerce.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.CoreCommerce.service.CouponService;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CouponScheduler {

	private final CouponService couponService;
	
	@Scheduled(cron = "0 0/3 * * * ?")
//	 @Scheduled(cron = "0 0/30 * * * ?")
    public void autoExpireCoupons() {

        couponService.expireCoupons();

        System.out.println("🔵 쿠폰 자동 만료 체크 실행 : " +  java.time.LocalDateTime.now());
    }
	
}
