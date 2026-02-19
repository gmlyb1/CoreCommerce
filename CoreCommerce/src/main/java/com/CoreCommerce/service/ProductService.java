package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id);
    }

    public void createProduct(Product product) {
    	productRepository.insert(product);
    }

    public void updateProduct(Product product) {
    	productRepository.update(product);
    }

    public void deleteProduct(Long id) {
    	productRepository.delete(id);
    }
}
