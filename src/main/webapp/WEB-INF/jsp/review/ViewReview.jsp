<%--
  Created by IntelliJ IDEA.
  User: tekiter
  Date: 2022/11/23
  Time: 2:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="../common/IncludeTop.jsp"%>

<jsp:useBean id="review"
             class="org.mybatis.jpetstore.web.actions.ReviewActionBean" />

<div id="Review">
  <table>
    <tr>
      <td>
        <b>${actionBean.review.title}</b>
      </td>
    </tr>
    <tr>
      <td>
        <b>${actionBean.product.name}</b>
      </td>
    </tr>
    <tr>
      <td>
        Author: ${actionBean.review.userId}
      </td>
    </tr>
    <tr>
      <td>
        ${actionBean.review.content}
      </td>
    </tr>
    <tr>
      <td>
        <img src="${actionBean.review.pictureUrl}">
      </td>
    </tr>
  </table>
</div>



<%@ include file="../common/IncludeBottom.jsp"%>
