
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    var baseImage;
    function ImageFileAsURL(element) {
        var file = element.files[0];
        const fr = new FileReader();

        fr.onload = (base64) => {
            const image = new Image();

            image.src = base64.target.result;

            image.onload = (e) => {
                const $canvas = document.createElement(`canvas`);
                const ctx = $canvas.getContext(`2d`);

                $canvas.width = e.target.width;
                $canvas.height = e.target.height;

                ctx.drawImage(e.target, 0, 0);

                // 용량이 줄어든 base64 이미지
                baseImage = $canvas.toDataURL(`image/jpeg`, 0.5);
                document.getElementById("picurl").value = baseImage;
                console.log($canvas.toDataURL(`image/jpeg`, 0.5));
            }
        }
        fr.readAsDataURL(file);
    }

</script>
<%@ include file="../common/IncludeTop.jsp"%>

<jsp:useBean id="review"
             class="org.mybatis.jpetstore.web.actions.ReviewActionBean" />

<div id="Catalog">
    <stripes:form
        beanclass="org.mybatis.jpetstore.web.actions.ReviewActionBean">

    <table>
        <tr>
            <th colspan=2>Write ReviewRating</th>
        </tr>
        <c:forEach var="rating" items="${actionBean.ratingList}" varStatus="status">
            <tr>
                <td>${rating.key} Rating:</td>
                <td>
                    <stripes:select name="ratingList[${status.index}].rating">
                        <stripes:option value="1">1</stripes:option>
                        <stripes:option value="2">2</stripes:option>
                        <stripes:option value="3">3</stripes:option>
                        <stripes:option value="4">4</stripes:option>
                        <stripes:option value="5">5</stripes:option>
                    </stripes:select></td>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <th colspan=2>Write Review</th>
        </tr>

        <tr>
            <td>Product:</td>
            <td>
                <text name="review.productId" value="${actionBean.product.productId}"/>
                    ${actionBean.product.name}</td>
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
        <tr>
            <td>Picture:</td>
            <td><input type="file" accept="image/*" id="picture" onchange="ImageFileAsURL(this)"/></td>
            <stripes:hidden name="review.pictureUrl" id="picurl"></stripes:hidden>
        </tr>

    </table>
    <stripes:submit name="newReview" value="Resist"/>
</stripes:form>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>

