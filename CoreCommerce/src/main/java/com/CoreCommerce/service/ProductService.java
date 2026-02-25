package com.CoreCommerce.service;

import java.util.List;

import javax.transaction.Transactional;

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
    
    public List<Product> findMainPage() {
    	return productRepository.findMainPage();
    }
    
    public List<Product> findOfferProductList() {
    	return productRepository.findOfferProductList();
    }
    
    @Transactional
    public void decreaseStock(Long productId, int quantity) {

        int result = productRepository.decreaseStock(productId, quantity);

        if (result == 0) {
            throw new IllegalStateException("재고 부족");
        }
    }
    
    @Transactional
    public void increaseStock(Long productId, int quantity) {

        productRepository.increaseStock(productId, quantity);
    }
}
