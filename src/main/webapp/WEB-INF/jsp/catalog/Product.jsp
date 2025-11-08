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

<jsp:useBean id="catalog"
             class="org.mybatis.jpetstore.web.actions.CatalogActionBean" />

<div id="BackLink">
    <%--  만약 카테고리 아이디가 ALL로 들어오면 Return to Main Menu로 뜨게끔 조건문 추가  --%>
    <c:choose>
        <c:when test="${actionBean.product.categoryId == 'ALL'}">
            <stripes:link beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
                Return to Main Menu
            </stripes:link>
        </c:when>

        <c:otherwise>
            <stripes:link beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean" event="viewCategory">
                <stripes:param name="categoryId" value="${actionBean.product.categoryId}" />
                Return to ${actionBean.product.categoryId}
            </stripes:link>
        </c:otherwise>
    </c:choose>
</div>

<div id="Catalog">

    <h2>${actionBean.product.name}</h2>

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
                <td>
                    <%-- 1. 기존 stripes:link에 CSS 클래스 "item-link"를 추가합니다. --%>
                    <stripes:link
                            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                            event="viewItem"
                            class="item-link"> <%-- item-link 추가 --%>
                        <stripes:param name="itemId" value="${item.itemId}" />
                        ${item.itemId}
                        <%-- 2. 숨겨진 팝업 <div>를 링크 안에 추가합니다. --%>
                        <div class="image-popup">
                            <img src="/jpetstore/images/placeholder.gif" alt="Item Image" />
                        </div>
                        <%-- 3. JavaScript가 이미지 경로를 찾을 수 있도록 상품 설명을 숨겨둡니다. --%>
                        <span class="popup-data" style="display: none;">${item.product.description}</span>
                    </stripes:link>
                </td>
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
        <tr>
            <td>
            </td>
        </tr>
    </table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>





