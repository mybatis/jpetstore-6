<%--

       Copyright 2010-2026 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          https://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>
<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><a href="${pageContext.request.contextPath}/catalog">Return to Main Menu</a></div>

<div id="Catalog">

<div id="Cart">

<h2>Shopping Cart</h2>
<form method="post" action="${pageContext.request.contextPath}/cart/update">
<table>
<tr>
<th><b>Item ID</b></th>
<th><b>Product ID</b></th>
<th><b>Description</b></th>
<th><b>In Stock?</b></th>
<th><b>Quantity</b></th>
<th><b>List Price</b></th>
<th><b>Total Cost</b></th>
<th>&nbsp;</th>
</tr>

<c:if test="${cart.numberOfItems == 0}">
<tr>
<td colspan="8"><b>Your cart is empty.</b></td>
</tr>
</c:if>

<c:forEach var="cartItem" items="${cart.cartItemList}">
<tr>
<td><a href="${pageContext.request.contextPath}/catalog/viewItem?itemId=${cartItem.item.itemId}">${cartItem.item.itemId}</a></td>
<td>${cartItem.item.product.productId}</td>
<td>${cartItem.item.attribute1} ${cartItem.item.attribute2}
${cartItem.item.attribute3} ${cartItem.item.attribute4}
${cartItem.item.attribute5} ${cartItem.item.product.name}</td>
<td>${cartItem.inStock}</td>
<td><input type="text" size="3" name="${cartItem.item.itemId}" value="${cartItem.quantity}" /></td>
<td>$<fmt:formatNumber value="${cartItem.item.listPrice}"
pattern="#,##0.00" /></td>
<td>$<fmt:formatNumber value="${cartItem.total}"
pattern="#,##0.00" /></td>
<td><a class="Button" href="${pageContext.request.contextPath}/cart/removeItem?workingItemId=${cartItem.item.itemId}">Remove</a></td>
</tr>
</c:forEach>
<tr>
<td colspan="7">Sub Total: $<fmt:formatNumber
value="${cart.subTotal}" pattern="#,##0.00" />
<input type="submit" value="Update Cart" /></td>
<td>&nbsp;</td>
</tr>
</table>

</form> <c:if test="${cart.numberOfItems > 0}">
<a class="Button" href="${pageContext.request.contextPath}/order/new">Proceed to Checkout</a>
</c:if></div>

<div id="MyList">
  <c:if test="${sessionScope.accountBean != null}">
<c:if test="${sessionScope.accountBean.authenticated}">
  <c:if test="${sessionScope.accountBean.account.listOption}">
    <%@ include file="IncludeMyList.jsp"%>
      </c:if>
</c:if>
  </c:if>
</div>

<div id="Separator">&nbsp;</div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
