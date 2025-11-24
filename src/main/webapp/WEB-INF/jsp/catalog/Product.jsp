<%@ include file="../common/IncludeTop.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- ★★★ 1. 비교하기 버튼 (우측 상단 고정) ★★★ -->
<button class="compare-btn-fixed" id="compareBtn" onclick="openComparisonPopup()" disabled>
    Compare
</button>

<jsp:useBean id="catalog"
             class="org.mybatis.jpetstore.web.actions.CatalogActionBean" />

<div id="BackLink">
    <%-- ★★★ 2. [복구] "ALL" 카테고리일 때 메인 메뉴로 돌아가는 로직 ★★★ --%>
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
                        <%-- 팝업 링크 구조 --%>
                    <stripes:link
                            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                            event="viewItem"
                            class="item-link">
                        <stripes:param name="itemId" value="${item.itemId}" />
                        ${item.itemId}

                        <%-- 이미지 팝업 --%>
                        <div class="image-popup">
                            <img src="/jpetstore/images/placeholder.gif" alt="Item Image" />
                            <div class="recommend-text"></div>
                        </div>

                        <%-- 데이터 숨김 (이미지 경로용) --%>
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
        <%-- ★★★ 3. [삭제됨] 여기에 있던 빈 <tr> 태그를 제거했습니다. (이상한 체크박스 원인) ★★★ --%>
    </table>

</div>

<script>
    // 이미지 경로 추출 함수
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

        // ★★★ 4. [안전장치] 데이터가 없어도 스크립트가 죽지 않도록 수정 ★★★
        // 세션 데이터가 있으면 쓰고, 없으면 빈 배열 [] 사용
        let recommendedIds = [];
        try {
            // JSP EL이 빈 문자열을 출력할 경우를 대비해 따옴표로 감싸고 파싱 시도
            const jsonStr = '${sessionScope.recommendationJson}';
            if (jsonStr && jsonStr.trim() !== '') {
                recommendedIds = JSON.parse(jsonStr);
            }
        } catch (e) {
            console.log('No recommendation data or parse error:', e);
        }

        // JSON이 객체 배열([{"productId":"..."}]) 형태일 경우 ID만 추출하는 로직 추가
        if (recommendedIds.length > 0 && typeof recommendedIds[0] === 'object') {
            recommendedIds = recommendedIds.map(item => item.productId);
        }

        links.forEach(link => {
            const popup = link.querySelector('.image-popup');
            const dataSpan = link.querySelector('.popup-data');
            const imgTag = popup.querySelector('img');
            const recommendDiv = popup.querySelector('.recommend-text');

            if (popup && dataSpan && imgTag) {
                // 이미지 설정
                const description = dataSpan.innerHTML;
                imgTag.src = extractImagePath(description);

                // 추천 배지 설정
                const currentItemId = dataSpan.getAttribute('data-id');

                // 안전하게 문자열 포함 여부 확인
                let isRecommended = false;
                if (Array.isArray(recommendedIds)) {
                    // ID가 포함되어 있는지 확인
                    isRecommended = recommendedIds.includes(currentItemId);
                }

                if (isRecommended) {
                    recommendDiv.innerHTML = '<div class="recommend-badge" style="background:#dff0d8; color:#3c763d; padding:5px; margin-top:5px; border-radius:4px; font-weight:bold;">👍 AI 추천 상품</div>';
                } else {
                    // 추천 아님 (비워두기)
                    recommendDiv.innerHTML = '';
                }
            }

            // 마우스 오버 이벤트
            link.addEventListener('mouseenter', function() {
                popup.style.display = 'block';
            });
            link.addEventListener('mouseleave', function() {
                popup.style.display = 'none';
            });
        });
    });
</script>

<%@ include file="../common/IncludeBottom.jsp"%>