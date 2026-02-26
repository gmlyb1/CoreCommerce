package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.CoreCommerce.domain.Cart;
import com.CoreCommerce.domain.CartItem;

@Mapper
public interface CartRepository {

	 // ===== CartItem =====
    void add(@Param("cartId") Long cartId,
             @Param("productId") Long productId,
             @Param("quantity") int quantity);

    List<CartItem> findByCartId(@Param("cartId") Long cartId);

    void delete(@Param("id") Long id);

    CartItem findByCartIdAndProductId(@Param("cartId") Long cartId,
                                      @Param("productId") Long productId);

    void updateQuantity(@Param("itemId") Long itemId,
                        @Param("quantity") int quantity);

    // ===== Cart =====
    Cart findCartByUserId(@Param("userId") Long userId);

    void createCart(Cart cart); // insert 후 id 세팅

	void clearByMember(Long memberId);

	Cart findCartByMemberId(Long memberId);

	List<CartItem> findByCartIdPaging(
		    @Param("cartId") Long cartId,
		    @Param("offset") int offset,
		    @Param("size") int size
		);

	int countByCartId(Long id);

	List<CartItem> getCartItems(Long id);

	List<CartItem> findByIdsAndMemberId(@Param("list") List<Long> list,  @Param("memberId") Long memberId);

	void deleteByCartItemIds( @Param("cartItemIds") List<Long> cartItemIds);

	void deleteByIdsAndMemberId( @Param("ids") List<Long> ids,  @Param("memberId") Long memberId );

    
}
