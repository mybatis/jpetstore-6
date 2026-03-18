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
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock
  private AccountService accountService;
  @Mock
  private CatalogService catalogService;

  @InjectMocks
  private AccountController accountController;

  @Test
  void signonFormReturnsSignonView() {
    assertThat(accountController.signonForm()).isEqualTo("account/SignonForm");
  }

  @Test
  void signonWithInvalidCredentialsReturnsSignonView() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    when(accountService.getAccount("bad", "bad")).thenReturn(null);

    String view = accountController.signon("bad", "bad", session, model);

    assertThat(view).isEqualTo("account/SignonForm");
    assertThat(model.asMap()).containsKey("message");
  }

  @Test
  void signonWithValidCredentialsRedirects() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    Account account = new Account();
    account.setUsername("j2ee");
    account.setFavouriteCategoryId("FISH");
    when(accountService.getAccount("j2ee", "j2ee")).thenReturn(account);
    when(catalogService.getProductListByCategory("FISH")).thenReturn(Collections.emptyList());

    String view = accountController.signon("j2ee", "j2ee", session, model);

    assertThat(view).isEqualTo("redirect:/catalog");
  }

  @Test
  void newAccountFormReturnsNewAccountView() {
    Model model = new ExtendedModelMap();
    String view = accountController.newAccountForm(model);
    assertThat(view).isEqualTo("account/NewAccountForm");
    assertThat(model.asMap()).containsKey("languages");
    assertThat(model.asMap()).containsKey("categories");
  }

  @Test
  void accountSessionIsAuthenticatedWithValidAccount() {
    Account account = new Account();
    account.setUsername("j2ee");
    AccountController.AccountSession session = new AccountController.AccountSession(account, Collections.emptyList(),
        true);
    assertThat(session.isAuthenticated()).isTrue();
  }

  @Test
  void accountSessionIsNotAuthenticatedWithNullAccount() {
    AccountController.AccountSession session = new AccountController.AccountSession(null, Collections.emptyList(),
        true);
    assertThat(session.isAuthenticated()).isFalse();
  }
}
