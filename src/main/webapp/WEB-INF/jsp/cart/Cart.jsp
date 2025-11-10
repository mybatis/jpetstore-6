<%--

       Copyright 2010-2023 the original author or authors.

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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="BackLink"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
	Return to Main Menu</stripes:link></div>

<div id="Catalog">

<div id="Cart">

<h2>Shopping Cart</h2>
<stripes:form
	beanclass="org.mybatis.jpetstore.web.actions.CartActionBean">
	<table>
		<tr>
			<th><b>Item ID</b></th>
			<th><b>Product ID</b></th>
			<th><b>Description</b></th>
			<th><b>In Stock?</b></th>
			<th><b>Quantity</b></th>
			<th><b>List Price</b></th>
			<th><b>Total Cost</b></th>
			<th>&nbsp;</th>
		</tr>

		<c:if test="${actionBean.cart.numberOfItems == 0}">
			<tr>
				<td colspan="8"><b>Your cart is empty.</b></td>
			</tr>
		</c:if>

		<c:forEach var="cartItem" items="${actionBean.cart.cartItems}">
			<tr>
                <%-- 1. 기존 stripes:link에 CSS 클래스 "item-link"를 추가합니다. --%>
				<td><stripes:link
					beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
					event="viewItem"
                    class="item-link"> <%-- item-link 추가 --%>
					<stripes:param name="itemId" value="${cartItem.item.itemId}" />
				    ${cartItem.item.itemId}
                    <%-- 2. 숨겨진 팝업 <div>를 링크 안에 추가합니다. --%>
                    <div class="image-popup">
                        <img src="/jpetstore/images/placeholder.gif" alt="Item Image" />
                    </div>
                    <%-- 3. JavaScript가 이미지 경로를 찾을 수 있도록 상품 설명을 숨겨둡니다. --%>
                    <span class="popup-data" style="display: none;"><c:out value="${cartItem.item.product.description}" escapeXml="false" /></span>
			  </stripes:link></td>
				<td>${cartItem.item.product.productId}</td>
				<td>${cartItem.item.attribute1} ${cartItem.item.attribute2}
				${cartItem.item.attribute3} ${cartItem.item.attribute4}
				${cartItem.item.attribute5} ${cartItem.item.product.name}</td>
				<td>${cartItem.inStock}</td>
				<td><stripes:text size="3" name="${cartItem.item.itemId}"
					value="${cartItem.quantity}" /></td>
				<td>$<fmt:formatNumber value="${cartItem.item.listPrice}"
					pattern="#,##0.00" /></td>
				<td>$<fmt:formatNumber value="${cartItem.total}"
					pattern="#,##0.00" /></td>
				<td><stripes:link class="Button"
					beanclass="org.mybatis.jpetstore.web.actions.CartActionBean"
					event="removeItemFromCart">
					<stripes:param name="workingItemId" value="${cartItem.item.itemId}" />
            	Remove
            </stripes:link></td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="7">Sub Total: $<fmt:formatNumber
				value="${actionBean.cart.subTotal}" pattern="#,##0.00" /> <stripes:submit
				name="updateCartQuantities" value="Update Cart" /></td>
			<td>&nbsp;</td>
		</tr>
	</table>

</stripes:form> <c:if test="${actionBean.cart.numberOfItems > 0}">
	<stripes:link class="Button"
		beanclass="org.mybatis.jpetstore.web.actions.OrderActionBean"
		event="newOrderForm">
      	Proceed to Checkout
      </stripes:link>
</c:if></div>

<div id="MyList">
  <c:if test="${sessionScope.accountBean != null}">
	<c:if test="${!sessionScope.accountBean.authenticated}">
	  <c:if test="${!empty sessionScope.accountBean.account.listOption}">
	    <%@ include file="IncludeMyList.jsp"%>
      </c:if>
	</c:if>
  </c:if>
</div>

<div id="Separator">&nbsp;</div>
</div>

<script>
    /**
     * JPetStore의 상품 설명(description) 문자열에서 이미지 경로를 추출하는 함수
     * (예: "<image src="../images/fish1.gif">Large")
     */
    function extractImagePath(desc) {
        if (!desc) {
            return '/jpetstore/images/placeholder.gif'; // null 또는 undefined 방지
        }
        // 정규표현식을 사용해 <img src="..."> 태그에서 경로만 추출
        const match = desc.match(/<img src="([^"]+)">/);
        if (match && match[1]) {
            // JPetStore의 상대 경로(../)를 웹 루트 절대 경로(/jpetstore/)로 변경
            return match[1].replace('../', '/jpetstore/');
        }
        return '/jpetstore/images/placeholder.gif'; // 이미지가 없는 상품 대비
    }

    /**
     * 페이지 로드가 완료되면, 모든 팝업 이미지의 경로를 '미리' 설정합니다.
     */
    document.addEventListener('DOMContentLoaded', function() {
        // .item-link 클래스를 가진 모든 링크를 찾습니다.
        const links = document.querySelectorAll('.item-link');

        links.forEach(link => {
            const popup = link.querySelector('.image-popup');
            const dataSpan = link.querySelector('.popup-data');
            const imgTag = popup.querySelector('img');

            if (popup && dataSpan && imgTag) {
                // 1. 숨겨진 <span>에서 HTML 문자열 전체를 읽어옵니다.
                const description = dataSpan.innerHTML;

                // 2. 설명에서 이미지 경로를 추출하여 <img> 태그의 src 속성에 설정합니다.
                imgTag.src = extractImagePath(description);
            }
        });
    });
</script>

<%@ include file="../common/IncludeBottom.jsp"%>
