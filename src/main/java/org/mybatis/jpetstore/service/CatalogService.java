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
package org.mybatis.jpetstore.service;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.mapper.CategoryMapper;
import org.mybatis.jpetstore.mapper.ItemMapper;
import org.mybatis.jpetstore.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
 * The Class CatalogService.
 *
 * @author Eduardo Macarron
 */
@Service
public class CatalogService {

  /** The category mapper. */
  private final CategoryMapper categoryMapper;
  /** The item mapper. */
  private final ItemMapper itemMapper;
  /** The product mapper. */
  private final ProductMapper productMapper;

  /**
   * Instantiates a new catalog service.
   *
   * @param categoryMapper
   *          the category mapper
   * @param itemMapper
   *          the item mapper
   * @param productMapper
   *          the product mapper
   */
  public CatalogService(CategoryMapper categoryMapper, ItemMapper itemMapper, ProductMapper productMapper) {
    this.categoryMapper = categoryMapper;
    this.itemMapper = itemMapper;
    this.productMapper = productMapper;
  }

  /**
   * Gets the category list.
   *
   * @return the category list
   */
  public List<Category> getCategoryList() {
    return categoryMapper.getCategoryList();
  }

  /**
   * Get category.
   *
   * @param categoryId
   *          the category id
   *
   * @return the category
   */
  public Category getCategory(String categoryId) {
    return categoryMapper.getCategory(categoryId);
  }

  /**
   * Get product.
   *
   * @param productId
   *          the product id
   *
   * @return the product
   */
  public Product getProduct(String productId) {
    return productMapper.getProduct(productId);
  }

  /**
   * Get product list by category.
   *
   * @param categoryId
   *          the category id
   *
   * @return the list
   */
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

  /**
   * Get item list by product.
   *
   * @param productId
   *          the product id
   *
   * @return the list
   */
  public List<Item> getItemListByProduct(String productId) {
    return itemMapper.getItemListByProduct(productId);
  }

  /**
   * Get item.
   *
   * @param itemId
   *          the item id
   *
   * @return the item
   */
  public Item getItem(String itemId) {
    return itemMapper.getItem(itemId);
  }

  /**
   * Is item in stock.
   *
   * @param itemId
   *          the item id
   *
   * @return true, if successful
   */
  public boolean isItemInStock(String itemId) {
    return itemMapper.getInventoryQuantity(itemId) > 0;
  }
}
