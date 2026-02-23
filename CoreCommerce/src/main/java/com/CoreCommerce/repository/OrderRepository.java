package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.OrderItem;

@Mapper
public interface OrderRepository {

	Order findById(Long id);

    int updateStatus(Long orderId, String status);

	void insert(Order order);

	void insertOrderItem(OrderItem orderItem);

	int updateOrderToPaid(@Param("orderId") Long orderId);

	List<Order> findByMemberId(Long memberId);
	
}
