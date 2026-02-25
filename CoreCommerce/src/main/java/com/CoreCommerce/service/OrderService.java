package com.CoreCommerce.service;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Cart;
import com.CoreCommerce.domain.CartItem;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.OrderItem;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.CartRepository;
import com.CoreCommerce.repository.OrderRepository;
import com.CoreCommerce.repository.ProductRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Autowired
    private HttpSession session;
    
    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartService cartService,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }
    
    @Transactional
    public Long createOrder(Long memberId) {

        // 1️⃣ 회원 장바구니 조회
        Cart cart = cartRepository.findCartByMemberId(memberId);

        if (cart == null) {
            throw new RuntimeException("장바구니 없음");
        }

        // 2️⃣ 장바구니 아이템 전체 조회
        List<CartItem> items =
                cartService.getCartItems(cart.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있음");
        }

        // 3️⃣ 총 금액 계산
        int totalPrice = items.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();

        // 4️⃣ 주문 생성
        Order order = new Order();
        order.setMemberId(memberId);
        order.setTotalPrice(totalPrice);
        order.setStatus("READY");

        orderRepository.insert(order); // 🔥 id 자동 세팅

        // 5️⃣ 주문 상품 저장
        for (CartItem item : items) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());

            orderRepository.insertOrderItem(orderItem);
        }

        // 6️⃣ 장바구니 전체 비우기
//        cartService.clearByMember(memberId);

        return order.getId();
    }


    public Order getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public void updateStatus(Long orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }

    @Transactional
    public void completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }

        // ✅ 이미 결제 완료 → 멱등 처리 (그냥 성공)
        if ("PAID".equals(order.getStatus())) {
            return;
        }

        int updated = orderRepository.updateOrderToPaid(orderId);

        // 🔥 혹시 모를 동시성 대비 (다른 트랜잭션이 먼저 처리했을 경우)
        if (updated == 0) {

            Order retryOrder = orderRepository.findById(orderId);

            if ("PAID".equals(retryOrder.getStatus())) {
                return; // 이미 다른 요청에서 성공 처리됨
            }

            throw new IllegalStateException("주문 상태 변경 실패");
        }
        
//        List<OrderItem> items = orderRepository.findOrderItems(orderId);
//
//        
//        for (OrderItem item : items) {
//
//            // 2️⃣ 재고 감소 (DB에서 stock = stock - ?)
//            int result = productRepository.decreaseStock(
//                    item.getProductId(),
//                    item.getQuantity()
//            );
//
//            // 3️⃣ 재고 부족이면 rollback
//            if (result == 0) {
//                throw new IllegalStateException("재고 부족");
//            }
//        }
        
        Member loginUser = (Member) session.getAttribute("loginUser");
        cartService.clearByMember(loginUser.getId());
    }
    
//    @Transactional
//    public void completeOrder(Long orderId) {
//
//        Order order = orderRepository.findById(orderId);
//
//        if (order == null) {
//            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
//        }
//
//        // ✅ 이미 결제 완료면 멱등 처리
//        if ("PAID".equals(order.getStatus())) {
//            return;
//        }
//
//        // ✅ 1. 주문 상태 PAID로 변경
//        int updated = orderRepository.updateOrderToPaid(orderId);
//
//        if (updated == 0) {
//
//            Order retry = orderRepository.findById(orderId);
//
//            if ("PAID".equals(retry.getStatus())) {
//                return;
//            }
//
//            throw new IllegalStateException("주문 상태 변경 실패");
//        }
//
//        // ✅ 2. 주문 아이템 조회
//        List<OrderItem> items = orderRepository.findByOrderId(orderId);
//
//        // ✅ 3. 재고 차감 (ProductService 통해서)
//        for (OrderItem item : items) {
//
//            int result = productRepository.decreaseStock(
//                    item.getProductId(),
//                    item.getQuantity()
//            );
//
//            if (result == 0) {
//                throw new IllegalStateException("재고 부족");
//            }
//        }
//    }
    
    
    
    public List<Order> findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

	public int countByMemberId(Long id) {
		return orderRepository.countByMemberId(id);
	}

	public List<Order> findByMemberIdPaging(Long id, int offset, int size) {
		return orderRepository.findByMemberIdPaging(id,offset,size);
	}

	public List<OrderItem> getOrderItems(Long id) {
		return orderRepository.getOrderItems(id);
	}

    
    
}
