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
package org.mybatis.jpetstore.web.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.service.OrderService;

/**
 * The Class OrderActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class OrderActionBean extends AbstractActionBean {

  /** The serial version uid. */
  private static final long serialVersionUID = -6171288227470176272L;

  /** The confirm order. */
  private static final String CONFIRM_ORDER = "/WEB-INF/jsp/order/ConfirmOrder.jsp";
  /** The list orders. */
  private static final String LIST_ORDERS = "/WEB-INF/jsp/order/ListOrders.jsp";
  /** The new order. */
  private static final String NEW_ORDER = "/WEB-INF/jsp/order/NewOrderForm.jsp";
  /** The shipping. */
  private static final String SHIPPING = "/WEB-INF/jsp/order/ShippingForm.jsp";
  /** The view order. */
  private static final String VIEW_ORDER = "/WEB-INF/jsp/order/ViewOrder.jsp";

  /** The card type list. */
  private static final List<String> CARD_TYPE_LIST;

  /** The order service. */
  @SpringBean
  private transient OrderService orderService;

  /** The order. */
  private Order order = new Order();
  /** The shipping address required. */
  private boolean shippingAddressRequired;
  /** The confirmed. */
  private boolean confirmed;
  /** The order list. */
  private List<Order> orderList;

  static {
    CARD_TYPE_LIST = Collections.unmodifiableList(Arrays.asList("Visa", "MasterCard", "American Express"));
  }

  /**
   * Gets the order id.
   *
   * @return the order id
   */
  public int getOrderId() {
    return order.getOrderId();
  }

  /**
   * Sets the order id.
   *
   * @param orderId
   *          the order id
   */
  public void setOrderId(int orderId) {
    order.setOrderId(orderId);
  }

  /**
   * Gets the order.
   *
   * @return the order
   */
  public Order getOrder() {
    return order;
  }

  /**
   * Sets the order.
   *
   * @param order
   *          the order
   */
  public void setOrder(Order order) {
    this.order = order;
  }

  /**
   * Checks if is shipping address required.
   *
   * @return true, if successful
   */
  public boolean isShippingAddressRequired() {
    return shippingAddressRequired;
  }

  /**
   * Sets the shipping address required.
   *
   * @param shippingAddressRequired
   *          the shipping address required
   */
  public void setShippingAddressRequired(boolean shippingAddressRequired) {
    this.shippingAddressRequired = shippingAddressRequired;
  }

  /**
   * Checks if is confirmed.
   *
   * @return true, if successful
   */
  public boolean isConfirmed() {
    return confirmed;
  }

  /**
   * Sets the confirmed.
   *
   * @param confirmed
   *          the confirmed
   */
  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  /**
   * Gets the credit card types.
   *
   * @return the credit card types
   */
  public List<String> getCreditCardTypes() {
    return CARD_TYPE_LIST;
  }

  /**
   * Gets the order list.
   *
   * @return the order list
   */
  public List<Order> getOrderList() {
    return orderList;
  }

  /**
   * List orders.
   *
   * @return the resolution
   */
  public Resolution listOrders() {
    HttpSession session = context.getRequest().getSession();
    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");
    orderList = orderService.getOrdersByUsername(accountBean.getAccount().getUsername());
    return new ForwardResolution(LIST_ORDERS);
  }

  /**
   * New order form.
   *
   * @return the resolution
   */
  public Resolution newOrderForm() {
    HttpSession session = context.getRequest().getSession();
    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("/actions/Account.action");
    CartActionBean cartBean = (CartActionBean) session.getAttribute("/actions/Cart.action");

    clear();
    if (accountBean == null || !accountBean.isAuthenticated()) {
      setMessage("You must sign on before attempting to check out.  Please sign on and try checking out again.");
      return new ForwardResolution(AccountActionBean.class);
    } else if (cartBean != null) {
      order.initOrder(accountBean.getAccount(), cartBean.getCart());
      return new ForwardResolution(NEW_ORDER);
    } else {
      setMessage("An order could not be created because a cart could not be found.");
      return new ForwardResolution(ERROR);
    }
  }

  /**
   * New order.
   *
   * @return the resolution
   */
  public Resolution newOrder() {
    HttpSession session = context.getRequest().getSession();

    if (shippingAddressRequired) {
      shippingAddressRequired = false;
      return new ForwardResolution(SHIPPING);
    } else if (!isConfirmed()) {
      return new ForwardResolution(CONFIRM_ORDER);
    } else if (getOrder() != null) {

      orderService.insertOrder(order);

      CartActionBean cartBean = (CartActionBean) session.getAttribute("/actions/Cart.action");
      cartBean.clear();

      setMessage("Thank you, your order has been submitted.");

      return new ForwardResolution(VIEW_ORDER);
    } else {
      setMessage("An error occurred processing your order (order was null).");
      return new ForwardResolution(ERROR);
    }
  }

  /**
   * View order.
   *
   * @return the resolution
   */
  public Resolution viewOrder() {
    HttpSession session = context.getRequest().getSession();

    AccountActionBean accountBean = (AccountActionBean) session.getAttribute("accountBean");

    order = orderService.getOrder(order.getOrderId());

    if (accountBean.getAccount().getUsername().equals(order.getUsername())) {
      return new ForwardResolution(VIEW_ORDER);
    } else {
      order = null;
      setMessage("You may only view your own orders.");
      return new ForwardResolution(ERROR);
    }
  }

  /**
   * Clear.
   */
  public void clear() {
    order = new Order();
    shippingAddressRequired = false;
    confirmed = false;
    orderList = null;
  }

}
