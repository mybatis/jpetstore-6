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
import static org.mockito.Mockito.mock;
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
}
