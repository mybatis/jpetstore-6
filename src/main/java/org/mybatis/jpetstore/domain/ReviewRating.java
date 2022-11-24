package org.mybatis.jpetstore.domain;

import java.io.Serializable;
import java.sql.Date;

public class ReviewRating implements Serializable {
    private String reviewId;
    private String key;
    private int rating;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
