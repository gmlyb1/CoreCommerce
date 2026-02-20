package com.CoreCommerce.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.CartItem;
import com.CoreCommerce.repository.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 장바구니에 상품 추가
    public void addToCart(CartItem item) {
        CartItem existing = cartRepository.findByCartIdAndProductId(item.getCartId(), item.getProductId());

        if (existing != null) {
            // 이미 존재하면 수량만 증가
            int newQuantity = existing.getQuantity() + item.getQuantity();
            cartRepository.updateQuantity(existing.getId(), newQuantity);
        } else {
            // 없으면 새로 추가
            cartRepository.add(item.getCartId(), item.getProductId(), item.getQuantity());
        }
    }

    // 특정 장바구니 아이템 조회
    public List<CartItem> getCartItems(Long cartId) {
        return cartRepository.findByCartId(cartId);
    }

    // 장바구니 아이템 삭제
    public void removeCartItem(Long id) {
        cartRepository.delete(id);
    }
}
