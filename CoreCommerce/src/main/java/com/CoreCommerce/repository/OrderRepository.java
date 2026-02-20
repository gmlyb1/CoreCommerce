package com.CoreCommerce.repository;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.OrderItem;

@Mapper
public interface OrderRepository {

	Order findById(Long id);

    int updateStatus(Long orderId, String status);

	void insert(Order order);

	void insertOrderItem(OrderItem orderItem);
}
