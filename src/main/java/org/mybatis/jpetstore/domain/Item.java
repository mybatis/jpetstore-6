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

/**
 * The Class Item.
 *
 * @author Eduardo Macarron
 */
public class Item implements Serializable {

  /** The serial version uid. */
  private static final long serialVersionUID = -2159121673445254631L;

  /** The item id. */
  private String itemId;
  /** The list price. */
  private BigDecimal listPrice;
  /** The unit cost. */
  private BigDecimal unitCost;
  /** The supplier id. */
  private int supplierId;
  /** The status. */
  private String status;
  /** The attribute1. */
  private String attribute1;
  /** The attribute2. */
  private String attribute2;
  /** The attribute3. */
  private String attribute3;
  /** The attribute4. */
  private String attribute4;
  /** The attribute5. */
  private String attribute5;
  /** The product. */
  private Product product;
  /** The quantity. */
  private int quantity;

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
    this.itemId = itemId.trim();
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
  }

  /**
   * Gets the product.
   *
   * @return the product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Sets the product.
   *
   * @param product
   *          the product
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Gets the supplier id.
   *
   * @return the supplier id
   */
  public int getSupplierId() {
    return supplierId;
  }

  /**
   * Sets the supplier id.
   *
   * @param supplierId
   *          the supplier id
   */
  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  /**
   * Gets the list price.
   *
   * @return the list price
   */
  public BigDecimal getListPrice() {
    return listPrice;
  }

  /**
   * Sets the list price.
   *
   * @param listPrice
   *          the list price
   */
  public void setListPrice(BigDecimal listPrice) {
    this.listPrice = listPrice;
  }

  /**
   * Gets the unit cost.
   *
   * @return the unit cost
   */
  public BigDecimal getUnitCost() {
    return unitCost;
  }

  /**
   * Sets the unit cost.
   *
   * @param unitCost
   *          the unit cost
   */
  public void setUnitCost(BigDecimal unitCost) {
    this.unitCost = unitCost;
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
   * Gets the attribute1.
   *
   * @return the attribute1
   */
  public String getAttribute1() {
    return attribute1;
  }

  /**
   * Sets the attribute1.
   *
   * @param attribute1
   *          the attribute1
   */
  public void setAttribute1(String attribute1) {
    this.attribute1 = attribute1;
  }

  /**
   * Gets the attribute2.
   *
   * @return the attribute2
   */
  public String getAttribute2() {
    return attribute2;
  }

  /**
   * Sets the attribute2.
   *
   * @param attribute2
   *          the attribute2
   */
  public void setAttribute2(String attribute2) {
    this.attribute2 = attribute2;
  }

  /**
   * Gets the attribute3.
   *
   * @return the attribute3
   */
  public String getAttribute3() {
    return attribute3;
  }

  /**
   * Sets the attribute3.
   *
   * @param attribute3
   *          the attribute3
   */
  public void setAttribute3(String attribute3) {
    this.attribute3 = attribute3;
  }

  /**
   * Gets the attribute4.
   *
   * @return the attribute4
   */
  public String getAttribute4() {
    return attribute4;
  }

  /**
   * Sets the attribute4.
   *
   * @param attribute4
   *          the attribute4
   */
  public void setAttribute4(String attribute4) {
    this.attribute4 = attribute4;
  }

  /**
   * Gets the attribute5.
   *
   * @return the attribute5
   */
  public String getAttribute5() {
    return attribute5;
  }

  /**
   * Sets the attribute5.
   *
   * @param attribute5
   *          the attribute5
   */
  public void setAttribute5(String attribute5) {
    this.attribute5 = attribute5;
  }

  @Override
  public String toString() {
    return "(" + getItemId() + "-" + getProduct().getProductId() + ")";
  }

}
