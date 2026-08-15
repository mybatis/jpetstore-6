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
 * The Class CartItem.
 *
 * @author Eduardo Macarron
 */
public class CartItem implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = 6620528781626504362L;

  /** The item. */
  private Item item;
  /** The quantity. */
  private int quantity;
  /** The in stock. */
  private boolean inStock;
  /** The total. */
  private BigDecimal total;

  /**
   * Checks if is in stock.
   *
   * @return true, if successful
   */
  public boolean isInStock() {
    return inStock;
  }

  /**
   * Sets the in stock.
   *
   * @param inStock
   *          the in stock
   */
  public void setInStock(boolean inStock) {
    this.inStock = inStock;
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
   * Increment quantity.
   */
  public void incrementQuantity() {
    quantity++;
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
