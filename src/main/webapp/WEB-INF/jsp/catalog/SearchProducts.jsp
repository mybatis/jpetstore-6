<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	Return to Main Menu
</stripes:link></div>

<div id="Catalog">

<table>
	<tr>
		<th>&nbsp;</th>
		<th>Product ID</th>
		<th>Name</th>
	</tr>
	<c:forEach var="product" items="${actionBean.productList}">
		<tr>
			<td><stripes:link
				beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
				event="viewProduct">
				<stripes:param name="productId" value="${product.productId}" />
				${product.description}
			</stripes:link></td>
			<td><b> <stripes:link
				beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
				event="viewProduct">
				<stripes:param name="productId" value="${product.productId}" />
				<font color="BLACK"> ${product.productId} </font>
			</stripes:link> </b></td>
			<td>${product.name}</td>
		</tr>
	</c:forEach>
	<tr>
		<td></td>
	</tr>

</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>




