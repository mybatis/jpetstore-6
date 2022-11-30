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

<jsp:useBean id="Product"
             class="org.mybatis.jpetstore.web.actions.AdminActionBean" />

<div id="BackLink"><stripes:link
        beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
    Return to Menu</stripes:link></div><br><br>

<div id="Product">
    <c:if test="${sessionScope.accountBean != null}">
        <c:if test="${sessionScope.accountBean.authenticated}">
            <c:if test="${sessionScope.accountBean.account.role == 0}">
                <h3>Please log in with admin ID!</h3>
            </c:if>
        </c:if>
    </c:if>

    <c:if test="${sessionScope.accountBean != null}">
        <c:if test="${sessionScope.accountBean.authenticated}">
            <c:if test="${sessionScope.accountBean.account.role != 0}">
                <table align="center">
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>&nbsp;</th>
                    </tr>

                    <c:forEach var="item" items="${actionBean.productList}">
                        <tr>
                            <td><stripes:link
                                    beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                                    event="viewProduct">
                                <stripes:param name="productId" value="${item.productId}"/>
                                ${item.productId}
                            </stripes:link></td>

                            <td>${item.name}</td>

                            <td><stripes:link class="Button"
                                              beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
                                              event="viewEditItem">
                                <stripes:param name="productId" value="${item.productId}"/>
                                EDIT ITEM
                            </stripes:link></td>

                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </c:if>
    </c:if>
</div><br><br>

<%@ include file="../common/IncludeBottom.jsp"%>




