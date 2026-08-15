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
import java.util.Optional;

/**
 * The Class LineItem.
 *
 * @author Eduardo Macarron
 */
public class LineItem implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = 6804536240033522156L;

  /** The order id. */
  private int orderId;
  /** The line number. */
  private int lineNumber;
  /** The quantity. */
  private int quantity;
  /** The item id. */
  private String itemId;
  /** The unit price. */
  private BigDecimal unitPrice;
  /** The item. */
  private Item item;
  /** The total. */
  private BigDecimal total;

  /**
   * Instantiates a new line item.
   */
  public LineItem() {
  }

  /**
   * Instantiates a new line item.
   *
   * @param lineNumber
   *          the line number
   * @param cartItem
   *          the cart item
   */
  public LineItem(int lineNumber, CartItem cartItem) {
    this.lineNumber = lineNumber;
    this.quantity = cartItem.getQuantity();
    this.itemId = cartItem.getItem().getItemId();
    this.unitPrice = cartItem.getItem().getListPrice();
    this.item = cartItem.getItem();
    calculateTotal();
  }

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
   * Gets the line number.
   *
   * @return the line number
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Sets the line number.
   *
   * @param lineNumber
   *          the line number
   */
  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * Gets the item id.
   *
   * @return the item id
   */
  public String getItemId() {
    return itemId;
  }

  /**
   * Sets the item id.
   *
   * @param itemId
   *          the item id
   */
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  /**
   * Gets the unit price.
   *
   * @return the unit price
   */
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  /**
   * Sets the unit price.
   *
   * @param unitprice
   *          the unitprice
   */
  public void setUnitPrice(BigDecimal unitprice) {
    this.unitPrice = unitprice;
  }

  /**
   * Gets the total.
   *
   * @return the total
   */
  public BigDecimal getTotal() {
    return total;
  }

  /**
   * Gets the item.
   *
   * @return the item
   */
  public Item getItem() {
    return item;
  }

  /**
   * Sets the item.
   *
   * @param item
   *          the item
   */
  public void setItem(Item item) {
    this.item = item;
    calculateTotal();
  }

  /**
   * Gets the quantity.
   *
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity.
   *
   * @param quantity
   *          the quantity
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
    calculateTotal();
  }

  /**
   * Calculate total.
   */
  private void calculateTotal() {
    total = Optional.ofNullable(item).map(Item::getListPrice).map(v -> v.multiply(new BigDecimal(quantity)))
        .orElse(null);
  }

}
