package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String name;

	    private int price;

	    private int stock;

	    @Column(length = 2000)
	    private String description;

	    private String imageUrl;

	    private String category;

	    private boolean sale;

	    private boolean newProduct;

	    private int originalPrice;

	    private LocalDateTime createdAt;
	    
	    @Column(name = "offer_yn")
	    private boolean offerYn = false;
	    
	    private int discountPercent;

}
