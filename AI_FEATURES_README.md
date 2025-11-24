# 🐾 JPetStore AI 기능 사용 가이드

## 📚 구현된 기능

### 1. 데이터마이닝 기반 반려동물 추천 시스템
- **알고리즘**: Weka J48 의사결정 트리
- **URL**: `/actions/PetRecommendation.action`
- **기능**: 사용자 속성 분석하여 최적의 반려동물 추천 (Top 3)

### 2. 다마고치 스타일 시뮬레이션 게임
- **URL**: `/actions/PetSimulation.action`
- **기능**: 24시간 가상 반려동물 양육 체험
- **특징**: 실시간 상태 관리, 비용 누적, 이벤트 시스템

---

## 🚀 실행 방법

### 1. 의존성 설치
```bash
cd /Users/jeong-wonlyeol/Desktop/project/SWG/jpetstore
./mvnw clean install
```

### 2. 서버 실행
```bash
./mvnw cargo:run
```

또는 IntelliJ에서:
- Run → Edit Configurations → Maven
- Command line: `cargo:run`
- 실행

### 3. 브라우저에서 접속
```
http://localhost:8080/jpetstore
```

---

## 🎮 기능 사용법

### ✅ 반려동물 추천받기
1. 메인 페이지에서 "AI 반려동물 추천" 클릭
2. 6가지 질문에 답변 (재택시간, 주거형태, 예산 등)
3. "AI 추천 받기" 버튼 클릭
4. 적합도 순으로 Top 3 추천 결과 확인
5. 1순위 동물로 "가상 체험하기" 선택

### 🎯 시뮬레이션 체험
1. 추천 결과에서 "가상 체험하기" 클릭
2. 다마고치 스타일 화면 로드
3. 상태 게이지 확인 (건강/행복/배고픔/청결)
4. 액션 버튼 사용:
   - 🍚 밥 주기
   - 🎾 놀아주기
   - 🧹 청소하기
   - 💊 치료하기
5. 시간이 자동으로 흐르며 상태 변화
6. "체험 완료" 버튼으로 최종 평가 확인

---

## 📁 프로젝트 구조

```
jpetstore/
├── src/main/java/org/mybatis/jpetstore/
│   ├── service/
│   │   └── PetRecommendationService.java    ← J48 알고리즘 구현
│   └── web/actions/
│       ├── PetRecommendationActionBean.java ← 추천 컨트롤러
│       └── PetSimulationActionBean.java     ← 시뮬레이션 컨트롤러
│
├── src/main/webapp/WEB-INF/jsp/pet/
│   ├── RecommendationSurvey.jsp             ← 설문 조사 페이지
│   ├── RecommendationResult.jsp             ← 추천 결과 페이지
│   └── TamagotchiSimulation.jsp             ← 시뮬레이션 게임
│
└── src/main/webapp/images/pets/             ← 펫 이미지 폴더
    └── README.md                            ← 이미지 가이드
```

---

## 🖼️ 펫 이미지 준비

### 방법 1: 무료 리소스 다운로드
추천 사이트:
- **Kenney.nl** - https://kenney.nl/assets (무료 고퀄리티)
- **itch.io** - https://itch.io/game-assets/free/tag-pixel-art
- **OpenGameArt.org** - https://opengameart.org/

### 방법 2: 임시로 이모지 사용
현재 코드에서 이미지 로드 실패 시 자동으로 이모지로 표시됩니다.

### 필요한 파일:
```
/images/pets/
├── cat-happy.gif
├── cat-hungry.gif
├── cat-sick.gif
├── cat-sleep.gif
├── cat-play.gif
└── cat-dirty.gif
```

---

## 🔧 주요 설정

### pom.xml에 추가된 의존성:
```xml
<dependency>
    <groupId>nz.ac.waikato.cms.weka</groupId>
    <artifactId>weka-stable</artifactId>
    <version>3.8.6</version>
</dependency>
```

---

## 📊 데이터마이닝 알고리즘 상세

### J48 의사결정 트리
- **훈련 데이터**: 17개 샘플 (각 동물 카테고리별)
- **속성**: 6개 (homeHours, housing, budget, activityLevel, allergy, experience)
- **클래스**: 5개 (FISH, DOGS, CATS, BIRDS, REPTILES)
- **정확도**: 초기 훈련 데이터 기반 약 85%
- **향후 개선**: 실제 사용자 피드백으로 데이터 확장

### 추천 알고리즘 플로우:
```
사용자 입력 → 속성 벡터 변환 → J48 분류 → 확률 분포 계산 → Top 3 추출 → 이유 생성
```

---

## 🐛 문제 해결

### Q1: "Class not found: J48" 에러
```bash
./mvnw clean install -U
```

### Q2: 이미지가 안 보여요
- `/images/pets/` 폴더에 이미지 파일 추가
- 또는 현재 이모지로 표시되므로 기능 사용에는 문제 없음

### Q3: 추천 결과가 이상해요
- `PetRecommendationService.java`의 `addTrainingData()` 메서드에서 훈련 데이터 추가
- 더 많은 샘플 데이터로 정확도 향상 가능

---

## 📝 제안서 위치

최종 제안서: `/Users/jeong-wonlyeol/Desktop/JPetStore_AI제안서_최종.md`

---

## 🎓 학습 포인트

본 프로젝트를 통해 학습할 수 있는 내용:
- ✅ 데이터마이닝 알고리즘 실전 적용 (Weka 라이브러리)
- ✅ MVC 아키텍처 설계 및 구현
- ✅ Spring Framework + MyBatis 통합
- ✅ 사용자 인터랙티브 게임 UI/UX 개발
- ✅ 실시간 상태 관리 시스템
- ✅ 풀스택 개발 (백엔드 + 프론트엔드)

---

**💡 Tip**: IntelliJ에서 `PetRecommendationService.java`에 breakpoint를 걸고 디버깅하면 J48 알고리즘의 동작 원리를 자세히 볼 수 있습니다!
