<%--

       Copyright 2010-2025 the original author or authors.

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
<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>

<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	Return to Main Menu</stripes:link></div>

<div id="Catalog">

<h2>All Products</h2>

<table>
	<tr>
		<th>Item ID</th>
		<th>Product ID</th>
		<th>Description</th>
		<th>List Price</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="item" items="${actionBean.itemList}">
		<tr>
			<td><stripes:link
				beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
				event="viewItem">
				<stripes:param name="itemId" value="${item.itemId}" />
				${item.itemId}
			</stripes:link></td>
			<td>${item.product.productId}</td>
			<td>
				${item.attribute1} ${item.attribute2} ${item.attribute3}
				${item.attribute4} ${item.attribute5} ${item.product.name}
			</td>
			<td><fmt:formatNumber value="${item.listPrice}"
				pattern="$#,##0.00" /></td>
			<td>
				<stripes:link
					beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
					event="addItemToCart">
					<stripes:param name="workingItemId" value="${item.itemId}" />
					Add to Cart
				</stripes:link>
			</td>
		</tr>
	</c:forEach>
</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>