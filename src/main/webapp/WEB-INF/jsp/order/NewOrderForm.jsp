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

<div id="Catalog">
<form method="post" action="${pageContext.request.contextPath}/order/new">

<table>
<tr>
<th colspan=2>Payment Details</th>
</tr>
<tr>
<td>Card Type:</td>
<td><select name="cardType">
<c:forEach var="type" items="${creditCardTypes}">
<option value="${type}">${type}</option>
</c:forEach>
</select></td>
</tr>
<tr>
<td>Card Number:</td>
<td><input type="text" name="creditCard" value="${order.creditCard}" /> * Use a fake number!</td>
</tr>
<tr>
<td>Expiry Date (MM/YYYY):</td>
<td><input type="text" name="expiryDate" value="${order.expiryDate}" /></td>
</tr>
<tr>
<th colspan=2>Billing Address</th>
</tr>

<tr>
<td>First name:</td>
<td><input type="text" name="billToFirstName" value="${order.billToFirstName}" /></td>
</tr>
<tr>
<td>Last name:</td>
<td><input type="text" name="billToLastName" value="${order.billToLastName}" /></td>
</tr>
<tr>
<td>Address 1:</td>
<td><input type="text" size="40" name="billAddress1" value="${order.billAddress1}" /></td>
</tr>
<tr>
<td>Address 2:</td>
<td><input type="text" size="40" name="billAddress2" value="${order.billAddress2}" /></td>
</tr>
<tr>
<td>City:</td>
<td><input type="text" name="billCity" value="${order.billCity}" /></td>
</tr>
<tr>
<td>State:</td>
<td><input type="text" size="4" name="billState" value="${order.billState}" /></td>
</tr>
<tr>
<td>Zip:</td>
<td><input type="text" size="10" name="billZip" value="${order.billZip}" /></td>
</tr>
<tr>
<td>Country:</td>
<td><input type="text" size="15" name="billCountry" value="${order.billCountry}" /></td>
</tr>

<tr>
<td colspan=2><input type="checkbox" name="shippingAddressRequired" value="true" />
Ship to different address...</td>
</tr>

</table>

<input type="submit" value="Continue" />

</form></div>

<%@ include file="../common/IncludeBottom.jsp"%>
