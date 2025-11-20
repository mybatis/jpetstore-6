<%@ page language="java"
		 contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>

<%@ include file="../common/IncludeTop.jsp"%>

<div id="Welcome">
<div id="WelcomeContent"><c:if
	test="${sessionScope.accountBean != null }">
	<c:if test="${sessionScope.accountBean.authenticated}">
        Welcome ${sessionScope.accountBean.account.firstName}!
      </c:if>
</c:if></div>
</div>

<div id="Main">
<div id="Sidebar">
<div id="SidebarContent"><stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewAllItems">
	<strong>All</strong>
</stripes:link> <br />
All Products <br />
<stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="FISH" />
	<img src="../images/fish_icon.gif" />
</stripes:link> <br />
Saltwater, Freshwater <br />
<stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="DOGS" />
	<img src="../images/dogs_icon.gif" />
</stripes:link> <br />
Various Breeds <br />
<stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="CATS" />
	<img src="../images/cats_icon.gif" />
</stripes:link> <br />
Various Breeds, Exotic Varieties <br />
<stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="REPTILES" />
	<img src="../images/reptiles_icon.gif" />
</stripes:link> <br />
Lizards, Turtles, Snakes <br />
<stripes:link
	beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
	event="viewCategory">
	<stripes:param name="categoryId" value="BIRDS" />
	<img src="../images/birds_icon.gif" />
</stripes:link> <br />
Exotic Varieties</div>
</div>

<div id="MainImage">
<div id="MainImageContent">
  <map name="estoremap">
	<area alt="Birds" coords="72,2,280,250"
		href="Catalog.action?viewCategory=&categoryId=BIRDS" shape="RECT" />
	<area alt="Fish" coords="2,180,72,250"
		href="Catalog.action?viewCategory=&categoryId=FISH" shape="RECT" />
	<area alt="Dogs" coords="60,250,130,320"
		href="Catalog.action?viewCategory=&categoryId=DOGS" shape="RECT" />
	<area alt="Reptiles" coords="140,270,210,340"
		href="Catalog.action?viewCategory=&categoryId=REPTILES" shape="RECT" />
	<area alt="Cats" coords="225,240,295,310"
		href="Catalog.action?viewCategory=&categoryId=CATS" shape="RECT" />
	<area alt="Birds" coords="280,180,350,250"
		href="Catalog.action?viewCategory=&categoryId=BIRDS" shape="RECT" />
  </map>
  <img height="355" src="../images/splash.gif" align="middle"
	usemap="#estoremap" width="350" /></div>
</div>

<div id="Separator">&nbsp;</div>
</div>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3>AI 반려동물 시뮬레이션 (테스트용)</h3>

<button type="button" onclick="startGame('고양이')">
	🐱 AI 고양이 키워보기 (테스트)
</button>
<button type="button" onclick="startGame('강아지')">
	🐶 AI 강아지 키워보기 (테스트)
</button>
<button type="button" onclick="startGame('새')">
	🐦 AI 새 키워보기 (테스트)
</button>


<div id="game-area" style="margin-top:16px; padding:12px; border:1px solid #ccc;">
	<div id="game-message" style="margin-bottom:8px; white-space:pre-wrap;"></div>
	<div id="game-status" style="font-size:12px; color:#555; margin-bottom:8px;"></div>
	<div id="game-options"></div>
</div>

<pre id="game-debug" style="margin-top:12px; font-size:11px; background:#f9f9f9; padding:8px;"></pre>

<script type="text/javascript">
	// 전역 세션 ID 저장
	let currentSessionId = null;

	const ctx = '${pageContext.request.contextPath}'; // 보통 /jpetstore

	function startGame(animal) {
		const url = ctx + '/actions/GameSimulation.action?startGame=&breedId='
				+ encodeURIComponent(animal);

		fetch(url, {
			headers: { 'Accept': 'application/json' }
		})
				.then(res => res.json())
				.then(data => {
					currentSessionId = data.sessionId;
					renderGameTurn(data);
				})
				.catch(err => {
					console.error(err);
					document.getElementById('game-message').innerText = '게임 시작 실패';
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
					currentSessionId = data.sessionId; // 그대로 유지
					renderGameTurn(data);
				})
				.catch(err => {
					console.error(err);
					document.getElementById('game-message').innerText = '다음 턴 요청 실패';
				});
	}

	function renderGameTurn(data) {
		// 디버그용 원본 JSON
		document.getElementById('game-debug').innerText = JSON.stringify(data, null, 2);
		// 메시지
		document.getElementById('game-message').innerText = data.message || '';

		// 상태 표시
		const statusText =
				'시간: ' + data.timeHour +
				' | 건강: ' + data.health +
				' | 행복도: ' + data.happiness +
				' | 비용: ' + data.cost +
				(data.finished ? '  [종료]' : '');
		document.getElementById('game-status').innerText = statusText;

		// 옵션 버튼 렌더링
		const optionsDiv = document.getElementById('game-options');
		optionsDiv.innerHTML = '';

		if (data.finished) {
			const optionsDiv = document.getElementById('game-options');
			optionsDiv.innerHTML = '';

			// 종합 점수 표시
			const scoreDiv = document.createElement('div');
			if (data.finalScore != null) {
				scoreDiv.innerText = '종합 점수: ' + data.finalScore + '점';
			} else {
				scoreDiv.innerText = '시뮬레이션이 종료되었습니다.'; // 혹시 finalScore 없을 때 fallback
			}
			optionsDiv.appendChild(scoreDiv);

			// 버튼은 더 이상 안 띄움
			return;
		}

		if (data.options && data.options.length > 0) {
			data.options.forEach(opt => {
				const btn = document.createElement('button');
				btn.type = 'button';
				btn.style.marginRight = '8px';
				btn.innerText = opt.id + ') ' + opt.text;
				btn.onclick = function() {
					nextStep(opt.id);
				};
				optionsDiv.appendChild(btn);
			});
		} else {
			const span = document.createElement('span');
			span.innerText = '선택지가 없습니다.';
			optionsDiv.appendChild(span);
		}
	}
</script>

<%@ include file="../common/IncludeBottom.jsp"%>

