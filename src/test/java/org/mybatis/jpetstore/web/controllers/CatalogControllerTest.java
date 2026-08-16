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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

/**
 * The Class CatalogControllerTest.
 */
@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {

  /** The catalog service. */
  @Mock
  private CatalogService catalogService;

  /** The catalog controller. */
  @InjectMocks
  private CatalogController catalogController;

  /**
   * View main returns correct view.
   */
  @Test
  void viewMainReturnsCorrectView() {
    assertThat(catalogController.viewMain()).isEqualTo("catalog/Main");
  }

  /**
   * View category with valid id.
   */
  @Test
  void viewCategoryWithValidId() {
    Model model = new ExtendedModelMap();
    Category category = new Category();
    List<Product> products = List.of();
    when(catalogService.getCategory("FISH")).thenReturn(category);
    when(catalogService.getProductListByCategory("FISH")).thenReturn(products);

    String view = catalogController.viewCategory("FISH", model);

    assertThat(view).isEqualTo("catalog/Category");
    assertThat(model.asMap()).containsKey("category");
    assertThat(model.asMap()).containsKey("productList");
  }

  /**
   * Search products with null keyword.
   */
  @Test
  void searchProductsWithNullKeyword() {
    Model model = new ExtendedModelMap();
    String view = catalogController.searchProducts(null, model);
    assertThat(view).isEqualTo("common/Error");
    assertThat(model.asMap()).containsKey("message");
  }

  /**
   * Search products with valid keyword.
   */
  @Test
  void searchProductsWithValidKeyword() {
    Model model = new ExtendedModelMap();
    List<Product> products = List.of();
    when(catalogService.searchProductList("dog")).thenReturn(products);

    String view = catalogController.searchProducts("dog", model);

    assertThat(view).isEqualTo("catalog/SearchProducts");
    assertThat(model.asMap()).containsKey("productList");
  }

  /**
   * View product with null id does not populate model.
   */
  @Test
  void viewProductWithNullIdDoesNotPopulateModel() {
    Model model = new ExtendedModelMap();

    String view = catalogController.viewProduct(null, model);

    assertThat(view).isEqualTo("catalog/Product");
    assertThat(model.asMap()).doesNotContainKey("itemList");
    assertThat(model.asMap()).doesNotContainKey("product");
  }

  /**
   * View product with valid id populates model.
   */
  @Test
  void viewProductWithValidIdPopulatesModel() {
    Model model = new ExtendedModelMap();
    Product product = new Product();
    product.setProductId("FI-SW-01");
    List<Item> items = List.of();
    when(catalogService.getProduct("FI-SW-01")).thenReturn(product);
    when(catalogService.getItemListByProduct("FI-SW-01")).thenReturn(items);

    String view = catalogController.viewProduct("FI-SW-01", model);

    assertThat(view).isEqualTo("catalog/Product");
    assertThat(model.asMap().get("product")).isSameAs(product);
    assertThat(model.asMap().get("itemList")).isSameAs(items);
  }

  /**
   * View item populates model with item and product.
   */
  @Test
  void viewItemPopulatesModelWithItemAndProduct() {
    Model model = new ExtendedModelMap();
    Product product = new Product();
    product.setProductId("FI-SW-01");
    Item item = new Item();
    item.setItemId("EST-1");
    item.setProduct(product);
    when(catalogService.getItem("EST-1")).thenReturn(item);

    String view = catalogController.viewItem("EST-1", model);

    assertThat(view).isEqualTo("catalog/Item");
    assertThat(model.asMap().get("item")).isSameAs(item);
    assertThat(model.asMap().get("product")).isSameAs(product);
  }
}
