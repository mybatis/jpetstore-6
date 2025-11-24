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

<button class="compare-btn-fixed" id="compareBtn" onclick="openComparisonPopup()" disabled>
    Compare
</button>

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
            <table class="itemList"> <tr>
                <th>Item ID</th>
                <th>Product ID</th>
                <th>Description</th>
                <th>List Price</th>
                <th>&nbsp;</th>
            </tr>
                <c:forEach var="item" items="${actionBean.recommendedItems}">
                    <tr>
                        <td>
                                <%-- ★★★ [수정 1] 팝업을 위한 구조 변경 시작 ★★★ --%>
                            <stripes:link
                                    beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                                    event="viewItem"
                                    class="item-link"> <stripes:param name="itemId" value="${item.itemId}" />
                                ${item.itemId}

                                <div class="image-popup">
                                    <img src="/jpetstore/images/placeholder.gif" alt="Item Image" />
                                </div>

                                <span class="popup-data" style="display: none;">
                    <c:out value="${item.product.description}" escapeXml="false" />
                </span>

                            </stripes:link>
                                <%-- ★★★ [수정 1] 팝업을 위한 구조 변경 끝 ★★★ --%>
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
            </table>
        </c:if>
    </c:if>

</div>

<%-- ★★★ [수정 2] 이미지 팝업 처리 스크립트 추가 ★★★ --%>
<script>
    // 이미지 경로 추출 함수 (Product.jsp와 동일)
    function extractImagePath(desc) {
        if (!desc) return '/jpetstore/images/placeholder.gif';
        const match = desc.match(/<img src="([^"]+)">/);
        if (match && match[1]) {
            return match[1].replace('../', '/jpetstore/');
        }
        return '/jpetstore/images/placeholder.gif';
    }

    document.addEventListener('DOMContentLoaded', function() {
        const links = document.querySelectorAll('.item-link');

        links.forEach(link => {
            const popup = link.querySelector('.image-popup');
            const dataSpan = link.querySelector('.popup-data');
            const imgTag = popup ? popup.querySelector('img') : null;

            if (popup && dataSpan && imgTag) {
                // 이미지 경로 설정
                const description = dataSpan.innerHTML;
                imgTag.src = extractImagePath(description);
            }
        });
    });
</script>

<%@ include file="../common/IncludeBottom.jsp"%>