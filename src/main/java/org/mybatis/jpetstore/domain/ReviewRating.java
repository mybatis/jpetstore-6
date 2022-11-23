package org.mybatis.jpetstore.domain;

import java.io.Serializable;
import java.sql.Date;

public class ReviewRating implements Serializable {
    private String reviewId;
    private String key;
    private int rating;
}
