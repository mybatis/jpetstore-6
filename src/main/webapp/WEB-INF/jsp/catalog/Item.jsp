<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewProduct">
	<stripes:param name="productId" value="${actionBean.product.productId}" />
	Return to ${actionBean.product.productId}
</stripes:link></div>

<div id="Catalog">

<table>
	<tr>
		<td>${actionBean.product.description}</td>
	</tr>
	<tr>
		<td><b> ${actionBean.item.itemId} </b></td>
	</tr>
	<tr>
		<td><b><font size="4"> ${actionBean.item.attribute1}
		${actionBean.item.attribute2} ${actionBean.item.attribute3}
		${actionBean.item.attribute4} ${actionBean.item.attribute5}
		${actionBean.product.name} </font></b></td>
	</tr>
	<tr>
		<td>${actionBean.product.name}</td>
	</tr>
	<tr>
		<td><c:if test="${actionBean.item.quantity <= 0}">
        Back ordered.
      </c:if> <c:if test="${actionBean.item.quantity > 0}">
      	${actionBean.item.quantity} in stock.
	  </c:if></td>
	</tr>
	<tr>
		<td><fmt:formatNumber value="${actionBean.item.listPrice}"
			pattern="$#,##0.00" /></td>
	</tr>

	<tr>
		<td><stripes:link class="Button"
			beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
			event="addItemToCart">
			<stripes:param name="workingItemId" value="${actionBean.item.itemId}" />
       	Add to Cart
       </stripes:link></td>
	</tr>
</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>



