<%@ include file="../common/IncludeTop.jsp"%>

<div class="tamagotchi-wrapper">
    <h2 style="text-align: center; color: #667eea; margin-bottom: 30px;">
        🐾 반려동물 체험 시뮬레이션
    </h2>

    <!-- 다마고치 컨테이너 -->
    <div class="tamagotchi-container">
        <!-- 상단: 시간 및 날짜 -->
        <div class="game-header">
            <div class="day-counter">Day <span id="dayNum">1</span></div>
            <div class="time-display" id="gameTime">07:00 AM</div>
        </div>

        <!-- 펫 디스플레이 영역 -->
        <div class="pet-display">
            <div class="pet-name">${petName != null ? petName : '나비'}</div>
            <div class="pet-animation">
                <img id="petImage" src="${pageContext.request.contextPath}/images/pets/cat-happy.gif" alt="반려동물">
            </div>
            <div class="pet-message" id="petMessage">만나서 반가워요! 잘 부탁해요~ 😊</div>
        </div>

        <!-- 상태 게이지 -->
        <div class="status-panel">
            <div class="stat-row">
                <span class="stat-icon">❤️</span>
                <span class="stat-label">건강</span>
                <div class="stat-bar">
                    <div class="stat-fill health" id="healthBar" style="width: 100%"></div>
                </div>
                <span class="stat-value" id="healthValue">100</span>
            </div>

            <div class="stat-row">
                <span class="stat-icon">😊</span>
                <span class="stat-label">행복</span>
                <div class="stat-bar">
                    <div class="stat-fill happiness" id="happinessBar" style="width: 80%"></div>
                </div>
                <span class="stat-value" id="happinessValue">80</span>
            </div>

            <div class="stat-row">
                <span class="stat-icon">🍖</span>
                <span class="stat-label">배고픔</span>
                <div class="stat-bar">
                    <div class="stat-fill hunger" id="hungerBar" style="width: 50%"></div>
                </div>
                <span class="stat-value" id="hungerValue">50</span>
            </div>

            <div class="stat-row">
                <span class="stat-icon">✨</span>
                <span class="stat-label">청결</span>
                <div class="stat-bar">
                    <div class="stat-fill clean" id="cleanBar" style="width: 100%"></div>
                </div>
                <span class="stat-value" id="cleanValue">100</span>
            </div>
        </div>

        <!-- 액션 버튼 -->
        <div class="action-buttons">
            <button class="action-btn feed" onclick="feedPet()">
                <span class="btn-icon">🍚</span>
                <span class="btn-text">밥 주기</span>
            </button>
            <button class="action-btn play" onclick="playWithPet()">
                <span class="btn-icon">🎾</span>
                <span class="btn-text">놀아주기</span>
            </button>
            <button class="action-btn clean" onclick="cleanPet()">
                <span class="btn-icon">🧹</span>
                <span class="btn-text">청소하기</span>
            </button>
            <button class="action-btn heal" onclick="healPet()">
                <span class="btn-icon">💊</span>
                <span class="btn-text">치료하기</span>
            </button>
        </div>

        <!-- 비용 및 이벤트 로그 -->
        <div class="info-panel">
            <div class="cost-display">
                💰 오늘 사용 비용: <span id="todayCost">0</span>원
                <br>
                📊 총 누적 비용: <span id="totalCost">0</span>원
            </div>

            <div class="event-log" id="eventLog">
                <div class="log-entry">🌅 시뮬레이션을 시작합니다...</div>
            </div>
        </div>

        <!-- 하단 컨트롤 -->
        <div class="game-controls">
            <button class="control-btn" onclick="pauseSimulation()">⏸ 일시정지</button>
            <button class="control-btn" onclick="speedUpTime()">⏩ 시간 빨리</button>
            <button class="control-btn primary" onclick="finishSimulation()">✅ 체험 완료</button>
        </div>
    </div>
</div>

