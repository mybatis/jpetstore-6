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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Iterator;

import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.CartItem;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

  private static final String ERROR_VIEW = "common/Error";

  @Autowired
  private CatalogService catalogService;

  private Cart getCart(HttpSession session) {
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
      cart = new Cart();
      session.setAttribute("cart", cart);
    }
    return cart;
  }

  @GetMapping({ "", "/" })
  public String viewCart(HttpSession session, Model model) {
    model.addAttribute("cart", getCart(session));
    return "cart/Cart";
  }

  @GetMapping("/addItem")
  public String addItemToCart(@RequestParam(value = "workingItemId", required = false) String workingItemId,
      HttpSession session, Model model) {
    if (workingItemId == null || workingItemId.trim().isEmpty()) {
      model.addAttribute("message", "Invalid item ID: cannot add item to cart.");
      return ERROR_VIEW;
    }
    Cart cart = getCart(session);
    if (cart.containsItemId(workingItemId)) {
      cart.incrementQuantityByItemId(workingItemId);
    } else {
      boolean isInStock = catalogService.isItemInStock(workingItemId);
      Item item = catalogService.getItem(workingItemId);
      cart.addItem(item, isInStock);
    }
    model.addAttribute("cart", cart);
    return "cart/Cart";
  }

  @GetMapping("/removeItem")
  public String removeItemFromCart(@RequestParam(value = "workingItemId", required = false) String workingItemId,
      HttpSession session, Model model) {
    if (workingItemId == null || workingItemId.trim().isEmpty()) {
      model.addAttribute("message", "Invalid item ID: cannot remove item from cart.");
      return ERROR_VIEW;
    }
    Cart cart = getCart(session);
    Item item = cart.removeItemById(workingItemId);
    if (item == null) {
      model.addAttribute("message", "Attempted to remove null CartItem from Cart.");
      return ERROR_VIEW;
    }
    model.addAttribute("cart", cart);
    return "cart/Cart";
  }

  @PostMapping("/update")
  public String updateCartQuantities(HttpServletRequest request, HttpSession session, Model model) {
    Cart cart = getCart(session);
    Iterator<CartItem> cartItems = cart.getAllCartItems();
    while (cartItems.hasNext()) {
      CartItem cartItem = cartItems.next();
      String itemId = cartItem.getItem().getItemId();
      try {
        int quantity = Integer.parseInt(request.getParameter(itemId));
        cart.setQuantityByItemId(itemId, quantity);
        if (quantity < 1) {
          cartItems.remove();
        }
      } catch (NumberFormatException e) {
        // ignore invalid numeric input on purpose
      }
    }
    model.addAttribute("cart", cart);
    return "cart/Cart";
  }

  @GetMapping("/checkout")
  public String checkOut(HttpSession session, Model model) {
    model.addAttribute("cart", getCart(session));
    return "cart/Checkout";
  }

}
