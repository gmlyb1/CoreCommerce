package com.CoreCommerce.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.WishlistRepository;

@Service
@Transactional
public class WishlistService {

	private final WishlistRepository wishlistRepository;
	
	public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }
	
	public boolean toggleWishlist(Long memberId, Long productId) {

	    int exists = wishlistRepository.existsWishlist(memberId, productId);

	    System.out.println("exists:"+exists);
	    
	    if (exists > 0) {
	        wishlistRepository.deleteWishlist(memberId, productId);
	        return false; // 찜 취소
	    } else {
	        wishlistRepository.insertWishlist(memberId, productId);
	        return true; // 찜 추가
	    }
	}

	public List<Product> getWishlist(Long id) {
		return wishlistRepository.getWishlist(id);
	}
}
