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
package org.mybatis.jpetstore.web.actions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * The Class CatalogActionBeanTest.
 */
class CatalogActionBeanTest {

  /**
   * Gets the item list output null.
   */
  @Test
  void getItemListOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getItemList()).isNull();

  }

  /**
   * Gets the product list output null.
   */
  @Test
  void getProductListOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getProductList()).isNull();

  }

  /**
   * Gets the category list output null.
   */
  @Test
  void getCategoryListOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getCategoryList()).isNull();

  }

  /**
   * Gets the item output null.
   */
  @Test
  void getItemOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getItem()).isNull();

  }

  /**
   * Gets the product output null.
   */
  @Test
  void getProductOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getProduct()).isNull();

  }

  /**
   * Gets the category output null.
   */
  @Test
  void getCategoryOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getCategory()).isNull();

  }

  /**
   * Gets the item id output null.
   */
  @Test
  void getItemIdOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getItemId()).isNull();

  }

  /**
   * Gets the product id output null.
   */
  @Test
  void getProductIdOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getProductId()).isNull();

  }

  /**
   * Gets the category id output null.
   */
  @Test
  void getCategoryIdOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getCategoryId()).isNull();

  }

  /**
   * Gets the keyword output null.
   */
  @Test
  void getKeywordOutputNull() {

    // Arrange
    final CatalogActionBean catalogActionBean = new CatalogActionBean();

    // Act and Assert result
    assertThat(catalogActionBean.getKeyword()).isNull();

  }

  /**
   * Constructor output not null.
   */
  @Test
  void constructorOutputNotNull() {

    // Act, creating object to test constructor
    final CatalogActionBean actual = new CatalogActionBean();

    // Assert result
    assertThat(actual).isNotNull();
    assertThat(actual.getContext()).isNull();

  }
}
