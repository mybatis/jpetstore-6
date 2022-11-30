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
package org.mybatis.jpetstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.jpetstore.domain.*;
import org.mybatis.jpetstore.mapper.CategoryMapper;
import org.mybatis.jpetstore.mapper.ItemMapper;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.mybatis.jpetstore.mapper.ReviewMapper;
import org.mybatis.jpetstore.mapper.ReviewRatingMapper;
import org.springframework.stereotype.Service;

/**
 * The Class CatalogService.
 *
 * @author Eduardo Macarron
 */
@Service
public class CatalogService {

  private final CategoryMapper categoryMapper;
  private final ItemMapper itemMapper;
  private final ProductMapper productMapper;
  private final ReviewMapper reviewMapper;
  private final ReviewRatingMapper reviewRatingMapper;

  public CatalogService(CategoryMapper categoryMapper, ItemMapper itemMapper, ProductMapper productMapper, ReviewMapper reviewMapper, ReviewRatingMapper reviewRatingMapper) {
    this.categoryMapper = categoryMapper;
    this.itemMapper = itemMapper;
    this.productMapper = productMapper;
    this.reviewMapper=reviewMapper;
    this.reviewRatingMapper=reviewRatingMapper;
  }

  public List<Category> getCategoryList() {
    return categoryMapper.getCategoryList();
  }

  public Category getCategory(String categoryId) {
    return categoryMapper.getCategory(categoryId);
  }

  public Product getProduct(String productId) {
    return productMapper.getProduct(productId);
  }

  public List<Product> getProductListByCategory(String categoryId) {
    return productMapper.getProductListByCategory(categoryId);
  }

  /**
   * Search product list.
   *
   * @param keywords
   *          the keywords
   *
   * @return the list
   */
  public List<Product> searchProductList(String keywords) {
    List<Product> products = new ArrayList<>();
    for (String keyword : keywords.split("\\s+")) {
      products.addAll(productMapper.searchProductList("%" + keyword.toLowerCase() + "%"));
    }
    return products;
  }

  public List<Item> getItemListByProduct(String productId) {
    return itemMapper.getItemListByProduct(productId);
  }

  public Item getItem(String itemId) {
    return itemMapper.getItem(itemId);
  }

  public boolean isItemInStock(String itemId) {
    return itemMapper.getInventoryQuantity(itemId) > 0;
  }

  public List<Review> getReviewList(String productId) {return reviewMapper.getReivewListByProductId(productId);  }

  public Map<String, Double> getRatingMapByCategory(String categoryId){
    Map<String, Double> result = new HashMap<>();
    List<Product> products = productMapper.getProductListByCategory(categoryId);
    for(Product product:products){
      String productId = product.getProductId();
      Double avg = getAverageRatingByProductId(productId);
      if (avg == null) continue;
      result.put(productId, Math.round(getAverageRatingByProductId(productId)*100)/100.0);
    }
    return result;
  }

  public Double getAverageRatingByProductId(String productId){
    List<Integer> ratings = new ArrayList<>();
    List<Review> reviews = reviewMapper.getReivewListByProductId(productId);
    if(reviews.isEmpty()) return null;
    for(Review review:reviews){
      String reviewId = review.getReviewId();
      reviewRatingMapper.getReviewRatingByReviewId(reviewId).forEach(reviewRating -> ratings.add(reviewRating.getRating()));
    }
    return ratings.stream().mapToDouble(num -> (double) num).sum() / ratings.size();
  }

}