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
<%@ include file="../common/IncludeTop.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div id="Catalog">

  <h2>AI Recommendations</h2>

  <c:if test="${!sessionScope.accountBean.authenticated}">
    <div class="Message">Please sign in to view recommendations.</div>
  </c:if>

  <c:if test="${sessionScope.accountBean.authenticated}">
    <c:if test="${empty actionBean.recommendedItems}">
      <div class="Message">No recommendations available.</div>
    </c:if>
    <c:if test="${not empty actionBean.recommendedItems}">
      <table>
        <tr>
          <th>Item ID</th>
          <th>Product ID</th>
          <th>Description</th>
          <th>List Price</th>
          <th>&nbsp;</th>
        </tr>
        <c:forEach var="item" items="${actionBean.recommendedItems}">
          <tr>
            <td><stripes:link
                beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                event="viewItem">
                <stripes:param name="itemId" value="${item.itemId}" />
                ${item.itemId}
            </stripes:link></td>
            <td>${item.product.productId}</td>
            <td>${item.attribute1} ${item.attribute2} ${item.attribute3}
            ${item.attribute4} ${item.attribute5} ${item.product.name}</td>
            <td><fmt:formatNumber value="${item.listPrice}"
              pattern="$#,##0.00" /></td>
            <td><stripes:link class="Button"
                beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
                event="addItemToCart">
                <stripes:param name="workingItemId" value="${item.itemId}" />
                Add to Cart
            </stripes:link></td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </c:if>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>
