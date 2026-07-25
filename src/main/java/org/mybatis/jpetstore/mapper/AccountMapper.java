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
package org.mybatis.jpetstore.mapper;

import org.mybatis.jpetstore.domain.Account;

/**
 * The Interface AccountMapper.
 *
 * @author Eduardo Macarron
 */
public interface AccountMapper {

  /**
   * Get account by username.
   *
   * @param username
   *          the username
   *
   * @return the account
   */
  Account getAccountByUsername(String username);

  /**
   * Get account by username and password.
   *
   * @param username
   *          the username
   * @param password
   *          the password
   *
   * @return the account
   */
  Account getAccountByUsernameAndPassword(String username, String password);

  /**
   * Insert account.
   *
   * @param account
   *          the account
   */
  void insertAccount(Account account);

  /**
   * Insert profile.
   *
   * @param account
   *          the account
   */
  void insertProfile(Account account);

  /**
   * Insert signon.
   *
   * @param account
   *          the account
   */
  void insertSignon(Account account);

  /**
   * Update account.
   *
   * @param account
   *          the account
   */
  void updateAccount(Account account);

  /**
   * Update profile.
   *
   * @param account
   *          the account
   */
  void updateProfile(Account account);

  /**
   * Update signon.
   *
   * @param account
   *          the account
   */
  void updateSignon(Account account);

}
