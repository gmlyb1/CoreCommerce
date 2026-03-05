package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Coupon;
import com.CoreCommerce.domain.MemberCoupon;

@Mapper
public interface CouponRepository {
	
	// ===== Coupon =====

    void insertCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);

    void deleteCoupon(Long id);

    Coupon findById(Long id);

    List<Coupon> findAll(@Param("offset") int offset, @Param("size") int size);


    // ===== MemberCoupon =====

    void issueCouponToMember(MemberCoupon memberCoupon);

    List<MemberCoupon> findMemberCoupons(Long memberId);

    MemberCoupon findMemberCouponById(Long id);

    int useCoupon(@Param("memberCouponId") Long memberCouponId,
                  @Param("orderId") Long orderId);

    // 할인 계산
    int calculateDiscount(@Param("memberCouponId") Long memberCouponId,
                          @Param("orderPrice") int orderPrice);

	void increaseIssuedQuantity(Long couponId);
	
	void increaseUsedQuantity(Long couponId);

	void decreaseUsedQuantity(Long couponId);

	void cancelCouponUsage(Long memberCouponId);

	int countFindAll();
	
}
