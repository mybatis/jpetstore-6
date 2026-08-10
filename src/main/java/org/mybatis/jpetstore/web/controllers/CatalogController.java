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

import java.util.List;

import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

  private static final String ERROR_VIEW = "common/Error";

  @Autowired
  private CatalogService catalogService;

  @GetMapping({ "", "/" })
  public String viewMain() {
    return "catalog/Main";
  }

  @GetMapping("/viewCategory")
  public String viewCategory(@RequestParam(value = "categoryId", required = false) String categoryId, Model model) {
    if (categoryId != null) {
      List<Product> productList = catalogService.getProductListByCategory(categoryId);
      Category category = catalogService.getCategory(categoryId);
      model.addAttribute("productList", productList);
      model.addAttribute("category", category);
    }
    return "catalog/Category";
  }

  @GetMapping("/viewProduct")
  public String viewProduct(@RequestParam(value = "productId", required = false) String productId, Model model) {
    if (productId != null) {
      List<Item> itemList = catalogService.getItemListByProduct(productId);
      Product product = catalogService.getProduct(productId);
      model.addAttribute("itemList", itemList);
      model.addAttribute("product", product);
    }
    return "catalog/Product";
  }

  @GetMapping("/viewItem")
  public String viewItem(@RequestParam("itemId") String itemId, Model model) {
    Item item = catalogService.getItem(itemId);
    Product product = item.getProduct();
    model.addAttribute("item", item);
    model.addAttribute("product", product);
    return "catalog/Item";
  }

  @GetMapping("/searchProducts")
  public String searchProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model,
      RedirectAttributes redirectAttributes) {
    if (keyword == null || keyword.length() < 1) {
      model.addAttribute("message", "Please enter a keyword to search for, then press the search button.");
      return ERROR_VIEW;
    }
    List<Product> productList = catalogService.searchProductList(keyword.toLowerCase());
    model.addAttribute("productList", productList);
    return "catalog/SearchProducts";
  }

}
