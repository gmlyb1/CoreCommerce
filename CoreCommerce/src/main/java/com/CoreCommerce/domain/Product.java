package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Product {

	private Long id;
	private String name;
	private int price;
	private int stock;
	private String description;
	private String imageUrl;

}
