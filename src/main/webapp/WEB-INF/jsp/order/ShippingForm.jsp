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
<input type="hidden" name="confirmed" value="false" />

<table>
<tr>
<th colspan=2>Shipping Address</th>
</tr>

<tr>
<td>First name:</td>
<td><input type="text" name="shipToFirstName" value="${order.shipToFirstName}" /></td>
</tr>
<tr>
<td>Last name:</td>
<td><input type="text" name="shipToLastName" value="${order.shipToLastName}" /></td>
</tr>
<tr>
<td>Address 1:</td>
<td><input type="text" size="40" name="shipAddress1" value="${order.shipAddress1}" /></td>
</tr>
<tr>
<td>Address 2:</td>
<td><input type="text" size="40" name="shipAddress2" value="${order.shipAddress2}" /></td>
</tr>
<tr>
<td>City:</td>
<td><input type="text" name="shipCity" value="${order.shipCity}" /></td>
</tr>
<tr>
<td>State:</td>
<td><input type="text" size="4" name="shipState" value="${order.shipState}" /></td>
</tr>
<tr>
<td>Zip:</td>
<td><input type="text" size="10" name="shipZip" value="${order.shipZip}" /></td>
</tr>
<tr>
<td>Country:</td>
<td><input type="text" size="15" name="shipCountry" value="${order.shipCountry}" /></td>
</tr>


</table>

<input type="submit" name="newOrder" value="Continue" />

</form></div>

<%@ include file="../common/IncludeBottom.jsp"%>
