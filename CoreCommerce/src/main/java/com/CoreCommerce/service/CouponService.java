package com.CoreCommerce.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Coupon;
import com.CoreCommerce.domain.MemberCoupon;
import com.CoreCommerce.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

	private final CouponRepository couponRepository;
	
	// ===== Coupon =====
	public void insertCoupon(Coupon coupon) {
		couponRepository.insertCoupon(coupon);
	}
	
	public void updateCoupon(Coupon coupon) {
		couponRepository.updateCoupon(coupon);
	}
	
	public void deleteCoupon(Long id) {
		couponRepository.deleteCoupon(id);
	}
	
	public Coupon findById(Long id) {
		return couponRepository.findById(id);
	}
	
	public List<Coupon> findAll(int offset, int size) {
		return couponRepository.findAll(offset, size);
	}
	
	// ===== MemberCoupon =====
	
	@Transactional
	public void issueCoupon(Long memberId, Long couponId) {

	    // 1️⃣ 쿠폰 조회
	    Coupon coupon = couponRepository.findById(couponId);

	    if (coupon == null || !coupon.isActive()) {
	        throw new IllegalStateException("사용 불가능한 쿠폰입니다.");
	    }

	    // 2️⃣ 발급 수량 체크
	    if (coupon.getTotalQuantity() > 0 &&
	        coupon.getIssuedQuantity() >= coupon.getTotalQuantity()) {

	        throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
	    }

	    // 3️⃣ 이미 받은 쿠폰인지 체크 (중복 방지)
	    List<MemberCoupon> list =
	            couponRepository.findMemberCoupons(memberId);

	    boolean already = list.stream()
	            .anyMatch(mc -> mc.getCouponId().equals(couponId));

	    if (already) {
	        throw new IllegalStateException("이미 보유한 쿠폰입니다.");
	    }

	    // 4️⃣ 회원 쿠폰 생성
	    MemberCoupon memberCoupon = new MemberCoupon();
	    memberCoupon.setMemberId(memberId);
	    memberCoupon.setCouponId(couponId);

	    couponRepository.issueCouponToMember(memberCoupon);

	    // 5️⃣ 쿠폰 발급 수량 증가
	    couponRepository.increaseIssuedQuantity(couponId);
	}
	
	public void issueCouponToMember(MemberCoupon memberCoupon) {
		couponRepository.issueCouponToMember(memberCoupon);
	}
	
	public List<MemberCoupon> findMemberCoupons(Long memberId) {
		return couponRepository.findMemberCoupons(memberId);
	}
	
	public MemberCoupon findMemberCouponById(Long id) {
		return couponRepository.findMemberCouponById(id);
	}
	
//	public int useCoupon(Long memberCouponId, Long orderId) {
//		return couponRepository.useCoupon(memberCouponId, orderId);
//	}
	@Transactional
	public void useCoupon(Long memberCouponId, Long orderId) {

	    MemberCoupon mc = couponRepository.findMemberCouponById(memberCouponId);

	    if (mc == null || mc.isUsed()) {
	        throw new IllegalStateException("이미 사용된 쿠폰입니다.");
	    }

	    // 1️⃣ member_coupon 사용 처리
	    couponRepository.useCoupon(memberCouponId, orderId);

	    // 2️⃣ coupon 사용 수량 증가
	    couponRepository.increaseUsedQuantity(mc.getCouponId());
	}
	
	public int calculateDiscount(Long memberCouponId,int orderPrice) {
		return couponRepository.calculateDiscount(memberCouponId, orderPrice);
	}
	
	public void increaseUsedQuantity(Long couponId) {
		couponRepository.increaseUsedQuantity(couponId);
	}
	
	public void decreaseUsedQuantity(Long couponId) {
		couponRepository.decreaseUsedQuantity(couponId);
	}
	
	@Transactional
	public void cancelCouponUsage(Long memberCouponId) {

	    MemberCoupon mc = couponRepository.findMemberCouponById(memberCouponId);

	    if (mc == null || !mc.isUsed()) {
	        return;
	    }

	    // member_coupon 복구
	    couponRepository.cancelCouponUsage(memberCouponId);

	    // coupon 사용 수량 감소
	    couponRepository.decreaseUsedQuantity(mc.getCouponId());
	}

	public int countFindAll() {
		return couponRepository.countFindAll();
	}
}
