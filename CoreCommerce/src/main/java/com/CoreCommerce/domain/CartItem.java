package com.CoreCommerce.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CartItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

	private Long id;
    private Long cartId;    // FK
    private Long productId; // FK
    private int quantity;

    // 편의 필드 (조인 결과)
    private String productName;
    private int price;
    private String imageUrl;
}
