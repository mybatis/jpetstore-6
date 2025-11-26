<%--

       Copyright 2010-2025 the original author or authors.

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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink">
  <stripes:link beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean">
    메인 메뉴로 돌아가기
  </stripes:link>
</div>

<div id="Catalog">
  <h2>당신에게 추천하는 Top 5 반려동물 카테고리</h2>
  <p>AI가 회원님의 라이프스타일을 분석하여 가장 적합한 반려동물 카테고리 5개를 추천해드립니다.</p>

  <div style="display: flex; gap: 20px; margin-top: 30px; flex-wrap: wrap;">
    <c:forEach var="category" items="${actionBean.top5Categories}" varStatus="status">
      <div style="flex: 1; min-width: 250px; max-width: 350px; border: 2px solid #ddd; border-radius: 10px; padding: 20px; background: #f9f9f9;">
        <h3 style="margin-top: 0; color: #333;">
          ${status.index + 1}순위: ${category.categoryName}
        </h3>

        <div style="margin: 15px 0;">
          <div style="background: #667eea; color: white; padding: 8px; border-radius: 5px; text-align: center; font-weight: bold;">
            적합도: ${category.confidence}%
          </div>
        </div>

        <p style="margin: 15px 0; line-height: 1.6; color: #555;">
          ${category.reason}
        </p>

        <div style="margin-top: 20px;">
          <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.GameSimulationActionBean"
            event="startPage"
            style="display: block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px; text-align: center; text-decoration: none; border-radius: 5px; font-weight: bold;">
            <stripes:param name="categoryId" value="${category.categoryId}" />
            이 카테고리 동물 키워보기
          </stripes:link>
        </div>

        <div style="margin-top: 10px;">
          <stripes:link
            beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
            event="viewCategory"
            style="display: block; background: #f0f0f0; color: #333; padding: 10px; text-align: center; text-decoration: none; border-radius: 5px;">
            <stripes:param name="categoryId" value="${category.categoryId}" />
            카테고리 상품 보기
          </stripes:link>
        </div>
      </div>
    </c:forEach>
  </div>

  <div style="margin-top: 40px; text-align: center;">
    <p style="color: #777; font-size: 14px;">
      원하는 카테고리의 동물을 가상으로 키워보고 나서 최종 추천을 받아보세요!
    </p>
  </div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
