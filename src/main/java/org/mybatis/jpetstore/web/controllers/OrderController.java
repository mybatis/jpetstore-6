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

import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {

  private static final String NEW_ORDER_VIEW = "order/NewOrderForm";
  private static final String SHIPPING_VIEW = "order/ShippingForm";
  private static final String CONFIRM_VIEW = "order/ConfirmOrder";
  private static final String VIEW_ORDER_VIEW = "order/ViewOrder";
  private static final String LIST_ORDERS_VIEW = "order/ListOrders";
  private static final String ERROR_VIEW = "common/Error";

  private static final List<String> CARD_TYPE_LIST = Collections
      .unmodifiableList(Arrays.asList("Visa", "MasterCard", "American Express"));

  @Autowired
  private OrderService orderService;

  @GetMapping("/list")
  public String listOrders(HttpSession session, Model model) {
    AccountController.AccountSession accountSession = (AccountController.AccountSession) session
        .getAttribute("accountBean");
    if (accountSession == null || !accountSession.isAuthenticated()) {
      model.addAttribute("message",
          "You must sign on before attempting to check out. Please sign on and try checking out again.");
      return ERROR_VIEW;
    }
    List<Order> orderList = orderService.getOrdersByUsername(accountSession.getAccount().getUsername());
    model.addAttribute("orderList", orderList);
    return LIST_ORDERS_VIEW;
  }

  @GetMapping("/new")
  public String newOrderForm(HttpSession session, Model model) {
    AccountController.AccountSession accountSession = (AccountController.AccountSession) session
        .getAttribute("accountBean");
    Cart cart = (Cart) session.getAttribute("cart");

    if (accountSession == null || !accountSession.isAuthenticated()) {
      model.addAttribute("message",
          "You must sign on before attempting to check out. Please sign on and try checking out again.");
      return "redirect:/account";
    }
    if (cart != null) {
      Order order = new Order();
      order.initOrder(accountSession.getAccount(), cart);
      session.setAttribute("order", order);
      model.addAttribute("order", order);
      model.addAttribute("creditCardTypes", CARD_TYPE_LIST);
      return NEW_ORDER_VIEW;
    }
    model.addAttribute("message", "An order could not be created because a cart could not be found.");
    return ERROR_VIEW;
  }

  @PostMapping("/new")
  public String newOrder(@ModelAttribute Order order,
      @RequestParam(value = "shippingAddressRequired", defaultValue = "false") boolean shippingAddressRequired,
      @RequestParam(value = "confirmed", defaultValue = "false") boolean confirmed, HttpSession session, Model model) {

    Order sessionOrder = (Order) session.getAttribute("order");

    // When confirming, use the session order directly - the confirm form only
    // submits confirmed=true with no other fields, so @ModelAttribute Order is empty
    if (confirmed) {
      if (sessionOrder != null) {
        orderService.insertOrder(sessionOrder);
        session.setAttribute("cart", new Cart());
        model.addAttribute("message", "Thank you, your order has been submitted.");
        model.addAttribute("order", sessionOrder);
        return VIEW_ORDER_VIEW;
      } else {
        model.addAttribute("message", "An error occurred processing your order (order was null).");
        return ERROR_VIEW;
      }
    }

    if (sessionOrder != null) {
      sessionOrder.setCardType(order.getCardType());
      sessionOrder.setCreditCard(order.getCreditCard());
      sessionOrder.setExpiryDate(order.getExpiryDate());
      sessionOrder.setBillToFirstName(order.getBillToFirstName());
      sessionOrder.setBillToLastName(order.getBillToLastName());
      sessionOrder.setBillAddress1(order.getBillAddress1());
      sessionOrder.setBillAddress2(order.getBillAddress2());
      sessionOrder.setBillCity(order.getBillCity());
      sessionOrder.setBillState(order.getBillState());
      sessionOrder.setBillZip(order.getBillZip());
      sessionOrder.setBillCountry(order.getBillCountry());
      if (order.getShipToFirstName() != null) {
        sessionOrder.setShipToFirstName(order.getShipToFirstName());
        sessionOrder.setShipToLastName(order.getShipToLastName());
        sessionOrder.setShipAddress1(order.getShipAddress1());
        sessionOrder.setShipAddress2(order.getShipAddress2());
        sessionOrder.setShipCity(order.getShipCity());
        sessionOrder.setShipState(order.getShipState());
        sessionOrder.setShipZip(order.getShipZip());
        sessionOrder.setShipCountry(order.getShipCountry());
      }
      order = sessionOrder;
    }

    if (shippingAddressRequired) {
      session.setAttribute("order", order);
      model.addAttribute("order", order);
      return SHIPPING_VIEW;
    } else {
      session.setAttribute("order", order);
      model.addAttribute("order", order);
      return CONFIRM_VIEW;
    }
  }

  @GetMapping("/view")
  public String viewOrder(@RequestParam("orderId") int orderId, HttpSession session, Model model) {
    AccountController.AccountSession accountSession = (AccountController.AccountSession) session
        .getAttribute("accountBean");
    if (accountSession == null || !accountSession.isAuthenticated()) {
      return "redirect:/account";
    }
    Order order = orderService.getOrder(orderId);
    if (accountSession.getAccount().getUsername().equals(order.getUsername())) {
      model.addAttribute("order", order);
      return VIEW_ORDER_VIEW;
    } else {
      model.addAttribute("message", "You may only view your own orders.");
      return ERROR_VIEW;
    }
  }

}
