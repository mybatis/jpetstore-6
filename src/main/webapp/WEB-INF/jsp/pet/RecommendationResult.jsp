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
<%@ include file="../common/IncludeTop.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="result-container">
    <h2 class="result-title">🎯 당신을 위한 맞춤 추천 결과</h2>
    <p class="result-subtitle">AI 데이터마이닝 분석 기반 추천</p>

    <div class="recommendations-wrapper">
        <c:forEach var="rec" items="${actionBean.recommendations}" varStatus="status">
            <div class="recommendation-card ${status.index == 0 ? 'top-recommendation' : ''}">
                <div class="rank-badge">
                    <c:choose>
                        <c:when test="${status.index == 0}">🥇</c:when>
                        <c:when test="${status.index == 1}">🥈</c:when>
                        <c:when test="${status.index == 2}">🥉</c:when>
                        <c:otherwise>${status.index + 1}</c:otherwise>
                    </c:choose>
                </div>

                <div class="pet-icon">
                    <c:choose>
                        <c:when test="${rec.petType == 'FISH'}">🐠</c:when>
                        <c:when test="${rec.petType == 'DOGS'}">🐕</c:when>
                        <c:when test="${rec.petType == 'CATS'}">🐈</c:when>
                        <c:when test="${rec.petType == 'BIRDS'}">🦜</c:when>
                        <c:when test="${rec.petType == 'REPTILES'}">🦎</c:when>
                    </c:choose>
                </div>

                <h3 class="pet-name">${rec.petTypeName}</h3>

                <div class="confidence-section">
                    <div class="confidence-label">적합도</div>
                    <div class="confidence-bar-container">
                        <div class="confidence-bar" style="width: ${rec.confidence}%">
                            <fmt:formatNumber value="${rec.confidence}" maxFractionDigits="1"/>%
                        </div>
                    </div>
                </div>

                <p class="recommendation-reason">
                    💡 ${rec.reason}
                </p>

                <c:if test="${status.index == 0}">
                    <div class="action-buttons">
                        <stripes:link href="/actions/PetSimulation.action" class="btn-primary">
                            <stripes:param name="petType" value="${rec.petType}"/>
                            <stripes:param name="petName" value="나비"/>
                            🎮 가상 체험하기
                        </stripes:link>

                        <stripes:link href="/actions/Catalog.action?viewCategory=${rec.petType}" class="btn-secondary">
                            🛒 상품 보러가기
                        </stripes:link>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <!-- 알고리즘 설명 섹션 -->
    <div class="algorithm-info">
        <h4>📊 추천 알고리즘</h4>
        <p>
            이 추천 결과는 <strong>J48 의사결정 트리</strong> 데이터마이닝 알고리즘을 사용하여 생성되었습니다.
            <br>
            당신의 생활 패턴, 주거 환경, 경제 상황 등을 종합적으로 분석하여 최적의 반려동물을 추천합니다.
        </p>
    </div>

    <!-- 다시 진단하기 버튼 -->
    <div class="retry-section">
        <stripes:link href="/actions/PetRecommendation.action" class="btn-retry">
            🔄 다시 진단하기
        </stripes:link>
    </div>
</div>

<style>
.result-container {
    max-width: 900px;
    margin: 40px auto;
    padding: 20px;
}

.result-title {
    text-align: center;
    color: #667eea;
    font-size: 32px;
    margin-bottom: 10px;
}

.result-subtitle {
    text-align: center;
    color: #666;
    font-size: 14px;
    margin-bottom: 40px;
    font-style: italic;
}

.recommendations-wrapper {
    display: flex;
    flex-direction: column;
    gap: 25px;
    margin-bottom: 40px;
}

.recommendation-card {
    background: white;
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 5px 20px rgba(0,0,0,0.1);
    position: relative;
    transition: all 0.3s;
}

.recommendation-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 30px rgba(0,0,0,0.15);
}

.top-recommendation {
    border: 3px solid #667eea;
    background: linear-gradient(135deg, #ffffff 0%, #f0f4ff 100%);
}

.rank-badge {
    position: absolute;
    top: 20px;
    right: 20px;
    font-size: 32px;
    font-weight: bold;
    color: #667eea;
}

.pet-icon {
    font-size: 80px;
    text-align: center;
    margin-bottom: 20px;
}

.pet-name {
    text-align: center;
    font-size: 28px;
    color: #333;
    margin-bottom: 20px;
}

.confidence-section {
    margin: 25px 0;
}

.confidence-label {
    font-size: 14px;
    color: #666;
    margin-bottom: 10px;
    font-weight: bold;
}

.confidence-bar-container {
    background: #e9ecef;
    height: 30px;
    border-radius: 15px;
    overflow: hidden;
    position: relative;
}

.confidence-bar {
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 15px;
    color: white;
    font-weight: bold;
    font-size: 14px;
    transition: width 1s ease-out;
    animation: fillBar 1.5s ease-out;
}

@keyframes fillBar {
    from { width: 0; }
}

.recommendation-reason {
    background: #f8f9fa;
    padding: 15px 20px;
    border-radius: 12px;
    border-left: 4px solid #667eea;
    color: #555;
    font-size: 16px;
    line-height: 1.6;
}

.action-buttons {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 15px;
    margin-top: 25px;
}

.btn-primary, .btn-secondary {
    padding: 15px 25px;
    border-radius: 12px;
    text-align: center;
    font-weight: bold;
    font-size: 16px;
    text-decoration: none;
    display: inline-block;
    transition: all 0.3s;
}

.btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(102, 126, 234, 0.5);
}

.btn-secondary {
    background: white;
    color: #667eea;
    border: 2px solid #667eea;
}

.btn-secondary:hover {
    background: #667eea;
    color: white;
}

.algorithm-info {
    background: #f8f9fa;
    padding: 25px;
    border-radius: 15px;
    margin-bottom: 30px;
}

.algorithm-info h4 {
    color: #667eea;
    margin-bottom: 15px;
    font-size: 20px;
}

.algorithm-info p {
    color: #555;
    line-height: 1.8;
    font-size: 15px;
}

.retry-section {
    text-align: center;
}

.btn-retry {
    display: inline-block;
    padding: 15px 40px;
    background: white;
    color: #667eea;
    border: 2px solid #667eea;
    border-radius: 50px;
    font-weight: bold;
    font-size: 16px;
    text-decoration: none;
    transition: all 0.3s;
}

.btn-retry:hover {
    background: #667eea;
    color: white;
    transform: scale(1.05);
}
</style>

<script>
// 애니메이션 효과
document.addEventListener('DOMContentLoaded', function() {
    const cards = document.querySelectorAll('.recommendation-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';

        setTimeout(() => {
            card.style.transition = 'all 0.6s ease-out';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 200);
    });
});
</script>

<%@ include file="../common/IncludeBottom.jsp"%>
