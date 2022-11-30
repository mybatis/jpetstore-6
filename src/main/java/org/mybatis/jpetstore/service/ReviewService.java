package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.Review;
import org.mybatis.jpetstore.domain.ReviewRating;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.mybatis.jpetstore.mapper.ReviewMapper;
import org.mybatis.jpetstore.mapper.ReviewRatingMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRatingMapper reviewRatingMapper;
    private final ProductMapper productMapper;

    ReviewService(ReviewMapper reviewMapper, ReviewRatingMapper reviewRatingMapper, ProductMapper productMapper) {
        this.reviewMapper = reviewMapper;
        this.reviewRatingMapper = reviewRatingMapper;
        this.productMapper = productMapper;
    }

    public List<Review> getReviews() {
        return this.reviewMapper.getReviews();
    }

    public Review getReviewById(String id) {
        return this.reviewMapper.getReviewById(id);
    }

    public List<ReviewRating> getReviewRatingById(String id) {
        return this.reviewRatingMapper.getReviewRatingByReviewId(id);
    }

    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }


    public void deleteReview(String id) {
        reviewRatingMapper.deleteReviewRatingByReviewId(id);
        reviewMapper.deleteReviewById(id);
    }
}
