/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.Review;
import org.mybatis.jpetstore.domain.ReviewRating;
import org.mybatis.jpetstore.service.ReviewService;

import javax.servlet.http.HttpSession;
import java.util.List;

@SessionScope
public class ReviewActionBean extends AbstractActionBean {

  private static final long serialVersionUID = -6121288227123176272L;

  private static final String VIEW_ORDER = "/WEB-INF/jsp/review/ViewReview.jsp";
  private static final String VIEW_WRITE_REVIEW = "/WEB-INF/jsp/review/WriteReview.jsp";

  private String reviewId;
  private Review review;
  private List<ReviewRating> ratingList;
  private Product product;
  private boolean isReviewOwner = false;
  private String itemId;
  private String userId;

  @SpringBean
  private transient ReviewService reviewService;

  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  public String getReviewId() {
    return this.reviewId;
  }

  public Review getReview() {
    return this.review;
  }

  public List<ReviewRating> getRatingList() { return this.ratingList; }

  public Product getProduct() {
    return this.product;
  }

  public boolean getIsReviewOwner() {
    return this.isReviewOwner;
  }
  public String getItemId() {
    return this.itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  public String getUserId() {
    return userId;
  }

  public void setRatingList(List<ReviewRating> ratingList) {
    this.ratingList = ratingList;
  }

  public void setReview(Review review) {
    this.review = review;
  }

  public Resolution viewReview() {
    HttpSession session = context.getRequest().getSession();
    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

    review = reviewService.getReviewById(reviewId);
    if (review != null) {
      product = reviewService.getProduct(review.getProductId());
      ratingList = reviewService.getReviewRatingById(review.getReviewId());

      if (accountBean != null) {
        isReviewOwner = accountBean.getAccount().getUsername().equals(review.getUserId());
      }
      else {
        isReviewOwner = false;
      }
    }

    return new ForwardResolution(VIEW_ORDER);
  }

  public Resolution deleteReview() {
    HttpSession session = context.getRequest().getSession();
    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

    review = reviewService.getReviewById(reviewId);
    if (accountBean != null) {
      isReviewOwner = accountBean.getAccount().getUsername().equals(review.getUserId());
    }
    else {
      isReviewOwner = false;
    }

    if (isReviewOwner && reviewId != null) {
      reviewService.deleteReview(reviewId);
    }

    return new RedirectResolution(CatalogActionBean.class, "viewMain");
  }

  public Resolution viewWriteReview() {
    HttpSession session = context.getRequest().getSession();
    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");

    if(itemId != null){
      product = reviewService.getProductById(itemId);
      userId = accountBean.getAccount().getUsername();
    }
    return new ForwardResolution(VIEW_WRITE_REVIEW);
  }
  public Resolution newReview() {
    HttpSession session = context.getRequest().getSession();

    review.setProductId(product.getProductId());
    review.setUserId(userId);
    review = reviewService.insertReview(review);

    return new ForwardResolution(VIEW_ORDER);
  }
}
