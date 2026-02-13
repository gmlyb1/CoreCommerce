package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.CoreCommerce.domain.Product;

@Mapper
public interface ProductRepository {
	
	List<Product> findAll();


}
