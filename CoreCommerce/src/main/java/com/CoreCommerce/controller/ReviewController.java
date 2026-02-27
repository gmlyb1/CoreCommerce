package com.CoreCommerce.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.domain.Review;
import com.CoreCommerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	@Autowired
	private ReviewRepository reviewRepository;
	
	// 🔥 리뷰 등록
    @PostMapping("/add")
    public String addReview(@ModelAttribute Review review,
                            HttpSession session) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        review.setMemberId(loginUser.getEmail());

        reviewRepository.insertReview(review);

        return "redirect:/product/" + review.getProductId();
    }

    // ✅ 리뷰 수정
    @PostMapping("/update")
    public String updateReview(@ModelAttribute Review review,
                               HttpSession session) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        review.setMemberId(loginUser.getEmail());

        reviewRepository.updateReview(review);

        return "redirect:/product/" + review.getProductId();
    }
    
    // 🔥 리뷰 삭제
    @PostMapping("/delete")
    public String deleteReview(@RequestParam Long id,
                               @RequestParam Long productId,
                               HttpSession session) {

        Member loginUser =
                (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        // 🔥 member email 같이 넘겨라
        reviewRepository.deleteReview(id, loginUser.getEmail());

        return "redirect:/product/" + productId;
    }
	
	
}