<style>
.tamagotchi-wrapper {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.tamagotchi-container {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 30px;
    padding: 25px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}

.game-header {
    display: flex;
    justify-content: space-between;
    color: white;
    font-weight: bold;
    margin-bottom: 20px;
    background: rgba(255,255,255,0.2);
    padding: 10px 15px;
    border-radius: 15px;
}

.pet-display {
    background: white;
    border-radius: 20px;
    padding: 20px;
    text-align: center;
    margin-bottom: 20px;
    min-height: 300px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.pet-name {
    font-size: 24px;
    font-weight: bold;
    color: #667eea;
    margin-bottom: 15px;
}

.pet-animation {
    margin: 20px 0;
}

#petImage {
    max-width: 200px;
    max-height: 200px;
    animation: float 3s ease-in-out infinite;
}

@keyframes float {
    0%, 100% { transform: translateY(0px); }
    50% { transform: translateY(-15px); }
}

.pet-message {
    background: #f0f0f0;
    padding: 15px;
    border-radius: 20px;
    font-size: 16px;
    color: #333;
    margin-top: 15px;
    position: relative;
    animation: fadeIn 0.5s;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
}

.status-panel {
    background: rgba(255,255,255,0.95);
    border-radius: 15px;
    padding: 20px;
    margin-bottom: 20px;
}

.stat-row {
    display: grid;
    grid-template-columns: 30px 60px 1fr 50px;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
}

.stat-row:last-child {
    margin-bottom: 0;
}

.stat-icon {
    font-size: 20px;
}

.stat-label {
    font-size: 14px;
    font-weight: bold;
    color: #555;
}

.stat-bar {
    background: #e0e0e0;
    height: 20px;
    border-radius: 10px;
    overflow: hidden;
    position: relative;
}

.stat-fill {
    height: 100%;
    transition: width 0.5s ease, background-color 0.3s;
    border-radius: 10px;
}

.stat-fill.health { background: linear-gradient(90deg, #ff6b6b, #ff8787); }
.stat-fill.happiness { background: linear-gradient(90deg, #ffd93d, #ffe66d); }
.stat-fill.hunger { background: linear-gradient(90deg, #6bcf7f, #95e1a3); }
.stat-fill.clean { background: linear-gradient(90deg, #4dabf7, #74c0fc); }

.stat-value {
    font-size: 14px;
    font-weight: bold;
    color: #667eea;
    text-align: right;
}

.action-buttons {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
    margin-bottom: 20px;
}

.action-btn {
    background: white;
    border: none;
    border-radius: 15px;
    padding: 15px;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.action-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 12px rgba(0,0,0,0.2);
}

.action-btn:active {
    transform: translateY(-1px);
}

.btn-icon {
    font-size: 28px;
    display: block;
    margin-bottom: 8px;
}

.btn-text {
    font-size: 14px;
    font-weight: bold;
    color: #555;
}

.info-panel {
    background: rgba(255,255,255,0.95);
    border-radius: 15px;
    padding: 15px;
    margin-bottom: 15px;
}

.cost-display {
    font-size: 14px;
    font-weight: bold;
    color: #667eea;
    margin-bottom: 15px;
    padding-bottom: 15px;
    border-bottom: 2px solid #e0e0e0;
}

.event-log {
    max-height: 120px;
    overflow-y: auto;
    font-size: 13px;
}

.log-entry {
    padding: 8px;
    margin-bottom: 5px;
    background: #f8f9fa;
    border-radius: 8px;
    border-left: 3px solid #667eea;
}

.game-controls {
    display: flex;
    gap: 10px;
}

.control-btn {
    flex: 1;
    background: rgba(255,255,255,0.9);
    border: none;
    padding: 12px;
    border-radius: 10px;
    cursor: pointer;
    font-weight: bold;
    color: #555;
    transition: all 0.3s;
}

.control-btn:hover {
    background: white;
}

.control-btn.primary {
    background: #10b981;
    color: white;
}

.control-btn.primary:hover {
    background: #059669;
}
</style>

<script>
// 게임 상태
let gameState = {
    health: 100,
    happiness: 80,
    hunger: 50,
    clean: 100,
    todayCost: 0,
    totalCost: 0,
    day: 1,
    hour: 7,
    minute: 0,
    isPaused: false,
    timeSpeed: 1,
    petMood: 'happy',
    eventHistory: []
};

// 이미지 경로 매핑
const petImages = {
    happy: '${pageContext.request.contextPath}/images/pets/cat-happy.gif',
    hungry: '${pageContext.request.contextPath}/images/pets/cat-hungry.gif',
    sick: '${pageContext.request.contextPath}/images/pets/cat-sick.gif',
    sleeping: '${pageContext.request.contextPath}/images/pets/cat-sleep.gif',
    playing: '${pageContext.request.contextPath}/images/pets/cat-play.gif',
    dirty: '${pageContext.request.contextPath}/images/pets/cat-dirty.gif'
};

// 밥 주기
function feedPet() {
    if (gameState.hunger >= 90) {
        showMessage("아직 배가 안 고파요! 😋");
        return;
    }

    gameState.hunger = Math.min(100, gameState.hunger + 30);
    gameState.happiness = Math.min(100, gameState.happiness + 5);
    gameState.todayCost += 3000;
    gameState.totalCost += 3000;

    changePetImage('happy');
    showMessage("냠냠~ 맛있어요! 고마워요~ 😻");
    addLog("🍚 사료를 급여했습니다. (비용: 3,000원)");
    updateUI();

    setTimeout(() => updatePetMood(), 2000);
}

// 놀아주기
function playWithPet() {
    if (gameState.happiness >= 95) {
        showMessage("지금은 충분히 행복해요! 😊");
        return;
    }

    gameState.happiness = Math.min(100, gameState.happiness + 20);
    gameState.hunger = Math.max(0, gameState.hunger - 10);
    gameState.clean = Math.max(0, gameState.clean - 5);

    changePetImage('playing');
    showMessage("와! 재밌어요! 더 놀아요~ 🎾");
    addLog("🎾 반려동물과 즐겁게 놀았습니다.");
    updateUI();

    setTimeout(() => updatePetMood(), 3000);
}

// 청소하기
function cleanPet() {
    if (gameState.clean >= 90) {
        showMessage("아직 깨끗해요! ✨");
        return;
    }

    gameState.clean = 100;
    gameState.happiness = Math.min(100, gameState.happiness + 10);
    gameState.todayCost += 2000;
    gameState.totalCost += 2000;

    changePetImage('happy');
    showMessage("깔끔해졌어요! 상쾌해요~ ✨");
    addLog("🧹 청소를 완료했습니다. (비용: 2,000원)");
    updateUI();

    setTimeout(() => updatePetMood(), 2000);
}

// 치료하기
function healPet() {
    if (gameState.health >= 90) {
        showMessage("건강해요! 병원 안 가도 돼요~ 💪");
        return;
    }

    gameState.health = Math.min(100, gameState.health + 40);
    gameState.todayCost += 50000;
    gameState.totalCost += 50000;

    changePetImage('happy');
    showMessage("병원 다녀왔어요! 이제 괜찮아요~ 💊");
    addLog("💊 동물병원에서 치료받았습니다. (비용: 50,000원)");
    updateUI();

    setTimeout(() => updatePetMood(), 2000);
}

// 펫 이미지 변경
function changePetImage(mood) {
    const img = document.getElementById('petImage');
    const imagePath = petImages[mood];
    if (imagePath) {
        img.src = imagePath;
        gameState.petMood = mood;
    }
}

// 메시지 표시
function showMessage(msg) {
    const messageEl = document.getElementById('petMessage');
    messageEl.textContent = msg;
    messageEl.style.animation = 'none';
    setTimeout(() => messageEl.style.animation = 'fadeIn 0.5s', 10);
}

// 로그 추가
function addLog(message) {
    const time = String(gameState.hour).padStart(2, '0') + ':' + String(gameState.minute).padStart(2, '0');
    const logEntry = document.createElement('div');
    logEntry.className = 'log-entry';
    logEntry.textContent = `[${time}] ${message}`;

    const logContainer = document.getElementById('eventLog');
    logContainer.insertBefore(logEntry, logContainer.firstChild);

    // 최대 10개 항목만 유지
    while (logContainer.children.length > 10) {
        logContainer.removeChild(logContainer.lastChild);
    }
}

// UI 업데이트
function updateUI() {
    document.getElementById('healthBar').style.width = gameState.health + '%';
    document.getElementById('healthValue').textContent = Math.round(gameState.health);

    document.getElementById('happinessBar').style.width = gameState.happiness + '%';
    document.getElementById('happinessValue').textContent = Math.round(gameState.happiness);

    document.getElementById('hungerBar').style.width = gameState.hunger + '%';
    document.getElementById('hungerValue').textContent = Math.round(gameState.hunger);

    document.getElementById('cleanBar').style.width = gameState.clean + '%';
    document.getElementById('cleanValue').textContent = Math.round(gameState.clean);

    document.getElementById('todayCost').textContent = gameState.todayCost.toLocaleString();
    document.getElementById('totalCost').textContent = gameState.totalCost.toLocaleString();

    document.getElementById('dayNum').textContent = gameState.day;
}

// 펫 기분 업데이트
function updatePetMood() {
    if (gameState.health < 30) {
        changePetImage('sick');
        showMessage("아파요... 병원에 가야 할 것 같아요 😿");
    } else if (gameState.hunger < 20) {
        changePetImage('hungry');
        showMessage("배고파요... 밥 주세요! 🍖");
    } else if (gameState.clean < 30) {
        changePetImage('dirty');
        showMessage("더러워요... 씻고 싶어요 💧");
    } else if (gameState.happiness > 80) {
        changePetImage('happy');
        showMessage("행복해요! 사랑해요~ 💕");
    } else {
        changePetImage('happy');
    }
}

// 시간 진행
function advanceTime() {
    if (gameState.isPaused) return;

    gameState.minute += 10 * gameState.timeSpeed;
    if (gameState.minute >= 60) {
        gameState.minute = 0;
        gameState.hour++;

        // 시간에 따른 자동 상태 변화
        gameState.hunger = Math.max(0, gameState.hunger - 3);
        gameState.happiness = Math.max(0, gameState.happiness - 2);
        gameState.clean = Math.max(0, gameState.clean - 1);

        // 랜덤 이벤트
        if (Math.random() < 0.2) {
            triggerRandomEvent();
        }
    }

    if (gameState.hour >= 24) {
        gameState.hour = 0;
        gameState.day++;
        gameState.todayCost = 0;
        addLog("🌙 하루가 지났습니다. Day " + gameState.day);
    }

    const timeStr = String(gameState.hour).padStart(2, '0') + ':' +
                   String(gameState.minute).padStart(2, '0') +
                   (gameState.hour < 12 ? ' AM' : ' PM');
    document.getElementById('gameTime').textContent = timeStr;

    updatePetMood();
    updateUI();
}

// 랜덤 이벤트
function triggerRandomEvent() {
    const events = [
        { msg: "🌧️ 비가 와서 산책을 못 갔어요.", effect: () => gameState.happiness -= 5 },
        { msg: "🎁 장난감을 발견했어요!", effect: () => gameState.happiness += 10 },
        { msg: "😴 낮잠을 잤어요.", effect: () => gameState.health += 5 },
        { msg: "🤧 감기 기운이 있어요.", effect: () => gameState.health -= 10 }
    ];

    const event = events[Math.floor(Math.random() * events.length)];
    event.effect();
    addLog(event.msg);
    showMessage(event.msg.substring(2));
}

// 일시정지
function pauseSimulation() {
    gameState.isPaused = !gameState.isPaused;
    const btn = event.target;
    btn.textContent = gameState.isPaused ? '▶️ 계속하기' : '⏸ 일시정지';
}

// 시간 가속
function speedUpTime() {
    gameState.timeSpeed = gameState.timeSpeed === 1 ? 3 : 1;
    const btn = event.target;
    btn.textContent = gameState.timeSpeed === 1 ? '⏩ 시간 빨리' : '⏸ 정상 속도';
}

// 체험 완료
function finishSimulation() {
    const avgHealth = gameState.health;
    const avgHappiness = gameState.happiness;
    const totalScore = Math.round((avgHealth + avgHappiness) / 2);

    let rating = '';
    if (totalScore >= 90) rating = '완벽한 집사형 🏆';
    else if (totalScore >= 70) rating = '좋은 집사형 😊';
    else if (totalScore >= 50) rating = '초보 집사형 📚';
    else rating = '연습이 필요해요 💪';

    alert(`
🎉 체험 완료!

📊 최종 점수: ${totalScore}점
🏅 평가: ${rating}

💰 총 사용 비용: ${gameState.totalCost.toLocaleString()}원
📅 체험 기간: ${gameState.day}일

이 반려동물이 당신에게 맞다고 생각하시나요?
    `);

    // 결과 페이지로 이동
    window.location.href = '${pageContext.request.contextPath}/actions/Catalog.action?viewCategory=';
}

// 게임 시작
setInterval(advanceTime, 2000); // 2초마다 시간 진행
addLog("🎮 시뮬레이션이 시작되었습니다!");
</script>

<%@ include file="../common/IncludeBottom.jsp"%>
