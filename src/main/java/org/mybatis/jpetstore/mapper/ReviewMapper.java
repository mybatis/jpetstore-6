package org.mybatis.jpetstore.mapper;

import java.util.List;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.Review;
import org.mybatis.jpetstore.domain.ReviewRating;

public interface ReviewMapper {
    List<Review> getReviews();
    Review getReviewById(String id);
    void deleteReviewById(String id);
    Item getItemById(String id);
    Product getProductById(String id);
    void insertReview(Review review);
    void insertReviewRating(ReviewRating rr);
}
