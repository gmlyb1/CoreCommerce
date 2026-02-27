package com.CoreCommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Review;
import com.CoreCommerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	
	public void insertReview(Review review) {
		reviewRepository.insertReview(review);
	}
	
	public List<Review> findByProductId(Long productId){
		return reviewRepository.findByProductId(productId);
	}
	
	public Double getAverageRating(Long productId){
		return reviewRepository.getAverageRating(productId);
	}
	
	public void updateReview(Review review){
		reviewRepository.updateReview(review);
	}
	
	public void deleteReview(Long id,String memberId){
		reviewRepository.deleteReview(id,memberId);
	}
}
