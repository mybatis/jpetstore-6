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
  <h2>반려동물 돌봄 시뮬레이션</h2>
  <p>24시간 동안 반려동물을 돌봐주세요. AI가 실시간으로 상황을 생성합니다.</p>

  <div style="margin: 20px 0; padding: 15px; background: #f0f0f0; border-radius: 5px;">
    <p><strong>품종:</strong> ${param.breedId}</p>
    <button type="button" onclick="startGame('${param.breedId}')" style="background: #667eea; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px;">
      게임 시작
    </button>
  </div>

  <div id="game-area" style="margin-top: 20px; padding: 20px; border: 2px solid #ddd; border-radius: 10px; background: #fafafa;">
    <div id="game-message" style="margin-bottom: 15px; white-space: pre-wrap; font-size: 16px; line-height: 1.6;"></div>
    <div id="game-status" style="font-size: 14px; color: #555; margin-bottom: 15px; padding: 10px; background: #e8e8e8; border-radius: 5px;"></div>
    <div id="game-options" style="display: flex; gap: 10px; flex-wrap: wrap;"></div>
  </div>

  <div id="game-complete" style="display: none; margin-top: 20px; padding: 20px; border: 2px solid #667eea; border-radius: 10px; background: #f0f4ff; text-align: center;">
    <h3 style="color: #667eea;">게임 완료!</h3>
    <p id="final-score-display" style="font-size: 18px; font-weight: bold; margin: 15px 0;"></p>
    <button type="button" onclick="goToFinalRecommendation()" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 30px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; font-weight: bold;">
      최종 추천 받기
    </button>
  </div>
</div>

<script type="text/javascript">
  let currentSessionId = null;
  const ctx = '${pageContext.request.contextPath}';

  function startGame(breedId) {
    const url = ctx + '/actions/GameSimulation.action?startGame=&breedId=' + encodeURIComponent(breedId);

    fetch(url, {
      headers: { 'Accept': 'application/json' }
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) {
        alert('게임 시작 실패: ' + data.error);
        return;
      }
      currentSessionId = data.sessionId;
      renderGameTurn(data);
    })
    .catch(err => {
      console.error(err);
      alert('게임 시작 중 오류가 발생했습니다.');
    });
  }

  function nextStep(optionId) {
    if (!currentSessionId) {
      alert('먼저 게임을 시작해 주세요.');
      return;
    }

    const params = new URLSearchParams({
      nextStep: '',
      sessionId: currentSessionId,
      optionId: optionId
    });

    const url = ctx + '/actions/GameSimulation.action?' + params.toString();

    fetch(url, {
      headers: { 'Accept': 'application/json' }
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) {
        alert('다음 턴 진행 실패: ' + data.error);
        return;
      }
      renderGameTurn(data);
    })
    .catch(err => {
      console.error(err);
      alert('다음 턴 진행 중 오류가 발생했습니다.');
    });
  }

  function renderGameTurn(data) {
    // 메시지 표시
    document.getElementById('game-message').innerText = data.message || '';

    // 상태 표시
    const statusText =
      '시간: ' + data.timeHour + '시 ' +
      '| 건강: ' + data.health + ' ' +
      '| 행복도: ' + data.happiness + ' ' +
      '| 비용: ' + data.cost + '원' +
      (data.finished ? ' [종료]' : '');
    document.getElementById('game-status').innerText = statusText;

    // 옵션 버튼 렌더링
    const optionsDiv = document.getElementById('game-options');
    optionsDiv.innerHTML = '';

    if (data.finished) {
      // 게임 완료
      document.getElementById('final-score-display').innerText = '종합 점수: ' + (data.finalScore || 0) + '점';
      document.getElementById('game-complete').style.display = 'block';
      return;
    }

    if (data.options && data.options.length > 0) {
      data.options.forEach(opt => {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.style.cssText = 'flex: 1; min-width: 150px; padding: 12px; background: white; border: 2px solid #667eea; border-radius: 5px; cursor: pointer; font-size: 14px;';
        btn.innerText = opt.id + ') ' + opt.text;
        btn.onclick = function() {
          nextStep(opt.id);
        };
        btn.onmouseover = function() {
          this.style.background = '#667eea';
          this.style.color = 'white';
        };
        btn.onmouseout = function() {
          this.style.background = 'white';
          this.style.color = 'black';
        };
        optionsDiv.appendChild(btn);
      });
    } else {
      const span = document.createElement('span');
      span.innerText = '선택지가 없습니다.';
      optionsDiv.appendChild(span);
    }
  }

  function goToFinalRecommendation() {
    if (!currentSessionId) {
      alert('게임 세션 정보가 없습니다.');
      return;
    }
    window.location.href = ctx + '/actions/FinalRecommendation.action?sessionId=' + currentSessionId;
  }
</script>

<%@ include file="../common/IncludeBottom.jsp"%>
