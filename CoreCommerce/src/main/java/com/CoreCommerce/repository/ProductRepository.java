package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CoreCommerce.domain.Product;

@Mapper
public interface ProductRepository{
	
	List<Product> findAll();

    Product findById(@Param("id") Long id);

    void insert(Product product);

    void update(Product product);

    void delete(@Param("id") Long id);

    List<Product> findMainPage();

	List<Product> findOfferProductList();
}
