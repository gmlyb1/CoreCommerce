package com.CoreCommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Product;
import com.CoreCommerce.repository.ProductRepository;
import com.CoreCommerce.repository.ReviewRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
	private final ProductRepository productRepository;
	
	@Autowired
	private final ReviewRepository reviewRepository;
	  // application.properties에서 지정한 이미지 저장 경로
    @Value("${upload.path}")
    private String uploadPath;


    // 상품 목록 페이지
    @GetMapping("/list")
    public String listProducts(HttpSession session, Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String productDetail(HttpSession session,@PathVariable Long id, Model model) {

        Product product = productRepository.findById(id);

        if (product == null) {
            return "redirect:/product/list";
        }
        
        Member loginUser = (Member)  session.getAttribute("loginUser");
        
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("reviewList", reviewRepository.findByProductId(id));
        model.addAttribute("avgRating", reviewRepository.getAverageRating(id));
        model.addAttribute("product", product);
        return "product/detail";
    }
    
    // 상품 등록 페이지
    @GetMapping("/form")
    public String productForm(@RequestParam(required = false) Long id, Model model) {
    	Product product;
        if (id != null) {
            product = productRepository.findById(id); // MyBatis는 Product 또는 null 반환
            if (product == null) {
                product = new Product(); // id 존재하지만 없으면 새 객체
            }
        } else {
            product = new Product();
        }

        model.addAttribute("product", product);
        return "product/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
       
    	if(product.getId() != null) {
    		Product dbProduct = productRepository.findById(product.getId());
    		
    		if(imageFile == null || imageFile.isEmpty()) {
    			product.setImageUrl(dbProduct.getImageUrl());
    		}
    	}
    	
    	if (!imageFile.isEmpty()) {
            String uploadDir = new File("src/main/resources/static/images/products").getAbsolutePath();
            
            // 디렉토리가 없으면 생성
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            imageFile.transferTo(filePath.toFile());

            product.setImageUrl("/images/products/" + filename);
        }
        
        if(product.getId() != null) {
        	productRepository.update(product);
        } else {
        	productRepository.insert(product);
        }
        return "redirect:/product/list";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.delete(id); // MyBatis 매퍼 호출
        return ResponseEntity.ok().build();
    }
}