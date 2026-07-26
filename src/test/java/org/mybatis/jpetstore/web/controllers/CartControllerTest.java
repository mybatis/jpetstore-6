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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

  @Mock
  private CatalogService catalogService;

  @InjectMocks
  private CartController cartController;

  @Test
  void viewCartReturnsCartView() {
    HttpSession session = mock(HttpSession.class);
    Cart cart = new Cart();
    when(session.getAttribute("cart")).thenReturn(cart);
    Model model = new ExtendedModelMap();

    String view = cartController.viewCart(session, model);

    assertThat(view).isEqualTo("cart/Cart");
    assertThat(model.asMap()).containsKey("cart");
  }

  @Test
  void addItemWithNullIdReturnsError() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();

    String view = cartController.addItemToCart(null, session, model);

    assertThat(view).isEqualTo("common/Error");
    assertThat(model.asMap()).containsKey("message");
  }

  @Test
  void removeItemWithNullIdReturnsError() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();

    String view = cartController.removeItemFromCart(null, session, model);

    assertThat(view).isEqualTo("common/Error");
    assertThat(model.asMap()).containsKey("message");
  }
}
