
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="../common/IncludeTop.jsp"%>

<jsp:useBean id="review"
             class="org.mybatis.jpetstore.web.actions.ReviewActionBean" />

<div id="WriteReview"><stripes:form
        beanclass="org.mybatis.jpetstore.web.actions.ReviewActionBean">

    <table>
        <tr>
            <th colspan=2>Write ReviewRating</th>
        </tr>
        <tr>
            <td>Money Rating:</td>
            <td><stripes:select name="reviewRating.rating">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">5</option>
                <option value="4">5</option>
            </stripes:select></td>
        </tr>
        <tr>
            <th colspan=2>Write Review</th>
        </tr>

        <tr>
            <td>Product:</td>
            <td>
                <text name="review.productId" value="${actionBean.product.productId}"/>
                    ${actionBean.product.productId}</td>
        </tr>
        <tr>
            <td>Author:</td>
            <td>
                <text name="review.userId" value="${actionBean.userId}"/>
                    ${actionBean.userId}</td>
        </tr>
        <tr>
            <td>Title:</td>
            <td><stripes:text name="review.title" /></td>
        </tr>
        <tr>
            <td>Content:</td>
            <td><stripes:text size="50" name="review.content" /></td>
        </tr>
        <%--<tr>
            <td>Picture:</td>
            <td><input type="file" name="review.pictureUrl" accept="image/*"
                       onchange="encodeImageFileAsURL(this)"/></td>
        </tr>--%>

    </table>

    <stripes:submit name="newReview" value="Continue" />

</stripes:form></div>

<%@ include file="../common/IncludeBottom.jsp"%>

<script>
    function encodeImageFileAsURL(element) {
        var file = element.files[0];
        var reader = new FileReader();
        reader.onloadend = function() {
            console.log('RESULT', reader.result)
        }
        reader.readAsDataURL(file);
        return reader.result;
    }
</script>