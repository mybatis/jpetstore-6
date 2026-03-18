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

<div id="BackLink"><a href="${pageContext.request.contextPath}/catalog/viewCategory?categoryId=${product.categoryId}">Return to ${product.categoryId}</a></div>

<div id="Catalog">

<h2>${product.name}</h2>

<table>
<tr>
<th>Item ID</th>
<th>Product ID</th>
<th>Description</th>
<th>List Price</th>
<th>&nbsp;</th>
</tr>
<c:forEach var="item" items="${itemList}">
<tr>
<td><a href="${pageContext.request.contextPath}/catalog/viewItem?itemId=${item.itemId}">${item.itemId}</a></td>
<td>${item.product.productId}</td>
<td>${item.attribute1} ${item.attribute2} ${item.attribute3}
${item.attribute4} ${item.attribute5} ${product.name}</td>
<td><fmt:formatNumber value="${item.listPrice}"
pattern="$#,##0.00" /></td>
<td><a class="Button" href="${pageContext.request.contextPath}/cart/addItem?workingItemId=${item.itemId}">Add to Cart</a></td>
</tr>
</c:forEach>
<tr>
<td>
</td>
</tr>
</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>
