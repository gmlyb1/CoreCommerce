package com.CoreCommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.CoreCommerce.domain.Review;

@Mapper
public interface ReviewRepository {

	void insertReview(Review review);

    List<Review> findByProductId(Long productId);

    Double getAverageRating(Long productId);

    void updateReview(Review review);

    void deleteReview(Long id, String memberId);

}
