package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.Review;
import org.mybatis.jpetstore.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;

    ReviewService(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public List<Review> getReviews() {
        return this.reviewMapper.getReviews();
    }
}
