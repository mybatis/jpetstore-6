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

<div id="Catalog">
  <h2>🎉 최종 추천 결과</h2>
  <p>게임 플레이와 라이프스타일 분석을 종합하여 가장 적합한 반려동물을 추천해드립니다.</p>

  <div style="margin-top: 30px; max-width: 600px; margin-left: auto; margin-right: auto;">
    <div style="border: 3px solid #667eea; border-radius: 15px; padding: 30px; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);">

      <div style="text-align: center; margin-bottom: 20px;">
        <h1 style="color: #667eea; margin: 0; font-size: 32px;">
          ${actionBean.finalRecommendation.breedName}
        </h1>
      </div>

      <div style="margin: 20px 0;">
        <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; border-radius: 10px; text-align: center; font-weight: bold; font-size: 20px;">
          적합도: ${actionBean.finalRecommendation.confidence}%
        </div>
      </div>

      <div style="margin: 25px 0; padding: 20px; background: white; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
        <h3 style="margin-top: 0; color: #333; font-size: 18px;">💡 추천 이유</h3>
        <p style="line-height: 1.8; color: #555; margin: 0;">
          ${actionBean.finalRecommendation.reason}
        </p>
      </div>

      <div style="margin: 25px 0; padding: 20px; background: #fff9e6; border-radius: 10px; border: 2px solid #ffd700;">
        <h3 style="margin-top: 0; color: #d97706; font-size: 18px;">🎮 게임 분석</h3>
        <div style="margin-bottom: 15px;">
          <strong style="color: #666;">게임 점수:</strong>
          <span style="font-size: 24px; color: #667eea; font-weight: bold; margin-left: 10px;">
            ${actionBean.finalRecommendation.gameScore}점
          </span>
        </div>
        <p style="line-height: 1.8; color: #555; margin: 0;">
          ${actionBean.finalRecommendation.gameInsight}
        </p>
      </div>

      <div style="margin-top: 30px;">
        <stripes:link
          beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
          event="viewProduct"
          style="display: block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; text-align: center; text-decoration: none; border-radius: 10px; font-weight: bold; font-size: 18px; margin-bottom: 10px;">
          <stripes:param name="productId" value="${actionBean.finalRecommendation.breedId}" />
          이 동물 상품 정보 보기
        </stripes:link>
      </div>

      <div style="margin-top: 10px;">
        <stripes:link
          beanclass="org.mybatis.jpetstore.web.actions.InitialRecommendationActionBean"
          style="display: block; background: #f0f0f0; color: #333; padding: 12px; text-align: center; text-decoration: none; border-radius: 10px;">
          다른 동물도 추천받기
        </stripes:link>
      </div>

    </div>
  </div>

  <div style="margin-top: 40px; text-align: center;">
    <p style="color: #777; font-size: 14px;">
      JPetStore AI 반려동물 추천 시스템
    </p>
  </div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>
