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
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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

<!-- AI Recommendations Section -->
<c:if test="${not empty actionBean.aiRecommendations}">
	<div id="AIRecommendations" style="margin-top: 30px; padding: 20px; background-color: #f8f9fa; border-radius: 8px; border: 2px solid #007bff;">
		<h3 style="color: #007bff; margin-top: 0;">
			🤖 AI 추천 상품
		</h3>
		<p style="color: #6c757d; font-size: 0.9em; margin-bottom: 15px;">
			이 상품과 함께 구매하면 좋은 제품을 AI가 추천해드립니다
		</p>
		<ul style="list-style-type: none; padding-left: 0;">
			<c:forEach var="recommendation" items="${actionBean.aiRecommendations}">
				<li style="margin-bottom: 10px; padding: 10px; background-color: white; border-radius: 5px; border-left: 4px solid #007bff;">
					${recommendation}
				</li>
			</c:forEach>
		</ul>
		<p style="color: #6c757d; font-size: 0.8em; margin-top: 15px; margin-bottom: 0;">
			💡 <i>Powered by Google Gemini AI</i>
		</p>
	</div>
</c:if>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>



