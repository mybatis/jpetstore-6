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

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;

/**
 * The Class CatalogActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class CatalogActionBean extends AbstractActionBean {

  /** The serial version uid. */
  private static final long serialVersionUID = 5849523372175050635L;

  /** The main. */
  private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
  /** The view category. */
  private static final String VIEW_CATEGORY = "/WEB-INF/jsp/catalog/Category.jsp";
  /** The view product. */
  private static final String VIEW_PRODUCT = "/WEB-INF/jsp/catalog/Product.jsp";
  /** The view item. */
  private static final String VIEW_ITEM = "/WEB-INF/jsp/catalog/Item.jsp";
  /** The search products. */
  private static final String SEARCH_PRODUCTS = "/WEB-INF/jsp/catalog/SearchProducts.jsp";

  /** The catalog service. */
  @SpringBean
  private transient CatalogService catalogService;

  /** The keyword. */
  private String keyword;

  /** The category id. */
  private String categoryId;
  /** The category. */
  private Category category;
  /** The category list. */
  private List<Category> categoryList;

  /** The product id. */
  private String productId;
  /** The product. */
  private Product product;
  /** The product list. */
  private List<Product> productList;

  /** The item id. */
  private String itemId;
  /** The item. */
  private Item item;
  /** The item list. */
  private List<Item> itemList;

  /**
   * Gets the keyword.
   *
   * @return the keyword
   */
  public String getKeyword() {
    return keyword;
  }

  /**
   * Sets the keyword.
   *
   * @param keyword
   *          the keyword
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

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
    this.categoryId = categoryId;
  }

  /**
   * Gets the product id.
   *
   * @return the product id
   */
  public String getProductId() {
    return productId;
  }

  /**
   * Sets the product id.
   *
   * @param productId
   *          the product id
   */
  public void setProductId(String productId) {
    this.productId = productId;
  }

  /**
   * Gets the item id.
   *
   * @return the item id
   */
  public String getItemId() {
    return itemId;
  }

  /**
   * Sets the item id.
   *
   * @param itemId
   *          the item id
   */
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  public Category getCategory() {
    return category;
  }

  /**
   * Sets the category.
   *
   * @param category
   *          the category
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Gets the product.
   *
   * @return the product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Sets the product.
   *
   * @param product
   *          the product
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Gets the item.
   *
   * @return the item
   */
  public Item getItem() {
    return item;
  }

  /**
   * Sets the item.
   *
   * @param item
   *          the item
   */
  public void setItem(Item item) {
    this.item = item;
  }

  /**
   * Gets the category list.
   *
   * @return the category list
   */
  public List<Category> getCategoryList() {
    return categoryList;
  }

  /**
   * Sets the category list.
   *
   * @param categoryList
   *          the category list
   */
  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  /**
   * Gets the product list.
   *
   * @return the product list
   */
  public List<Product> getProductList() {
    return productList;
  }

  /**
   * Sets the product list.
   *
   * @param productList
   *          the product list
   */
  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  /**
   * Gets the item list.
   *
   * @return the item list
   */
  public List<Item> getItemList() {
    return itemList;
  }

  /**
   * Sets the item list.
   *
   * @param itemList
   *          the item list
   */
  public void setItemList(List<Item> itemList) {
    this.itemList = itemList;
  }

  /**
   * View main.
   *
   * @return the forward resolution
   */
  @DefaultHandler
  public ForwardResolution viewMain() {
    return new ForwardResolution(MAIN);
  }

  /**
   * View category.
   *
   * @return the forward resolution
   */
  public ForwardResolution viewCategory() {
    if (categoryId != null) {
      productList = catalogService.getProductListByCategory(categoryId);
      category = catalogService.getCategory(categoryId);
    }
    return new ForwardResolution(VIEW_CATEGORY);
  }

  /**
   * View product.
   *
   * @return the forward resolution
   */
  public ForwardResolution viewProduct() {
    if (productId != null) {
      itemList = catalogService.getItemListByProduct(productId);
      product = catalogService.getProduct(productId);
    }
    return new ForwardResolution(VIEW_PRODUCT);
  }

  /**
   * View item.
   *
   * @return the forward resolution
   */
  public ForwardResolution viewItem() {
    item = catalogService.getItem(itemId);
    product = item.getProduct();
    return new ForwardResolution(VIEW_ITEM);
  }

  /**
   * Search products.
   *
   * @return the forward resolution
   */
  public ForwardResolution searchProducts() {
    if (keyword == null || keyword.length() < 1) {
      setMessage("Please enter a keyword to search for, then press the search button.");
      return new ForwardResolution(ERROR);
    } else {
      productList = catalogService.searchProductList(keyword.toLowerCase());
      return new ForwardResolution(SEARCH_PRODUCTS);
    }
  }

  /**
   * Clear.
   */
  public void clear() {
    keyword = null;

    categoryId = null;
    category = null;
    categoryList = null;

    productId = null;
    product = null;
    productList = null;

    itemId = null;
    item = null;
    itemList = null;
  }

}
