package org.mybatis.jpetstore.mapper;

import org.mybatis.jpetstore.domain.ReviewRating;

import java.util.List;

public interface ReviewRatingMapper {
    List<ReviewRating> getReviewRatingByReviewId(String reviewId);
}
