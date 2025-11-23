<%--

       Copyright 2010-2022 the original author or authors.

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

<!-- ★★★ 1. [추가] 비교하기 버튼 (이게 있어야 팝업을 띄웁니다) ★★★ -->
<button class="compare-btn-fixed" id="compareBtn" onclick="openComparisonPopup()" disabled>
    Compare
</button>

<jsp:useBean id="catalog"
             class="org.mybatis.jpetstore.web.actions.CatalogActionBean" />

<div id="BackLink"><stripes:link
        beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
        event="viewCategory">
    <stripes:param name="categoryId"
                   value="${actionBean.product.categoryId}" />
    Return to ${actionBean.product.categoryId}
</stripes:link></div>

<div id="Catalog">

    <h2>${actionBean.product.name}</h2>

    <%-- ★★★ 2. [수정] class="itemList" 추가 (이게 있어야 체크박스가 생깁니다) ★★★ --%>
    <table class="itemList">
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
                        <%-- 팝업 기능을 위한 링크 구조 (님 코드 유지) --%>
                    <stripes:link
                            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                            event="viewItem"
                            class="item-link">
                        <stripes:param name="itemId" value="${item.itemId}" />
                        ${item.itemId}

                        <%-- 팝업 영역 --%>
                        <div class="image-popup">
                            <img src="/jpetstore/images/placeholder.gif" alt="Item Image" />
                                <%-- 추천 텍스트 영역 --%>
                            <div class="recommend-text"></div>
                        </div>

                        <%-- 데이터 숨김 영역 --%>
                        <span class="popup-data" style="display: none;" data-id="${item.itemId}">
                     <c:out value="${item.product.description}" escapeXml="false" />
                 </span>
                    </stripes:link>
                </td>
                <td>${item.product.productId}</td>
                <td>${item.attribute1} ${item.attribute2} ${item.attribute3}
                        ${item.attribute4} ${item.attribute5} ${actionBean.product.name}</td>
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




