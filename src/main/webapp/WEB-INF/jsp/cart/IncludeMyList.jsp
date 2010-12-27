<c:if test="${!empty accountBean.myList}">
	<p>Pet Favorites <br />
	Shop for more of your favorite pets here.</p>
	<ul>
		<c:forEach var="product" items="${accountBean.myList}">
			<li><stripes:link
				beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
				event="viewProduct">
				<stripes:param name="productId" value="${product.productId}" />
			${product.name}
		</stripes:link> (${product.productId})</li>
		</c:forEach>
	</ul>

</c:if>
