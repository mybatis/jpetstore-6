<%--
  Created by IntelliJ IDEA.
  User: sujin
  Date: 2022-11-25
  Time: 오전 11:42
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

<div id="UpdateForm">
    <stripes:form
            beanclass="org.mybatis.jpetstore.web.actions.AdminActionBean">
        <h3><b> ${actionBean.product.name} </b></h3>
        <table align="center">
            <tr>
                <th>Item ID</th>
                <th>Description</th>
                <th>List Price</th>
                <th>Quantity</th>
            </tr>

            <tr>
                <td>${actionBean.item.itemId}</td>

                <td><stripes:text size="14" name="item.attribute1"
                                  value="${actionBean.item.attribute1}" /></td>
                <td><stripes:text size="14" name="item.listPrice"
                                  value="${actionBean.item.listPrice}" /></td>
                <td><stripes:text size="14" name="item.quantity"
                                  value="${actionBean.item.quantity}" /></td>
            </tr>
        </table>


        <stripes:submit class="button-green" name="updateItem" value="Submit"/>

    </stripes:form>
</div><br/><br/>



<%@ include file="../common/IncludeBottom.jsp"%>