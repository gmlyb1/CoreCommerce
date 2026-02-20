package com.CoreCommerce.repository;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.Payment;

@Mapper
public interface PaymentRepository {

	int insert(Payment payment);

	void confirmPayment(String paymentKey, Long orderId, int amount);
}
