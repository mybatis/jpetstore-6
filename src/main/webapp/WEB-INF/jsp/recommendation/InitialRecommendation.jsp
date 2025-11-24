<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/IncludeTop.jsp"%>

<div id="Catalog">
  <h2>당신에게 추천하는 Top 3 반려동물</h2>
  <p>AI가 회원님의 라이프스타일을 분석하여 가장 적합한 반려동물 3종을 추천해드립니다.</p>

  <div style="display: flex; gap: 20px; margin-top: 30px; flex-wrap: wrap;">
    <c:forEach var="breed" items="${actionBean.top3Breeds}" varStatus="status">
      <div style="flex: 1; min-width: 250px; max-width: 350px; border: 2px solid #ddd; border-radius: 10px; padding: 20px; background: #f9f9f9;">
        <h3 style="margin-top: 0; color: #333;">
          ${status.index + 1}순위: ${breed.breedName}
        </h3>

        <div style="margin: 15px 0;">
          <div style="background: #667eea; color: white; padding: 8px; border-radius: 5px; text-align: center; font-weight: bold;">
            적합도: ${breed.confidence}%
          </div>
        </div>

        <p style="margin: 15px 0; line-height: 1.6; color: #555;">
          ${breed.reason}
        </p>

        <div style="margin-top: 20px;">
          <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.GameSimulationActionBean"
            event="startPage"
            style="display: block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px; text-align: center; text-decoration: none; border-radius: 5px; font-weight: bold;">
            <stripes:param name="breedId" value="${breed.breedId}" />
            이 동물 키워보기
          </stripes:link>
        </div>

        <div style="margin-top: 10px;">
          <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
            event="viewProduct"
            style="display: block; background: #f0f0f0; color: #333; padding: 10px; text-align: center; text-decoration: none; border-radius: 5px;">
            <stripes:param name="productId" value="${breed.breedId}" />
            상품 정보 보기
          </stripes:link>
        </div>
      </div>
    </c:forEach>
  </div>

  <div style="margin-top: 40px; text-align: center;">
    <p style="color: #777; font-size: 14px;">
      각 동물을 가상으로 키워보고 나서 최종 추천을 받아보세요!
    </p>
  </div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
