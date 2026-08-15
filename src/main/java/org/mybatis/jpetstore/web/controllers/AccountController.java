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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

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

/**
 * The Class AccountController.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

  /** The signon view. */
  private static final String SIGNON_VIEW = "account/SignonForm";
  /** The new account view. */
  private static final String NEW_ACCOUNT_VIEW = "account/NewAccountForm";
  /** The edit account view. */
  private static final String EDIT_ACCOUNT_VIEW = "account/EditAccountForm";

  /** The language list. */
  private static final List<String> LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
  /** The category list. */
  private static final List<String> CATEGORY_LIST = Collections
      .unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));

  /** The account service. */
  @Autowired
  private AccountService accountService;
  /** The catalog service. */
  @Autowired
  private CatalogService catalogService;

  /**
   * Signon form.
   *
   * @return the string
   */
  @GetMapping({ "", "/" })
  public String signonForm() {
    return SIGNON_VIEW;
  }

  /**
   * Signon.
   *
   * @param username
   *          the username
   * @param password
   *          the password
   * @param session
   *          the session
   * @param model
   *          the model
   *
   * @return the string
   */
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

  /**
   * Signoff.
   *
   * @param session
   *          the session
   *
   * @return the string
   */
  @GetMapping("/signoff")
  public String signoff(HttpSession session) {
    session.invalidate();
    return "redirect:/catalog";
  }

  /**
   * New account form.
   *
   * @param model
   *          the model
   *
   * @return the string
   */
  @GetMapping("/new")
  public String newAccountForm(Model model) {
    model.addAttribute("languages", LANGUAGE_LIST);
    model.addAttribute("categories", CATEGORY_LIST);
    model.addAttribute("account", new Account());
    return NEW_ACCOUNT_VIEW;
  }

  /**
   * New account.
   *
   * @param account
   *          the account
   *
   * @return the string
   */
  @PostMapping("/new")
  public String newAccount(@ModelAttribute Account account) {
    accountService.insertAccount(account);
    return "redirect:/catalog";
  }

  /**
   * Edit account form.
   *
   * @param session
   *          the session
   * @param model
   *          the model
   *
   * @return the string
   */
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

  /**
   * Edit account.
   *
   * @param account
   *          the account
   * @param session
   *          the session
   *
   * @return the string
   */
  @PostMapping("/edit")
  public String editAccount(@ModelAttribute Account account, HttpSession session) {
    accountService.updateAccount(account);
    Account updatedAccount = accountService.getAccount(account.getUsername());
    List<Product> myList = catalogService.getProductListByCategory(updatedAccount.getFavouriteCategoryId());
    session.setAttribute("accountBean", new AccountSession(updatedAccount, myList, true));
    return "redirect:/account/edit";
  }

  /**
   * Inner class to hold account session data, compatible with JSP ${sessionScope.accountBean.*} references.
   */
  public static class AccountSession implements java.io.Serializable {
    /** The serial version uid. */
    private static final long serialVersionUID = 1L;
    /** The account. */
    private final Account account;
    /** The my list. */
    private final List<Product> myList;
    /** The authenticated. */
    private final boolean authenticated;

    /**
     * Instantiates a new account session.
     *
     * @param account
     *          the account
     * @param myList
     *          the my list
     * @param authenticated
     *          the authenticated
     */
    public AccountSession(Account account, List<Product> myList, boolean authenticated) {
      this.account = account;
      this.myList = myList;
      this.authenticated = authenticated;
    }

    /**
     * Gets the account.
     *
     * @return the account
     */
    public Account getAccount() {
      return account;
    }

    /**
     * Gets the my list.
     *
     * @return the my list
     */
    public List<Product> getMyList() {
      return myList;
    }

    /**
     * Checks if is authenticated.
     *
     * @return true, if successful
     */
    public boolean isAuthenticated() {
      return authenticated && account != null && account.getUsername() != null;
    }
  }
}
