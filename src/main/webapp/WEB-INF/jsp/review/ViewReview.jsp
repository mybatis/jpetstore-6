
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
    <c:forEach var="rating" items="${actionBean.ratingList}">
      <tr>
        <td>
          ${rating.key}: ${rating.rating}
        </td>
      </tr>
    </c:forEach>
    <tr>
      <td>
        <img src="${actionBean.review.pictureUrl}">
      </td>
    </tr>
  </table>
</div>



<%@ include file="../common/IncludeBottom.jsp"%>
