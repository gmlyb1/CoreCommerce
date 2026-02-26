package com.CoreCommerce.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OrderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

	private Long id;
    private Long orderId;    // FK는 그냥 Long
    private Long productId;
    private int price;
    private int quantity;
    private Long cartItemId;
}
