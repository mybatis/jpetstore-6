<%--
  Created by IntelliJ IDEA.
  User: sujin
  Date: 2022-11-25
  Time: 오전 11:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="../common/IncludeTop.jsp"%>


<jsp:useBean id="catalog"
             class="org.mybatis.jpetstore.web.actions.AdminActionBean" />


<div id="BackLink"><stripes:link
        beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
        event="viewProductList">
    Back to Product
</stripes:link></div><br/><br/>

<div id="AddForm">
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

                <stripes:form beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean">
                    <h3><b> ${actionBean.product.name} </b></h3>
                    <table align="center">
                        <tr>
                            <th>ProductId</th>
                            <th>Item ID</th>
                            <th>Description</th>
                            <th>List Price</th>
                            <th>Quantity</th>
                        </tr>

                        <tr>
                            <td>${actionBean.product.productId}</td>
                            <td><stripes:text name="item.itemId"/></td>
                            <td><stripes:text name="item.attribute1"/></td>
                            <td><stripes:text name="item.listPrice"/></td>
                            <td><stripes:text name="inventory.quantity"/></td>
                        </tr>
                    </table>
                    <stripes:submit class="button-green" name="insertItem" value="Submit"/>
                </stripes:form>
            </c:if>
        </c:if>
    </c:if>
</div><br/><br/>



<%@ include file="../common/IncludeBottom.jsp"%>