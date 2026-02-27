package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.OrderItem;

@Mapper
public interface OrderRepository {

	Order findById(Long id);

    int updateStatus(Long orderId,
            String status,
            String courier,
            String trackingNumber);

	void insert(Order order);

	void insertOrderItem(OrderItem orderItem);

	int updateOrderToPaid(@Param("orderId") Long orderId);

	List<Order> findByMemberId(Long memberId);

	int countByMemberId(Long id);

	List<Order> findByMemberIdPaging( @Param("memberId") Long memberId,@Param("offset") int offset, @Param("size") int size);
	
	void createOrder(Long memberId);

	List<OrderItem> findByOrderId(Long orderId);

	List<OrderItem> getOrderItems(Long id);

	List<OrderItem> findOrderItems(Long orderId);

	void cancelOrder(Long orderId, Long id);

	List<OrderItem> findItems(Long orderId);

	List<Order> findAll();

	List<Order> findPaging(@Param("offset") int offset, @Param("size") int size);

	int countAll();

	int getTodayOrderCount();

	int getTodaySales();

	int getMonthlySales();

	int getTotalSales();

	List<java.util.Map<String, Object>> getWeeklySales();

	List<java.util.Map<String, Object>> getWeeklyOrderCount();





}
