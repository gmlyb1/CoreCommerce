package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Product;

@Mapper
public interface WishlistRepository {

	int existsWishlist(@Param("memberId") Long memberId,
            @Param("productId") Long productId);

	void insertWishlist(@Param("memberId") Long memberId,
	             @Param("productId") Long productId);
	
	void deleteWishlist(@Param("memberId") Long memberId,
	             @Param("productId") Long productId);

	List<Product> getWishlist(Long id);

}
