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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.service.OrderService;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  @Test
  void listOrdersWithoutAuthReturnsError() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    when(session.getAttribute("accountBean")).thenReturn(null);

    String view = orderController.listOrders(session, model);

    assertThat(view).isEqualTo("common/Error");
    assertThat(model.asMap()).containsKey("message");
  }

  @Test
  void newOrderFormWithoutAuthRedirects() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    when(session.getAttribute("accountBean")).thenReturn(null);
    when(session.getAttribute("cart")).thenReturn(new Cart());

    String view = orderController.newOrderForm(session, model);

    assertThat(view).isEqualTo("redirect:/account");
  }

  @Test
  void listOrdersWithAuthReturnsOrderList() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    org.mybatis.jpetstore.domain.Account account = new org.mybatis.jpetstore.domain.Account();
    account.setUsername("j2ee");
    AccountController.AccountSession accountSession = new AccountController.AccountSession(account,
        Collections.emptyList(), true);
    when(session.getAttribute("accountBean")).thenReturn(accountSession);
    when(orderService.getOrdersByUsername("j2ee")).thenReturn(Collections.emptyList());

    String view = orderController.listOrders(session, model);

    assertThat(view).isEqualTo("order/ListOrders");
    assertThat(model.asMap()).containsKey("orderList");
  }

  @Test
  void confirmOrderUsesBillingAddressFromSession() {
    // When the ConfirmOrder form is submitted it only posts confirmed=true.
    // The @ModelAttribute Order is therefore all-null. The controller must use
    // the session order (which has the full billing address) and must NOT
    // overwrite it with the null-filled model attribute.
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();

    org.mybatis.jpetstore.domain.Order sessionOrder = new org.mybatis.jpetstore.domain.Order();
    sessionOrder.setBillAddress1("901 San Antonio Road");
    sessionOrder.setBillCity("Palo Alto");
    sessionOrder.setBillState("CA");
    sessionOrder.setBillZip("94303");
    sessionOrder.setBillCountry("USA");
    sessionOrder.setBillToFirstName("ABC");
    sessionOrder.setBillToLastName("Banner");
    sessionOrder.setCreditCard("999 9999 9999 9999");
    sessionOrder.setExpiryDate("12/03");
    sessionOrder.setCardType("Visa");

    when(session.getAttribute("order")).thenReturn(sessionOrder);

    // Simulate ConfirmOrder POST: empty @ModelAttribute (all fields null), confirmed=true
    org.mybatis.jpetstore.domain.Order emptyOrder = new org.mybatis.jpetstore.domain.Order();
    String view = orderController.newOrder(emptyOrder, false, true, session, model);

    assertThat(view).isEqualTo("order/ViewOrder");
    assertThat(model.asMap().get("message")).isEqualTo("Thank you, your order has been submitted.");

    // Verify insertOrder was called exactly once
    verify(orderService, times(1)).insertOrder(any(org.mybatis.jpetstore.domain.Order.class));

    // The session order must NOT have been overwritten with null
    assertThat(sessionOrder.getBillAddress1()).isEqualTo("901 San Antonio Road");
    assertThat(sessionOrder.getBillCity()).isEqualTo("Palo Alto");
  }
}
