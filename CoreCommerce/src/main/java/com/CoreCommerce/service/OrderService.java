package com.CoreCommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    
    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartService cartService,
                        ProductRepository productRepository,
                        SimpMessagingTemplate simpMessagingTemplate) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
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

    public void updateStatus(Long orderId, String status, String courier, String trackingNumber) {
        orderRepository.updateStatus(orderId, status, courier, trackingNumber);
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
        
        List<OrderItem> items = orderRepository.findOrderItems(orderId);

        List<Long> cartItemIds = items.stream()
        						.map(OrderItem::getCartItemId)
        						.collect(Collectors.toList());
        
        for (OrderItem item : items) {
        	
            // 2️⃣ 재고 감소 (DB에서 stock = stock - ?)
            int result = productRepository.decreaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
            
            // 3️⃣ 재고 부족이면 rollback
            if (result == 0) {
                throw new IllegalStateException("재고가 부족 합니다. 판매자에게 문의해 주시기 바랍니다.");
            }
            
        }
        
          cartService.deleteByCartItemIds(cartItemIds);
    }
    
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

	@Transactional
	public Long createOrderBySelectedItems(Long memberId, List<Long> cartItemIds) {

	    // 🔥 로그인 사용자 것만 조회 (보안)
	    List<CartItem> items =
	            cartRepository.findByIdsAndMemberId(cartItemIds, memberId);

	    if (items.isEmpty()) {
	        throw new IllegalStateException("선택한 상품이 없습니다.");
	    }

	    int totalPrice = 0;

	    for (CartItem item : items) {
	        totalPrice += item.getPrice() * item.getQuantity();
	    }

	    Order order = new Order();
	    order.setMemberId(memberId);
	    order.setStatus("READY");
	    order.setTotalPrice(totalPrice);

	    orderRepository.insert(order);

	    for (CartItem item : items) {
	        OrderItem orderItem = new OrderItem();
	        orderItem.setOrderId(order.getId());
	        orderItem.setProductId(item.getProductId());
	        orderItem.setQuantity(item.getQuantity());
	        orderItem.setPrice(item.getPrice());
	        orderItem.setCartItemId(item.getId());
	        
	        orderRepository.insertOrderItem(orderItem);
	    }

	    return order.getId();
	}

	@Transactional
	public void cancelOrder(Long orderId, Long memberId) {

	    Order order = orderRepository.findById(orderId);

	    if (order == null) {
	        throw new RuntimeException("주문이 없습니다.");
	    }

	    if (!order.getMemberId().equals(memberId)) {
	        throw new RuntimeException("권한 없습니다.");
	    }

	    // 🔥 취소 불가능 상태 차단
	    if ("CANCELLED".equals(order.getStatus())) {
	        throw new RuntimeException("이미 취소된 주문 입니다.");
	    }

	    if ("SHIPPED".equals(order.getStatus())) {
	        throw new RuntimeException("이미 배송된 주문은 취소 불가합니다.");
	    }

	    // ✅ 상태 변경
	    orderRepository.updateStatus(orderId, "CANCELLED",null,null);
	    
	    
	    // ✅ 주문 아이템 조회
	    List<OrderItem> items = orderRepository.findItems(orderId);

	    // ✅ 재고 복구
	    for (OrderItem item : items) {

	        productRepository.increaseStock(
	                item.getProductId(),
	                item.getQuantity()
	        );
	    }
	}
	
	public List<OrderItem> findItems(Long orderId) {
		return orderRepository.findItems(orderId);
	}

	@Transactional
	public Long createSingleOrder(Long memberId, Long productId, int quantity){

	    Product product = productRepository.findById(productId);

	    if(product.getStock() < quantity){
	        throw new RuntimeException("재고가 부족합니다. 판매자에게 문의하세요.");
	    }

	    int totalPrice = product.getPrice() * quantity;

	    Order order = new Order();
	    order.setMemberId(memberId);
	    order.setStatus("READY");
	    order.setTotalPrice(totalPrice);

	    orderRepository.insert(order);

	    OrderItem item = new OrderItem();
	    item.setOrderId(order.getId());
	    item.setProductId(productId);
	    item.setPrice(product.getPrice());
	    item.setQuantity(quantity);

	    orderRepository.insertOrderItem(item);

	    return order.getId();
	}
	
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	
	public List<Order> findPaging(@Param("size") int size,
							      @Param("offset") int offset){
		return orderRepository.findPaging(offset, size);
	}

	public int countAll() {
		return orderRepository.countAll();
	}

	public List<OrderItem> findByOrderId(Long orderId) {
		return orderRepository.findByOrderId(orderId);
	}

	public void updateDiscount(Long orderId, Object object, int i, int totalPrice) {
		orderRepository.updateDiscount(orderId, orderId, i, totalPrice);
	}

	public void updateFinalPrice(Long realOrderId, int finalPrice) {
		orderRepository.updateFinalPrice(realOrderId, finalPrice);
	}
	
    
}
