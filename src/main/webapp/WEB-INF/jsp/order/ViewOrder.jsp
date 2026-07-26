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

<table>
<tr>
<th align="center" colspan="2">Order #${order.orderId}
<fmt:formatDate value="${order.orderDate}"
pattern="yyyy/MM/dd hh:mm:ss" /></th>
</tr>
<tr>
<th colspan="2">Payment Details</th>
</tr>
<tr>
<td>Card Type:</td>
<td><c:out value="${order.cardType}" /></td>
</tr>
<tr>
<td>Card Number:</td>
<td><c:out value="${order.creditCard}" /> * Fake
number!</td>
</tr>
<tr>
<td>Expiry Date (MM/YYYY):</td>
<td><c:out value="${order.expiryDate}" /></td>
</tr>
<tr>
<th colspan="2">Billing Address</th>
</tr>
<tr>
<td>First name:</td>
<td><c:out value="${order.billToFirstName}" /></td>
</tr>
<tr>
<td>Last name:</td>
<td><c:out value="${order.billToLastName}" /></td>
</tr>
<tr>
<td>Address 1:</td>
<td><c:out value="${order.billAddress1}" /></td>
</tr>
<tr>
<td>Address 2:</td>
<td><c:out value="${order.billAddress2}" /></td>
</tr>
<tr>
<td>City:</td>
<td><c:out value="${order.billCity}" /></td>
</tr>
<tr>
<td>State:</td>
<td><c:out value="${order.billState}" /></td>
</tr>
<tr>
<td>Zip:</td>
<td><c:out value="${order.billZip}" /></td>
</tr>
<tr>
<td>Country:</td>
<td><c:out value="${order.billCountry}" /></td>
</tr>
<tr>
<th colspan="2">Shipping Address</th>
</tr>
<tr>
<td>First name:</td>
<td><c:out value="${order.shipToFirstName}" /></td>
</tr>
<tr>
<td>Last name:</td>
<td><c:out value="${order.shipToLastName}" /></td>
</tr>
<tr>
<td>Address 1:</td>
<td><c:out value="${order.shipAddress1}" /></td>
</tr>
<tr>
<td>Address 2:</td>
<td><c:out value="${order.shipAddress2}" /></td>
</tr>
<tr>
<td>City:</td>
<td><c:out value="${order.shipCity}" /></td>
</tr>
<tr>
<td>State:</td>
<td><c:out value="${order.shipState}" /></td>
</tr>
<tr>
<td>Zip:</td>
<td><c:out value="${order.shipZip}" /></td>
</tr>
<tr>
<td>Country:</td>
<td><c:out value="${order.shipCountry}" /></td>
</tr>
<tr>
<td>Courier:</td>
<td><c:out value="${order.courier}" /></td>
</tr>
<tr>
<td colspan="2">Status: <c:out value="${order.status}" /></td>
</tr>
<tr>
<td colspan="2">
<table>
<tr>
<th>Item ID</th>
<th>Description</th>
<th>Quantity</th>
<th>Price</th>
<th>Total Cost</th>
</tr>
<c:forEach var="lineItem" items="${order.lineItems}">
<tr>
<td><a href="${pageContext.request.contextPath}/catalog/viewItem?itemId=${lineItem.item.itemId}">${lineItem.item.itemId}</a></td>
<td><c:if test="${lineItem.item != null}">
${lineItem.item.attribute1}
${lineItem.item.attribute2}
${lineItem.item.attribute3}
${lineItem.item.attribute4}
${lineItem.item.attribute5}
${lineItem.item.product.name}
</c:if> <c:if test="${lineItem.item == null}">
<i>{description unavailable}</i>
</c:if></td>

<td>${lineItem.quantity}</td>
<td>$<fmt:formatNumber value="${lineItem.unitPrice}"
pattern="#,##0.00" /></td>
<td>$<fmt:formatNumber value="${lineItem.total}"
pattern="#,##0.00" /></td>
</tr>
</c:forEach>
<tr>
<th colspan="5">Total: $<fmt:formatNumber
value="${order.totalPrice}" pattern="#,##0.00" /></th>
</tr>
</table>
</td>
</tr>

</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>
