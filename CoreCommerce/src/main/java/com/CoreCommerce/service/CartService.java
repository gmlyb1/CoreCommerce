package com.CoreCommerce.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Cart;
import com.CoreCommerce.domain.CartItem;
import com.CoreCommerce.repository.CartRepository;

import lombok.RequiredArgsConstructor;


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


    // 장바구니 아이템 삭제
    public void removeCartItem(Long id) {
        cartRepository.delete(id);
    }

	public void clearByMember(Long memberId) {
		cartRepository.clearByMember(memberId);
	}
	
	public Cart findCartByMemberId(Long memberId) {
		return cartRepository.findCartByMemberId(memberId);
	}
	
	public List<CartItem> findByCartIdPaging(Long id, int offset, int size) {
		return cartRepository.findByCartIdPaging(id,offset,size);
	}
 
	public int countByCartId(Long id) {
		return cartRepository.countByCartId(id);
	}

	public List<CartItem> getCartItems(Long id) {
		return cartRepository.getCartItems(id);
	}

	public void deleteByCartItemIds(List<Long> cartItemIds) {
		cartRepository.deleteByCartItemIds(cartItemIds);
	}
	
	public void deleteByIdsAndMemberId(List<Long> cartItemIds, Long id) {
		cartRepository.deleteByIdsAndMemberId(cartItemIds, id);
	}
}
