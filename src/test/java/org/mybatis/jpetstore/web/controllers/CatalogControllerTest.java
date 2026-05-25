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
package org.mybatis.jpetstore.web.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

  @Mock
  private CatalogService catalogService;

  @InjectMocks
  private CatalogController catalogController;

  @Test
  void viewMainReturnsCorrectView() {
    assertThat(catalogController.viewMain()).isEqualTo("catalog/Main");
  }

  @Test
  void viewCategoryWithValidId() {
    Model model = new ExtendedModelMap();
    Category category = new Category();
    List<Product> products = Collections.emptyList();
    when(catalogService.getCategory("FISH")).thenReturn(category);
    when(catalogService.getProductListByCategory("FISH")).thenReturn(products);

    String view = catalogController.viewCategory("FISH", model);

    assertThat(view).isEqualTo("catalog/Category");
    assertThat(model.asMap()).containsKey("category");
    assertThat(model.asMap()).containsKey("productList");
  }

  @Test
  void searchProductsWithNullKeyword() {
    Model model = new ExtendedModelMap();
    String view = catalogController.searchProducts(null, model, null);
    assertThat(view).isEqualTo("common/Error");
    assertThat(model.asMap()).containsKey("message");
  }

  @Test
  void searchProductsWithValidKeyword() {
    Model model = new ExtendedModelMap();
    List<Product> products = Collections.emptyList();
    when(catalogService.searchProductList("dog")).thenReturn(products);

    String view = catalogController.searchProducts("dog", model, null);

    assertThat(view).isEqualTo("catalog/SearchProducts");
    assertThat(model.asMap()).containsKey("productList");
  }
}
