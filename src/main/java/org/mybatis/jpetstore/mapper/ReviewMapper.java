package org.mybatis.jpetstore.mapper;

import java.util.List;
import org.mybatis.jpetstore.domain.Review;

public interface ReviewMapper {
    List<Review> getReviews();
    Review getReviewById(String id);
}
