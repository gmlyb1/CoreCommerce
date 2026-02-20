package com.CoreCommerce.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.CoreCommerce.domain.Order;
import com.CoreCommerce.domain.Payment;
import com.CoreCommerce.repository.PaymentRepository;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final CartService cartService;

    @Value("${toss.secret-key}")
    private String secretKey;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderService orderService,
                          CartService cartService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @Transactional
    public void confirmPayment(String paymentKey,
                               Long orderId,
                               int amount) {

        // 1️⃣ 주문 조회
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            throw new RuntimeException("주문 없음");
        }

        // 2️⃣ 금액 위변조 방지
        if (order.getTotalPrice() != amount) {
            throw new RuntimeException("금액 불일치");
        }

        // 3️⃣ 토스 승인 API 호출
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId.toString());
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://api.tosspayments.com/v1/payments/confirm",
                        request,
                        String.class
                );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("결제 승인 실패");
        }

        // 4️⃣ 주문 상태 변경
        orderService.updateStatus(orderId, "PAID");

        // 5️⃣ payment 저장
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPaymentKey(paymentKey);
        payment.setMethod("CARD");
        payment.setAmount(amount);
        payment.setStatus("DONE");
        payment.setApprovedAt(LocalDateTime.now());

        paymentRepository.insert(payment);

        // 6️⃣ 장바구니 비우기
        cartService.clearByMember(order.getMemberId());
    }
}
