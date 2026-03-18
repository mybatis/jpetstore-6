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

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

  private static final String SIGNON_VIEW = "account/SignonForm";
  private static final String NEW_ACCOUNT_VIEW = "account/NewAccountForm";
  private static final String EDIT_ACCOUNT_VIEW = "account/EditAccountForm";

  private static final List<String> LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
  private static final List<String> CATEGORY_LIST = Collections
      .unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));

  @Autowired
  private AccountService accountService;
  @Autowired
  private CatalogService catalogService;

  @GetMapping({ "", "/" })
  public String signonForm() {
    return SIGNON_VIEW;
  }

  @PostMapping("/signon")
  public String signon(@RequestParam("username") String username, @RequestParam("password") String password,
      HttpSession session, Model model) {
    Account account = accountService.getAccount(username, password);
    if (account == null) {
      model.addAttribute("message", "Invalid username or password.  Signon failed.");
      return SIGNON_VIEW;
    }
    account.setPassword(null);
    List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
    session.setAttribute("accountBean", new AccountSession(account, myList, true));
    return "redirect:/catalog";
  }

  @GetMapping("/signoff")
  public String signoff(HttpSession session) {
    session.invalidate();
    return "redirect:/catalog";
  }

  @GetMapping("/new")
  public String newAccountForm(Model model) {
    model.addAttribute("languages", LANGUAGE_LIST);
    model.addAttribute("categories", CATEGORY_LIST);
    model.addAttribute("account", new Account());
    return NEW_ACCOUNT_VIEW;
  }

  @PostMapping("/new")
  public String newAccount(@ModelAttribute Account account, HttpSession session) {
    accountService.insertAccount(account);
    Account savedAccount = accountService.getAccount(account.getUsername());
    List<Product> myList = catalogService.getProductListByCategory(savedAccount.getFavouriteCategoryId());
    session.setAttribute("accountBean", new AccountSession(savedAccount, myList, true));
    return "redirect:/catalog";
  }

  @GetMapping("/edit")
  public String editAccountForm(HttpSession session, Model model) {
    AccountSession accountSession = (AccountSession) session.getAttribute("accountBean");
    if (accountSession != null) {
      model.addAttribute("account", accountSession.getAccount());
    }
    model.addAttribute("languages", LANGUAGE_LIST);
    model.addAttribute("categories", CATEGORY_LIST);
    return EDIT_ACCOUNT_VIEW;
  }

  @PostMapping("/edit")
  public String editAccount(@ModelAttribute Account account, HttpSession session) {
    accountService.updateAccount(account);
    Account updatedAccount = accountService.getAccount(account.getUsername());
    List<Product> myList = catalogService.getProductListByCategory(updatedAccount.getFavouriteCategoryId());
    session.setAttribute("accountBean", new AccountSession(updatedAccount, myList, true));
    return "redirect:/catalog";
  }

  /**
   * Inner class to hold account session data, compatible with JSP ${sessionScope.accountBean.*} references.
   */
  public static class AccountSession implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private final Account account;
    private final List<Product> myList;
    private final boolean authenticated;

    public AccountSession(Account account, List<Product> myList, boolean authenticated) {
      this.account = account;
      this.myList = myList;
      this.authenticated = authenticated;
    }

    public Account getAccount() {
      return account;
    }

    public List<Product> getMyList() {
      return myList;
    }

    public boolean isAuthenticated() {
      return authenticated && account != null && account.getUsername() != null;
    }
  }
}
