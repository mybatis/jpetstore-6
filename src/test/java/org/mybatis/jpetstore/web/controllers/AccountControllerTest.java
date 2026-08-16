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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpSession;

import java.util.List;

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

/**
 * The Class AccountControllerTest.
 */
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  /** The account service. */
  @Mock
  private AccountService accountService;
  /** The catalog service. */
  @Mock
  private CatalogService catalogService;

  /** The account controller. */
  @InjectMocks
  private AccountController accountController;

  /**
   * Signon form returns signon view.
   */
  @Test
  void signonFormReturnsSignonView() {
    assertThat(accountController.signonForm()).isEqualTo("account/SignonForm");
  }

  /**
   * Signon with invalid credentials returns signon view.
   */
  @Test
  void signonWithInvalidCredentialsReturnsSignonView() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    when(accountService.getAccount("bad", "bad")).thenReturn(null);

    String view = accountController.signon("bad", "bad", session, model);

    assertThat(view).isEqualTo("account/SignonForm");
    assertThat(model.asMap()).containsKey("message");
  }

  /**
   * Signon with valid credentials redirects.
   */
  @Test
  void signonWithValidCredentialsRedirects() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    Account account = new Account();
    account.setUsername("j2ee");
    account.setFavouriteCategoryId("FISH");
    when(accountService.getAccount("j2ee", "j2ee")).thenReturn(account);
    when(catalogService.getProductListByCategory("FISH")).thenReturn(List.of());

    String view = accountController.signon("j2ee", "j2ee", session, model);

    assertThat(view).isEqualTo("redirect:/catalog");
  }

  /**
   * New account form returns new account view.
   */
  @Test
  void newAccountFormReturnsNewAccountView() {
    Model model = new ExtendedModelMap();
    String view = accountController.newAccountForm(model);
    assertThat(view).isEqualTo("account/NewAccountForm");
    assertThat(model.asMap()).containsKey("languages");
    assertThat(model.asMap()).containsKey("categories");
  }

  /**
   * Signoff invalidates session and redirects to catalog.
   */
  @Test
  void signoffInvalidatesSessionAndRedirectsToCatalog() {
    HttpSession session = mock(HttpSession.class);

    String view = accountController.signoff(session);

    assertThat(view).isEqualTo("redirect:/catalog");
    verify(session, times(1)).invalidate();
  }

  /**
   * Account session is authenticated with valid account.
   */
  @Test
  void accountSessionIsAuthenticatedWithValidAccount() {
    Account account = new Account();
    account.setUsername("j2ee");
    AccountController.AccountSession session = new AccountController.AccountSession(account, List.of(), true);
    assertThat(session.isAuthenticated()).isTrue();
  }

  /**
   * Account session is not authenticated with null account.
   */
  @Test
  void accountSessionIsNotAuthenticatedWithNullAccount() {
    AccountController.AccountSession session = new AccountController.AccountSession(null, List.of(), true);
    assertThat(session.isAuthenticated()).isFalse();
  }

  /**
   * New account redirects to catalog without logging in.
   */
  @Test
  void newAccountRedirectsToCatalogWithoutLoggingIn() {
    // Registration should insert the account and redirect to catalog WITHOUT
    // auto-logging the user in - the IT test expects the user to manually sign in.
    Account account = new Account();
    account.setUsername("newuser");

    String view = accountController.newAccount(account);

    assertThat(view).isEqualTo("redirect:/catalog");
  }

  /**
   * Edit account redirects to edit page not catalog.
   */
  @Test
  void editAccountRedirectsToEditPageNotCatalog() {
    // After saving account, should redirect to /account/edit so the user stays
    // on their profile page (the IT test expects the edit form to reload).
    HttpSession session = mock(HttpSession.class);
    Account account = new Account();
    account.setUsername("j2ee");
    account.setFavouriteCategoryId("FISH");
    when(accountService.getAccount("j2ee")).thenReturn(account);
    when(catalogService.getProductListByCategory("FISH")).thenReturn(List.of());

    String view = accountController.editAccount(account, session);

    assertThat(view).isEqualTo("redirect:/account/edit");
  }

  /**
   * Edit account form with session returns edit account view with account.
   */
  @Test
  void editAccountFormWithSessionReturnsEditAccountViewWithAccount() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    Account account = new Account();
    account.setUsername("j2ee");
    AccountController.AccountSession accountSession = new AccountController.AccountSession(account, List.of(), true);
    when(session.getAttribute("accountBean")).thenReturn(accountSession);

    String view = accountController.editAccountForm(session, model);

    assertThat(view).isEqualTo("account/EditAccountForm");
    assertThat(model.asMap().get("account")).isSameAs(account);
    assertThat(model.asMap()).containsKey("languages");
    assertThat(model.asMap()).containsKey("categories");
  }

  /**
   * Edit account form without session returns edit account view without account.
   */
  @Test
  void editAccountFormWithoutSessionReturnsEditAccountViewWithoutAccount() {
    HttpSession session = mock(HttpSession.class);
    Model model = new ExtendedModelMap();
    when(session.getAttribute("accountBean")).thenReturn(null);

    String view = accountController.editAccountForm(session, model);

    assertThat(view).isEqualTo("account/EditAccountForm");
    assertThat(model.asMap()).doesNotContainKey("account");
    assertThat(model.asMap()).containsKey("languages");
    assertThat(model.asMap()).containsKey("categories");
  }
}
