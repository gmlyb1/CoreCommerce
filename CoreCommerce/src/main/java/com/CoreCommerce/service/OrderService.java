package com.CoreCommerce.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Cart;
import com.CoreCommerce.domain.CartItem;
import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.OrderItem;
import com.CoreCommerce.repository.CartRepository;
import com.CoreCommerce.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }
    
    @Transactional
    public Long createOrder(Long memberId) {

        // 1️⃣ 회원의 장바구니 조회
        Cart cart = cartRepository.findCartByMemberId(memberId);

        if (cart == null) {
            throw new RuntimeException("장바구니 없음");
        }

        List<CartItem> items = cartService.getCartItems(cart.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있음");
        }

        // 2️⃣ 총 금액 계산
        int totalPrice = items.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();

        // 3️⃣ 주문 생성
        Order order = new Order();
        order.setMemberId(memberId);
        order.setTotalPrice(totalPrice);
        order.setStatus("READY");

        orderRepository.insert(order); // id 자동 세팅

        // 4️⃣ 주문 상품 저장
        for (CartItem item : items) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());

            orderRepository.insertOrderItem(orderItem);
        }

        // 5️⃣ 장바구니 비우기
        cartService.clearByMember(memberId);

        return order.getId();
    }


    public Order getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public void updateStatus(Long orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }
    
    
    
    
}
