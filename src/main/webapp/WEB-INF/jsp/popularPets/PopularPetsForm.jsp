<%--
  Created by IntelliJ IDEA.
  User: Lim
  Date: 2021-11-30
  Time: 오후 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../common/IncludeTop.jsp"%>

<h2>Popular Pets</h2>

<table>
    <tr>
        <th>ItemId</th>
        <th>Description</th>
        <th>Quantity</th>
    </tr>

    <c:forEach var="popularPets" items="${actionBean.popularPets}">
        <tr>
            <td><stripes:link
                    beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
                    event="viewItem">
                <stripes:param name="itemId" value="${popularPets.itemId}" />
                ${popularPets.itemId}
            </stripes:link></td>
            <td>${popularPets.attr1} ${popularPets.name} </td>
            <td>${popularPets.sum}</td>
        </tr>
    </c:forEach>
</table>

<%@ include file="../common/IncludeBottom.jsp"%>
