<%--
  Created by IntelliJ IDEA.
  User: sujin
  Date: 2022-11-24
  Time: 오후 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../common/IncludeTop.jsp"%>

<jsp:useBean id="catalog"
             class="org.mybatis.jpetstore.web.actions.AdminActionBean" />

<div id="BackLink"><stripes:link
        beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
        event="viewProductList">
    Back to Product
</stripes:link></div><br/><br/>

<div id="EditItem">
    <h3><b> ${actionBean.product.name} </b></h3>

    <table align="center">
        <tr>
            <th>Item ID</th>
            <th>Product ID</th>
            <th>Description</th>
            <th>List Price</th>
            <th>&nbsp;</th>
            <th> </th>
        </tr>

        <c:forEach var="item" items="${actionBean.itemList}">
            <tr>
                <td><stripes:link
                        beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                        event="viewItem">
                    <stripes:param name="itemId" value="${item.itemId}" />
                    ${item.itemId}
                </stripes:link></td>
                <td>${item.product.productId}</td>
                <td>${item.attribute1} ${item.attribute2} ${item.attribute3}
                        ${item.attribute4} ${item.attribute5} ${actionBean.product.name}</td>
                <td><fmt:formatNumber value="${item.listPrice}"
                                      pattern="$#,##0.00" /></td>

                <td><stripes:link class="Button"
                                  beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
                                  event="viewUpdateForm">
                    <stripes:param name="itemId" value="${item.itemId}"/>
                    UPDATE
                </stripes:link></td>
                <td><stripes:link class="Button"
                                  beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
                                  event="deleteItem">
                    <stripes:param name="itemId" value="${item.itemId}"/>
                    DELETE
                </stripes:link></td>

            </tr></c:forEach>

    </table>
    <stripes:link class="Button"
                  beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean"
                  event="viewAddForm">
        <stripes:param name="productId" value="${actionBean.product.productId}"/>
        ADD
    </stripes:link>
</div><br/><br/>

<%@ include file="../common/IncludeBottom.jsp"%>