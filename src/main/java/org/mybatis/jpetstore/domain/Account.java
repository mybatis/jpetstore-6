/*
 * Copyright 2010-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mybatis.jpetstore.domain;

import java.io.Serializable;

import net.sourceforge.stripes.validation.Validate;

/**
 * The Class Account.
 * Represents a user account within the JPetStore application.
 *
 * @author Eduardo Macarron
 */
public class Account implements Serializable {

  private static final long serialVersionUID = 8751282105532159742L;

  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String status;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String zip;
  private String country;
  private String phone;
  private String favouriteCategoryId;
  private String languagePreference;
  private boolean listOption;
  private boolean bannerOption;
  private String bannerName;

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username the new username
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password.
   *
   * @param password the new password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Gets the email address.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address.
   *
   * @param email the new email
   */
  public void setEmail(final String email) {
    this.email = email;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name. This field is required when creating or editing an account.
   *
   * @param firstName the new first name
   */
  @Validate(required = true, on = { "newAccount", "editAccount" })
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name. This field is required when creating or editing an account.
   *
   * @param lastName the new last name
   */
  @Validate(required = true, on = { "newAccount", "editAccount" })
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the account status.
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the account status.
   *
   * @param status the new status
   */
  public void setStatus(final String status) {
    this.status = status;
  }

  /**
   * Gets the first line of the address.
   *
   * @return the address1
   */
  public String getAddress1() {
    return address1;
  }

  /**
   * Sets the first line of the address.
   *
   * @param address1 the new address1
   */
  public void setAddress1(final String address1) {
    this.address1 = address1;
  }

  /**
   * Gets the second line of the address.
   *
   * @return the address2
   */
  public String getAddress2() {
    return address2;
  }

  /**
   * Sets the second line of the address.
   *
   * @param address2 the new address2
   */
  public void setAddress2(final String address2) {
    this.address2 = address2;
  }

  /**
   * Gets the city.
   *
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the city.
   *
   * @param city the new city
   */
  public void setCity(final String city) {
    this.city = city;
  }

  /**
   * Gets the state or province.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the state or province.
   *
   * @param state the new state
   */
  public void setState(final String state) {
    this.state = state;
  }

  /**
   * Gets the zip or postal code.
   *
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * Sets the zip or postal code.
   *
   * @param zip the new zip
   */
  public void setZip(final String zip) {
    this.zip = zip;
  }

  /**
   * Gets the country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets the country.
   *
   * @param country the new country
   */
  public void setCountry(final String country) {
    this.country = country;
  }

  /**
   * Gets the phone number.
   *
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Sets the phone number.
   *
   * @param phone the new phone
   */
  public void setPhone(final String phone) {
    this.phone = phone;
  }

  /**
   * Gets the user's favourite category ID (e.g., DOGS, CATS).
   *
   * @return the favourite category ID
   */
  public String getFavouriteCategoryId() {
    return favouriteCategoryId;
  }

  /**
   * Sets the user's favourite category ID.
   *
   * @param favouriteCategoryId the new favourite category ID
   */
  public void setFavouriteCategoryId(final String favouriteCategoryId) {
    this.favouriteCategoryId = favouriteCategoryId;
  }

  /**
   * Gets the language preference.
   *
   * @return the language preference
   */
  public String getLanguagePreference() {
    return languagePreference;
  }

  /**
   * Sets the language preference.
   *
   * @param languagePreference the new language preference
   */
  public void setLanguagePreference(final String languagePreference) {
    this.languagePreference = languagePreference;
  }

  /**
   * Checks if the list option is enabled for the user.
   *
   * @return true, if list option is enabled
   */
  public boolean isListOption() {
    return listOption;
  }

  /**
   * Sets the list option preference.
   *
   * @param listOption the new list option preference
   */
  public void setListOption(final boolean listOption) {
    this.listOption = listOption;
  }

  /**
   * Checks if the banner option is enabled for the user.
   *
   * @return true, if banner option is enabled
   */
  public boolean isBannerOption() {
    return bannerOption;
  }

  /**
   * Sets the banner option preference.
   *
   * @param bannerOption the new banner option preference
   */
  public void setBannerOption(final boolean bannerOption) {
    this.bannerOption = bannerOption;
  }

  /**
   * Gets the banner name associated with the user's favourite category.
   *
   * @return the banner name
   */
  public String getBannerName() {
    return bannerName;
  }

  /**
   * Sets the banner name.
   *
   * @param bannerName the new banner name
   */
  public void setBannerName(final String bannerName) {
    this.bannerName = bannerName;
  }

}
