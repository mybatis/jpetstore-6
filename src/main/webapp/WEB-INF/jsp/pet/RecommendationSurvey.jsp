<%@ include file="../common/IncludeTop.jsp"%>

<div class="survey-container">
    <h2 class="survey-title">🐾 나에게 맞는 반려동물 찾기</h2>
    <p class="survey-subtitle">몇 가지 질문에 답하시면 AI가 최적의 반려동물을 추천해드립니다</p>

    <stripes:form action="/actions/PetRecommendation.action" method="post" class="survey-form">

        <!-- 질문 1: 하루 재택 시간 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">🏠</span>
                1. 하루에 집에 머무는 시간은 얼마나 되시나요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="homeHours" value="2" required>
                    <span class="radio-text">2시간 미만 (거의 외출)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="homeHours" value="5" checked>
                    <span class="radio-text">2-6시간 (일반 직장인)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="homeHours" value="10">
                    <span class="radio-text">6시간 이상 (재택/프리랜서)</span>
                </label>
            </div>
        </div>

        <!-- 질문 2: 주거 형태 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">🏢</span>
                2. 현재 거주하시는 곳은 어떤 형태인가요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="housing" value="원룸" required>
                    <span class="radio-text">원룸 (소형 공간)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="housing" value="아파트" checked>
                    <span class="radio-text">아파트 (중형 공간)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="housing" value="단독주택">
                    <span class="radio-text">단독주택 (대형 공간 + 마당)</span>
                </label>
            </div>
        </div>

        <!-- 질문 3: 월 예산 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">💰</span>
                3. 반려동물에 사용할 수 있는 월 예산은 얼마인가요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="budget" value="10만원미만" required>
                    <span class="radio-text">10만원 미만</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="budget" value="10-30만원" checked>
                    <span class="radio-text">10-30만원</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="budget" value="30만원이상">
                    <span class="radio-text">30만원 이상</span>
                </label>
            </div>
        </div>

        <!-- 질문 4: 활동성 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">🏃</span>
                4. 당신의 성격은 어떤 편인가요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="activityLevel" value="활발함" required>
                    <span class="radio-text">활발함 (운동 좋아함, 산책 자주)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="activityLevel" value="보통" checked>
                    <span class="radio-text">보통 (적당히 활동적)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="activityLevel" value="조용함">
                    <span class="radio-text">조용함 (집에서 휴식 선호)</span>
                </label>
            </div>
        </div>

        <!-- 질문 5: 알레르기 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">🤧</span>
                5. 동물 털 알레르기가 있으신가요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="allergy" value="있음" required>
                    <span class="radio-text">있음</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="allergy" value="없음" checked>
                    <span class="radio-text">없음</span>
                </label>
            </div>
        </div>

        <!-- 질문 6: 경험 -->
        <div class="question-card">
            <label class="question-label">
                <span class="question-icon">📚</span>
                6. 반려동물을 키워본 경험이 있으신가요?
            </label>
            <div class="radio-group">
                <label class="radio-option">
                    <input type="radio" name="experience" value="있음" required>
                    <span class="radio-text">있음 (경험 많음)</span>
                </label>
                <label class="radio-option">
                    <input type="radio" name="experience" value="없음" checked>
                    <span class="radio-text">없음 (초보자)</span>
                </label>
            </div>
        </div>

        <!-- 제출 버튼 -->
        <div class="submit-section">
            <button type="submit" name="getRecommendation" class="submit-btn">
                🔍 AI 추천 받기
            </button>
        </div>

    </stripes:form>
</div>

<style>
.survey-container {
    max-width: 800px;
    margin: 40px auto;
    padding: 20px;
}

.survey-title {
    text-align: center;
    color: #667eea;
    font-size: 32px;
    margin-bottom: 10px;
}

.survey-subtitle {
    text-align: center;
    color: #666;
    font-size: 16px;
    margin-bottom: 40px;
}

.survey-form {
    background: white;
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 10px 40px rgba(0,0,0,0.1);
}

.question-card {
    margin-bottom: 35px;
    padding-bottom: 30px;
    border-bottom: 2px solid #f0f0f0;
}

.question-card:last-of-type {
    border-bottom: none;
}

.question-label {
    display: block;
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
}

.question-icon {
    font-size: 24px;
    margin-right: 10px;
}

.radio-group {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.radio-option {
    background: #f8f9fa;
    padding: 15px 20px;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    border: 2px solid transparent;
}

.radio-option:hover {
    background: #e9ecef;
    transform: translateX(5px);
}

.radio-option input[type="radio"] {
    margin-right: 15px;
    width: 20px;
    height: 20px;
    cursor: pointer;
}

.radio-option input[type="radio"]:checked + .radio-text {
    color: #667eea;
    font-weight: bold;
}

.radio-option:has(input:checked) {
    background: #e7f0ff;
    border-color: #667eea;
}

.radio-text {
    font-size: 16px;
    color: #555;
}

.submit-section {
    text-align: center;
    margin-top: 40px;
}

.submit-btn {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border: none;
    padding: 18px 50px;
    font-size: 18px;
    font-weight: bold;
    border-radius: 50px;
    cursor: pointer;
    box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
    transition: all 0.3s;
}

.submit-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
}

.submit-btn:active {
    transform: translateY(-1px);
}
</style>

<%@ include file="../common/IncludeBottom.jsp"%>
