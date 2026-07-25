/*
 *    Copyright 2010-2026 the original author or authors.
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
package org.mybatis.jpetstore.domain;

import java.io.Serializable;

/**
 * The Class Category.
 *
 * @author Eduardo Macarron
 */
public class Category implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = 3992469837058393712L;

  /** The category id. */
  private String categoryId;
  /** The name. */
  private String name;
  /** The description. */
  private String description;

  /**
   * Gets the category id.
   *
   * @return the category id
   */
  public String getCategoryId() {
    return categoryId;
  }

  /**
   * Sets the category id.
   *
   * @param categoryId
   *          the category id
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId.trim();
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return getCategoryId();
  }

}
