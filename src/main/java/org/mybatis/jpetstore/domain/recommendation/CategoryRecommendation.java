/*
 *    Copyright 2010-2025 the original author or authors.
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
package org.mybatis.jpetstore.domain.recommendation;

import java.io.Serializable;

public class CategoryRecommendation implements Serializable {

  private static final long serialVersionUID = 1L;

  private String categoryId;
  private String categoryName;
  private int confidence;
  private String reason;

  public CategoryRecommendation() {
  }

  public CategoryRecommendation(String categoryId, String categoryName, int confidence, String reason) {
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.confidence = confidence;
    this.reason = reason;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
