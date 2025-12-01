/*
 *    Copyright 2010-2025 the original author or authors.
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
package org.mybatis.jpetstore.web.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.service.AccountService;
import org.mybatis.jpetstore.service.CatalogService;

/**
 * The Class AccountActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class AccountActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 5499663666155758178L;

  private static final String NEW_ACCOUNT = "/WEB-INF/jsp/account/NewAccountForm.jsp";
  private static final String EDIT_ACCOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";
  private static final String SIGNON = "/WEB-INF/jsp/account/SignonForm.jsp";

  private static final List<String> LANGUAGE_LIST;
  private static final List<String> CATEGORY_LIST;

  @SpringBean
  private transient AccountService accountService;
  @SpringBean
  private transient CatalogService catalogService;

  private Account account = new Account();
  private List<Product> myList;
  private boolean authenticated;

  static {
    LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
    CATEGORY_LIST = Collections.unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));
  }

  public Account getAccount() {
    return this.account;
  }

  public String getUsername() {
    return account.getUsername();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setUsername(String username) {
    account.setUsername(username);
  }

  public String getPassword() {
    return account.getPassword();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setPassword(String password) {
    account.setPassword(password);
  }

  public List<Product> getMyList() {
    return myList;
  }

  public void setMyList(List<Product> myList) {
    this.myList = myList;
  }

  public List<String> getLanguages() {
    return LANGUAGE_LIST;
  }

  public List<String> getCategories() {
    return CATEGORY_LIST;
  }

  public Resolution newAccountForm() {
    return new ForwardResolution(NEW_ACCOUNT);
  }

  /**
   * New account.
   *
   * @return the resolution
   */
  public Resolution newAccount() {
    // 모든 필수 필드 검증
    if (!validateAccountFields()) {
      return new ForwardResolution(NEW_ACCOUNT);
    }

    accountService.insertAccount(account);
    account = accountService.getAccount(account.getUsername());
    myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
    authenticated = true;
    return new RedirectResolution(InitialRecommendationActionBean.class);
  }

  /**
   * Edits the account form.
   *
   * @return the resolution
   */
  public Resolution editAccountForm() {
    return new ForwardResolution(EDIT_ACCOUNT);
  }

  /**
   * Edits the account.
   *
   * @return the resolution
   */
  public Resolution editAccount() {
    // 모든 필수 필드 검증
    if (!validateAccountFields()) {
      return new ForwardResolution(EDIT_ACCOUNT);
    }

    accountService.updateAccount(account);
    account = accountService.getAccount(account.getUsername());
    myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
    return new RedirectResolution(CatalogActionBean.class);
  }

  /**
   * Signon form.
   *
   * @return the resolution
   */
  @DefaultHandler
  public Resolution signonForm() {
    return new ForwardResolution(SIGNON);
  }

  /**
   * Signon.
   *
   * @return the resolution
   */
  public Resolution signon() {
    // 빈 값 검증
    String username = getUsername();
    String password = getPassword();

    if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
      setMessage("사용자 정보를 모두 입력해주세요.");
      return new ForwardResolution(SIGNON);
    }

    account = accountService.getAccount(username, password);

    if (account == null) {
      String value = "Invalid username or password.  Signon failed.";
      setMessage(value);
      clear();
      return new ForwardResolution(SIGNON);
    } else {
      account.setPassword(null);
      myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
      authenticated = true;
      HttpSession s = context.getRequest().getSession();
      // this bean is already registered as /actions/Account.action
      s.setAttribute("accountBean", this);
      return new RedirectResolution(CatalogActionBean.class);
    }
  }

  /**
   * Signoff.
   *
   * @return the resolution
   */
  public Resolution signoff() {
    context.getRequest().getSession().invalidate();
    clear();
    return new RedirectResolution(CatalogActionBean.class);
  }

  /**
   * Checks if is authenticated.
   *
   * @return true, if is authenticated
   */
  public boolean isAuthenticated() {
    return authenticated && account != null && account.getUsername() != null;
  }

  /**
   * Clear.
   */
  public void clear() {
    account = new Account();
    myList = null;
    authenticated = false;
  }

  /**
   * Validate all required account fields.
   *
   * @return true if all required fields are filled, false otherwise
   */
  private boolean validateAccountFields() {
    StringBuilder errors = new StringBuilder();

    if (account.getFirstName() == null || account.getFirstName().trim().isEmpty()) {
      errors.append("First name is required. ");
    }
    if (account.getLastName() == null || account.getLastName().trim().isEmpty()) {
      errors.append("Last name is required. ");
    }
    if (account.getEmail() == null || account.getEmail().trim().isEmpty()) {
      errors.append("Email is required. ");
    }
    if (account.getPhone() == null || account.getPhone().trim().isEmpty()) {
      errors.append("Phone is required. ");
    }
    if (account.getAddress1() == null || account.getAddress1().trim().isEmpty()) {
      errors.append("Address 1 is required. ");
    }
    if (account.getCity() == null || account.getCity().trim().isEmpty()) {
      errors.append("City is required. ");
    }
    if (account.getState() == null || account.getState().trim().isEmpty()) {
      errors.append("State is required. ");
    }
    if (account.getZip() == null || account.getZip().trim().isEmpty()) {
      errors.append("Zip is required. ");
    }
    if (account.getCountry() == null || account.getCountry().trim().isEmpty()) {
      errors.append("Country is required. ");
    }

    if (errors.length() > 0) {
      setMessage("모든 필수 정보를 입력해주세요: " + errors.toString());
      return false;
    }

    return true;
  }

}
