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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The Class Order.
 *
 * @author Eduardo Macarron
 */
public class Order implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = 6321792448424424931L;

  /** The order id. */
  private int orderId;
  /** The username. */
  private String username;
  /** The order date. */
  private Date orderDate;
  /** The ship address1. */
  private String shipAddress1;
  /** The ship address2. */
  private String shipAddress2;
  /** The ship city. */
  private String shipCity;
  /** The ship state. */
  private String shipState;
  /** The ship zip. */
  private String shipZip;
  /** The ship country. */
  private String shipCountry;
  /** The bill address1. */
  private String billAddress1;
  /** The bill address2. */
  private String billAddress2;
  /** The bill city. */
  private String billCity;
  /** The bill state. */
  private String billState;
  /** The bill zip. */
  private String billZip;
  /** The bill country. */
  private String billCountry;
  /** The courier. */
  private String courier;
  /** The total price. */
  private BigDecimal totalPrice;
  /** The bill to first name. */
  private String billToFirstName;
  /** The bill to last name. */
  private String billToLastName;
  /** The ship to first name. */
  private String shipToFirstName;
  /** The ship to last name. */
  private String shipToLastName;
  /** The credit card. */
  private String creditCard;
  /** The expiry date. */
  private String expiryDate;
  /** The card type. */
  private String cardType;
  /** The locale. */
  private String locale;
  /** The status. */
  private String status;
  /** The line items. */
  private List<LineItem> lineItems = new ArrayList<>();

  /**
   * Gets the order id.
   *
   * @return the order id
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * Sets the order id.
   *
   * @param orderId
   *          the order id
   */
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

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
   * Gets the order date.
   *
   * @return the order date
   */
  public Date getOrderDate() {
    return orderDate;
  }

  /**
   * Sets the order date.
   *
   * @param orderDate
   *          the order date
   */
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  /**
   * Gets the ship address1.
   *
   * @return the ship address1
   */
  public String getShipAddress1() {
    return shipAddress1;
  }

  /**
   * Sets the ship address1.
   *
   * @param shipAddress1
   *          the ship address1
   */
  public void setShipAddress1(String shipAddress1) {
    this.shipAddress1 = shipAddress1;
  }

  /**
   * Gets the ship address2.
   *
   * @return the ship address2
   */
  public String getShipAddress2() {
    return shipAddress2;
  }

  /**
   * Sets the ship address2.
   *
   * @param shipAddress2
   *          the ship address2
   */
  public void setShipAddress2(String shipAddress2) {
    this.shipAddress2 = shipAddress2;
  }

  /**
   * Gets the ship city.
   *
   * @return the ship city
   */
  public String getShipCity() {
    return shipCity;
  }

  /**
   * Sets the ship city.
   *
   * @param shipCity
   *          the ship city
   */
  public void setShipCity(String shipCity) {
    this.shipCity = shipCity;
  }

  /**
   * Gets the ship state.
   *
   * @return the ship state
   */
  public String getShipState() {
    return shipState;
  }

  /**
   * Sets the ship state.
   *
   * @param shipState
   *          the ship state
   */
  public void setShipState(String shipState) {
    this.shipState = shipState;
  }

  /**
   * Gets the ship zip.
   *
   * @return the ship zip
   */
  public String getShipZip() {
    return shipZip;
  }

  /**
   * Sets the ship zip.
   *
   * @param shipZip
   *          the ship zip
   */
  public void setShipZip(String shipZip) {
    this.shipZip = shipZip;
  }

  /**
   * Gets the ship country.
   *
   * @return the ship country
   */
  public String getShipCountry() {
    return shipCountry;
  }

  /**
   * Sets the ship country.
   *
   * @param shipCountry
   *          the ship country
   */
  public void setShipCountry(String shipCountry) {
    this.shipCountry = shipCountry;
  }

  /**
   * Gets the bill address1.
   *
   * @return the bill address1
   */
  public String getBillAddress1() {
    return billAddress1;
  }

  /**
   * Sets the bill address1.
   *
   * @param billAddress1
   *          the bill address1
   */
  public void setBillAddress1(String billAddress1) {
    this.billAddress1 = billAddress1;
  }

  /**
   * Gets the bill address2.
   *
   * @return the bill address2
   */
  public String getBillAddress2() {
    return billAddress2;
  }

  /**
   * Sets the bill address2.
   *
   * @param billAddress2
   *          the bill address2
   */
  public void setBillAddress2(String billAddress2) {
    this.billAddress2 = billAddress2;
  }

  /**
   * Gets the bill city.
   *
   * @return the bill city
   */
  public String getBillCity() {
    return billCity;
  }

  /**
   * Sets the bill city.
   *
   * @param billCity
   *          the bill city
   */
  public void setBillCity(String billCity) {
    this.billCity = billCity;
  }

  /**
   * Gets the bill state.
   *
   * @return the bill state
   */
  public String getBillState() {
    return billState;
  }

  /**
   * Sets the bill state.
   *
   * @param billState
   *          the bill state
   */
  public void setBillState(String billState) {
    this.billState = billState;
  }

  /**
   * Gets the bill zip.
   *
   * @return the bill zip
   */
  public String getBillZip() {
    return billZip;
  }

  /**
   * Sets the bill zip.
   *
   * @param billZip
   *          the bill zip
   */
  public void setBillZip(String billZip) {
    this.billZip = billZip;
  }

  /**
   * Gets the bill country.
   *
   * @return the bill country
   */
  public String getBillCountry() {
    return billCountry;
  }

  /**
   * Sets the bill country.
   *
   * @param billCountry
   *          the bill country
   */
  public void setBillCountry(String billCountry) {
    this.billCountry = billCountry;
  }

  /**
   * Gets the courier.
   *
   * @return the courier
   */
  public String getCourier() {
    return courier;
  }

  /**
   * Sets the courier.
   *
   * @param courier
   *          the courier
   */
  public void setCourier(String courier) {
    this.courier = courier;
  }

  /**
   * Gets the total price.
   *
   * @return the total price
   */
  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  /**
   * Sets the total price.
   *
   * @param totalPrice
   *          the total price
   */
  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  /**
   * Gets the bill to first name.
   *
   * @return the bill to first name
   */
  public String getBillToFirstName() {
    return billToFirstName;
  }

  /**
   * Sets the bill to first name.
   *
   * @param billToFirstName
   *          the bill to first name
   */
  public void setBillToFirstName(String billToFirstName) {
    this.billToFirstName = billToFirstName;
  }

  /**
   * Gets the bill to last name.
   *
   * @return the bill to last name
   */
  public String getBillToLastName() {
    return billToLastName;
  }

  /**
   * Sets the bill to last name.
   *
   * @param billToLastName
   *          the bill to last name
   */
  public void setBillToLastName(String billToLastName) {
    this.billToLastName = billToLastName;
  }

  /**
   * Gets the ship to first name.
   *
   * @return the ship to first name
   */
  public String getShipToFirstName() {
    return shipToFirstName;
  }

  /**
   * Sets the ship to first name.
   *
   * @param shipFoFirstName
   *          the ship fo first name
   */
  public void setShipToFirstName(String shipFoFirstName) {
    this.shipToFirstName = shipFoFirstName;
  }

  /**
   * Gets the ship to last name.
   *
   * @return the ship to last name
   */
  public String getShipToLastName() {
    return shipToLastName;
  }

  /**
   * Sets the ship to last name.
   *
   * @param shipToLastName
   *          the ship to last name
   */
  public void setShipToLastName(String shipToLastName) {
    this.shipToLastName = shipToLastName;
  }

  /**
   * Gets the credit card.
   *
   * @return the credit card
   */
  public String getCreditCard() {
    return creditCard;
  }

  /**
   * Sets the credit card.
   *
   * @param creditCard
   *          the credit card
   */
  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  /**
   * Gets the expiry date.
   *
   * @return the expiry date
   */
  public String getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets the expiry date.
   *
   * @param expiryDate
   *          the expiry date
   */
  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * Gets the card type.
   *
   * @return the card type
   */
  public String getCardType() {
    return cardType;
  }

  /**
   * Sets the card type.
   *
   * @param cardType
   *          the card type
   */
  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  /**
   * Gets the locale.
   *
   * @return the locale
   */
  public String getLocale() {
    return locale;
  }

  /**
   * Sets the locale.
   *
   * @param locale
   *          the locale
   */
  public void setLocale(String locale) {
    this.locale = locale;
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
   * Sets the line items.
   *
   * @param lineItems
   *          the line items
   */
  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  /**
   * Gets the line items.
   *
   * @return the line items
   */
  public List<LineItem> getLineItems() {
    return lineItems;
  }

  /**
   * Inits the order.
   *
   * @param account
   *          the account
   * @param cart
   *          the cart
   */
  public void initOrder(Account account, Cart cart) {

    username = account.getUsername();
    orderDate = new Date();

    shipToFirstName = account.getFirstName();
    shipToLastName = account.getLastName();
    shipAddress1 = account.getAddress1();
    shipAddress2 = account.getAddress2();
    shipCity = account.getCity();
    shipState = account.getState();
    shipZip = account.getZip();
    shipCountry = account.getCountry();

    billToFirstName = account.getFirstName();
    billToLastName = account.getLastName();
    billAddress1 = account.getAddress1();
    billAddress2 = account.getAddress2();
    billCity = account.getCity();
    billState = account.getState();
    billZip = account.getZip();
    billCountry = account.getCountry();

    totalPrice = cart.getSubTotal();

    creditCard = "999 9999 9999 9999";
    expiryDate = "12/03";
    cardType = "Visa";
    courier = "UPS";
    locale = "CA";
    status = "P";

    Iterator<CartItem> i = cart.getAllCartItems();
    while (i.hasNext()) {
      CartItem cartItem = i.next();
      addLineItem(cartItem);
    }

  }

  /**
   * Add line item.
   *
   * @param cartItem
   *          the cart item
   */
  public void addLineItem(CartItem cartItem) {
    LineItem lineItem = new LineItem(lineItems.size() + 1, cartItem);
    addLineItem(lineItem);
  }

  /**
   * Add line item.
   *
   * @param lineItem
   *          the line item
   */
  public void addLineItem(LineItem lineItem) {
    lineItems.add(lineItem);
  }

}
