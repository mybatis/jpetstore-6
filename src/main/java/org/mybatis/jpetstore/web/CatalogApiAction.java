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
package org.mybatis.jpetstore.web;

import java.util.List;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoints for catalog data (categories, products, items).
 * Provides JSON responses for mobile apps and external integrations.
 *
 * @author Generated
 */
@RestController
@RequestMapping("/api/catalog")
public class CatalogApiAction {

  private final CatalogService catalogService;

  public CatalogApiAction(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  /**
   * Get all categories.
   *
   * @return list of all categories as JSON
   */
  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getCategories() {
    List<Category> categories = catalogService.getCategoryList();
    return ResponseEntity.ok(categories);
  }

  /**
   * Get a specific category by ID.
   *
   * @param categoryId the category ID
   * @return category details as JSON, or 404 if not found
   */
  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<Category> getCategory(@PathVariable String categoryId) {
    Category category = catalogService.getCategory(categoryId);
    if (category == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(category);
  }

  /**
   * Get products by category.
   *
   * @param categoryId the category ID
   * @return list of products in that category as JSON
   */
  @GetMapping("/categories/{categoryId}/products")
  public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String categoryId) {
    List<Product> products = catalogService.getProductListByCategory(categoryId);
    return ResponseEntity.ok(products);
  }

  /**
   * Get a specific product by ID.
   *
   * @param productId the product ID
   * @return product details as JSON, or 404 if not found
   */
  @GetMapping("/products/{productId}")
  public ResponseEntity<Product> getProduct(@PathVariable String productId) {
    Product product = catalogService.getProduct(productId);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(product);
  }

  /**
   * Search products by keyword.
   *
   * @param keywords search keywords (space-separated)
   * @return list of matching products as JSON
   */
  @GetMapping("/products/search")
  public ResponseEntity<List<Product>> searchProducts(@RequestParam String keywords) {
    if (keywords == null || keywords.trim().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    List<Product> products = catalogService.searchProductList(keywords);
    return ResponseEntity.ok(products);
  }

  /**
   * Get items by product.
   *
   * @param productId the product ID
   * @return list of items for that product as JSON
   */
  @GetMapping("/products/{productId}/items")
  public ResponseEntity<List<Item>> getItemsByProduct(@PathVariable String productId) {
    List<Item> items = catalogService.getItemListByProduct(productId);
    return ResponseEntity.ok(items);
  }

  /**
   * Get a specific item by ID.
   *
   * @param itemId the item ID
   * @return item details as JSON, or 404 if not found
   */
  @GetMapping("/items/{itemId}")
  public ResponseEntity<Item> getItem(@PathVariable String itemId) {
    Item item = catalogService.getItem(itemId);
    if (item == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(item);
  }

  /**
   * Check if an item is in stock.
   *
   * @param itemId the item ID
   * @return JSON object with stock status
   */
  @GetMapping("/items/{itemId}/stock")
  public ResponseEntity<StockStatus> checkStock(@PathVariable String itemId) {
    boolean inStock = catalogService.isItemInStock(itemId);
    return ResponseEntity.ok(new StockStatus(itemId, inStock));
  }

  /**
   * Helper class for stock status response.
   */
  public static class StockStatus {
    public String itemId;
    public boolean inStock;

    public StockStatus(String itemId, boolean inStock) {
      this.itemId = itemId;
      this.inStock = inStock;
    }

    public String getItemId() {
      return itemId;
    }

    public boolean isInStock() {
      return inStock;
    }
  }
}
