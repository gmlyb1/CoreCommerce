package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	
	private Long id;
    private Long memberId;
    private int totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String courier;
    private String trackingNumber;
    private String email;
}
