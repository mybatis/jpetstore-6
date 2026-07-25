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
package org.mybatis.jpetstore.domain;

import java.io.Serializable;

import net.sourceforge.stripes.validation.Validate;

/**
 * The Class Account.
 *
 * @author Eduardo Macarron
 */
public class Account implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = 8751282105532159742L;

  /** The username. */
  private String username;
  /** The password. */
  private String password;
  /** The email. */
  private String email;
  /** The first name. */
  private String firstName;
  /** The last name. */
  private String lastName;
  /** The status. */
  private String status;
  /** The address1. */
  private String address1;
  /** The address2. */
  private String address2;
  /** The city. */
  private String city;
  /** The state. */
  private String state;
  /** The zip. */
  private String zip;
  /** The country. */
  private String country;
  /** The phone. */
  private String phone;
  /** The favourite category id. */
  private String favouriteCategoryId;
  /** The language preference. */
  private String languagePreference;
  /** The list option. */
  private boolean listOption;
  /** The banner option. */
  private boolean bannerOption;
  /** The banner name. */
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
   * @param username
   *          the username
   */
  public void setUsername(String username) {
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
   * @param password
   *          the password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email.
   *
   * @param email
   *          the email
   */
  public void setEmail(String email) {
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
   * Sets the first name.
   *
   * @param firstName
   *          the first name
   */
  @Validate(required = true, on = { "newAccount", "editAccount" })
  public void setFirstName(String firstName) {
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
   * Sets the last name.
   *
   * @param lastName
   *          the last name
   */
  @Validate(required = true, on = { "newAccount", "editAccount" })
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status
   *          the status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gets the address1.
   *
   * @return the address1
   */
  public String getAddress1() {
    return address1;
  }

  /**
   * Sets the address1.
   *
   * @param address1
   *          the address1
   */
  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  /**
   * Gets the address2.
   *
   * @return the address2
   */
  public String getAddress2() {
    return address2;
  }

  /**
   * Sets the address2.
   *
   * @param address2
   *          the address2
   */
  public void setAddress2(String address2) {
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
   * @param city
   *          the city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Gets the state.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the state.
   *
   * @param state
   *          the state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Gets the zip.
   *
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * Sets the zip.
   *
   * @param zip
   *          the zip
   */
  public void setZip(String zip) {
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
   * @param country
   *          the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets the phone.
   *
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Sets the phone.
   *
   * @param phone
   *          the phone
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Gets the favourite category id.
   *
   * @return the favourite category id
   */
  public String getFavouriteCategoryId() {
    return favouriteCategoryId;
  }

  /**
   * Sets the favourite category id.
   *
   * @param favouriteCategoryId
   *          the favourite category id
   */
  public void setFavouriteCategoryId(String favouriteCategoryId) {
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
   * @param languagePreference
   *          the language preference
   */
  public void setLanguagePreference(String languagePreference) {
    this.languagePreference = languagePreference;
  }

  /**
   * Checks if is list option.
   *
   * @return true, if successful
   */
  public boolean isListOption() {
    return listOption;
  }

  /**
   * Sets the list option.
   *
   * @param listOption
   *          the list option
   */
  public void setListOption(boolean listOption) {
    this.listOption = listOption;
  }

  /**
   * Checks if is banner option.
   *
   * @return true, if successful
   */
  public boolean isBannerOption() {
    return bannerOption;
  }

  /**
   * Sets the banner option.
   *
   * @param bannerOption
   *          the banner option
   */
  public void setBannerOption(boolean bannerOption) {
    this.bannerOption = bannerOption;
  }

  /**
   * Gets the banner name.
   *
   * @return the banner name
   */
  public String getBannerName() {
    return bannerName;
  }

  /**
   * Sets the banner name.
   *
   * @param bannerName
   *          the banner name
   */
  public void setBannerName(String bannerName) {
    this.bannerName = bannerName;
  }

}
