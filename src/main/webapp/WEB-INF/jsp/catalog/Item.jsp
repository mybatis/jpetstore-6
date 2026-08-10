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

<div id="BackLink"><a href="${pageContext.request.contextPath}/catalog/viewProduct?productId=${product.productId}">Return to ${product.productId}</a></div>

<div id="Catalog">

<table>
<tr>
<td>${product.description}</td>
</tr>
<tr>
<td><b> ${item.itemId} </b></td>
</tr>
<tr>
<td><b><font size="4"> ${item.attribute1}
${item.attribute2} ${item.attribute3}
${item.attribute4} ${item.attribute5}
${product.name} </font></b></td>
</tr>
<tr>
<td>${product.name}</td>
</tr>
<tr>
<td><c:if test="${item.quantity <= 0}">
        Back ordered.
      </c:if> <c:if test="${item.quantity > 0}">
      ${item.quantity} in stock.
  </c:if></td>
</tr>
<tr>
<td><fmt:formatNumber value="${item.listPrice}"
pattern="$#,##0.00" /></td>
</tr>

<tr>
<td><a class="Button" href="${pageContext.request.contextPath}/cart/addItem?workingItemId=${item.itemId}">Add to Cart</a></td>
</tr>
</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>
