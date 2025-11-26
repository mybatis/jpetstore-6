/*
 *    Copyright 2010-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.service;

import java.util.List;

import org.mybatis.jpetstore.domain.gamesimulation.GameOption;
import org.mybatis.jpetstore.domain.gamesimulation.GameSession;
import org.mybatis.jpetstore.domain.gamesimulation.GameStateView;
import org.springframework.stereotype.Component;

/**
 * Gemini에게 보낼 프롬프트를 만들어주는 빌더. - 게임 규칙 설명 - JSON 형식 강제 - 직전 상태/선택 정보 포함
 */
@Component
public class GamePromptBuilder {

  /**
   * 게임 처음 시작할 때 사용하는 프롬프트.
   *
   * @param accountId
   *          유저 ID (필요 없으면 프롬프트에서 안 써도 됨)
   * @param breedName
   *          시뮬레이션할 반려동물 품종 이름 (예: Persian, Bulldog 등)
   */
  public String buildStartPrompt(String accountId, String breedName) {
    return """
        당신은 반려동물 돌봄 시뮬레이션 게임 엔진입니다.
        사용자는 24시간 동안 %s를 돌보는 게임을 진행합니다.

        ### 공통 게임 규칙
        - timeHour는 아침 7시에서 시작합니다. (예: 7, 8, 9 ...)
        - 다음날 아침 7시(=31) 이상이 되면 finished=true로 설정하고 시뮬레이션을 종료합니다.
        - health, happiness, cost는 사용자의 선택에 따라 현실적으로 변화해야 합니다.
          - health : 0 ~ 150 범위
          - happiness : 0 ~ 150 범위
          - cost : 한국 원화 기준 누적 비용 (정수)

        ### 동물별 특별 규칙 (반드시 계속 유지할 것)

        - 이번 시뮬레이션의 대상 동물: "%s"

        - 강아지(대형견·소형견 포함):
            - 산책이 중요하며, 산책 중에 배변을 해결합니다.
            - 산책이 부족하거나 배변을 오래 참으면 health/happiness가 감소합니다.
        - 고양이:
            - 화장실 모래에서만 배변합니다.
            - 화장실 청결이 중요하며, 청소를 소홀하면 health/ happiness가 감소합니다.
            - 환경 변화와 낯선 사람에 예민합니다.
        - 새:
            - 새장 내 환경(물, 모이, 횃대, 목욕, 햇볕) 관리가 중요합니다.
            - 청결, 목욕을 소홀히 하면 health/happiness가 감소합니다.
        - 물고기:
            - 수질 관리(물갈이, pH, 수온)가 매우 중요합니다.
            - 먹이 양 조절이 중요하며, 과식하면 수질이 악화되고 health가 감소합니다.
            - 수온이 적정 범위를 벗어나면 health가 급격히 감소합니다.
        - 파충류:
            - 온습도 관리가 필수이며, 히터와 습도계를 활용해야 합니다.
            - 자외선 조사(UVB 램프)가 필요하며, 부족하면 health가 감소합니다.
            - 먹이는 살아있는 먹이 또는 냉동 먹이를 급여하며, 급여 주기를 지켜야 합니다.

        ### 선택지(옵션) 설계 규칙 (자연스럽고 현실적인 선택 분포)
        - 각 턴의 옵션(options[])은 '누가 봐도 티 나는 극단적인 선택지'가 아니라
            **현실에서 반려동물을 키우다 보면 흔히 발생하는 자연스러운 선택들로 구성합니다.**
        - 선택지는 아래 세 유형 중 최소 두 가지 이상을 섞어 구성합니다.
            1) **적극적이고 이상적인 선택지**
                - 예: 산책을 나간다, 장난감으로 놀아준다, 사료를 급여한다 등
                - health/happiness가 자연스럽게 증가할 수 있음
            2) **겉보기엔 괜찮아 보이지만 완벽하진 않은 선택지** (이 부분이 핵심!)
                - 사용자가 의도적으로 나쁘려는 게 아니라
                    **‘현실에서 바빠서/피곤해서 종종 하게 되는 선택’**
                - 예:
                    - “지금 일 때문에 바빠서 잠깐만 쓰다듬고 넘어간다”
                    - “밖에 나갈 준비가 귀찮아 짧은 산책만 하고 돌아온다”
                    - “게임 중이라 반응은 해주지만 제대로 신경 쓰지는 못한다”
                    - “사료는 줬지만 물은 깜박했다”
                    - health/happiness 변화는 작거나, 한쪽이 오르고 다른 한쪽이 미세하게 떨어지는 등
                    **미묘한 결과**를 만들어야 합니다.
            3) **눈에 띄지 않지만 실제로는 좋지 않은 선택지**
                - 지나치게 부정적인 표현을 사용하지 말고,
                    **실수·방심·게으름 같은 인간적인 상황**으로 표현합니다.
                - 예:
                    - “피곤해서 산책을 조금 미룬다”
                    - “핸드폰을 보다가 강아지가 부르는 걸 바로 알아채지 못한다”
                    - “화장실 청소를 ‘조금만 있다가’ 하려다 까먹는다”
                    - health/happiness는 확실히 감소하지만,
                    **표현 자체는 부드럽고 자연스럽게** 적습니다.
        - 옵션 세 가지 모두
            “누가 봐도 정답/오답처럼 보이도록 만들지 말고”
            **모두 현실적인 인간적 상황처럼 보이도록 구성해야 합니다.**
        - health/happiness는 선택지 난이도에 따라 다음 범위에서 변동합니다:
            - 이상적인 선택: +4 ~ +12
            - 애매한 선택: +1 ~ -4
            - 인간적 실수/게으름 선택: -6 ~ -15
            (하지만 표현은 부드럽고 자연스럽게)

        ### 숫자 표기 규칙 (매우 중요)
        - timeHour, health, happiness, cost, finished 값은 반드시 **단일 리터럴 값**으로만 작성하세요.
          - 예: 7, 100, 50, 0, false
          - 절대 이렇게 쓰면 안 됩니다:
            - 7+3
            - 1,000
            - "3000원"
            - "50"
            - 2900 + 80000
        - cost에는 "원" 같은 단위를 붙이지 말고, 정수 숫자만 사용하세요.

        ### 응답 JSON 형식 (필수)
        아래 형식과 동일한 key만 사용해야 합니다.
        JSON 바깥에 어떤 설명도 쓰지 마세요.
        ```json 같은 코드 블록 표시도 절대 사용하지 마세요.

        {
          "message": "상황 설명 (자연스러운 한국어 한 문단)",
          "timeHour": 7,
          "health": 100,
          "happiness": 50,
          "cost": 0,
          "finished": false,
          "finalScore": 0,
          "options": [
            { "id": "A", "text": "선택지1" },
            { "id": "B", "text": "선택지2" },
            { "id": "C", "text": "선택지3" }
          ]
        }

        ### 지금 할 일
        - timeHour는 **반드시 7**로 설정합니다.
        - 기본 health=100, happiness=50, cost=0, finished=false로 설정합니다.
        - options는 2~3개 정도 제시합니다.
        - "%s"를(을) 처음 집에 데려온 아침 상황을 이야기 형식으로 묘사하세요.
        - 각 동물의 대표적인 습성/관리 포인트(산책, 배변, 화장실, 새장 관리 등)를 message와 options.text에 자연스럽게 반영하세요.
        - 아직 게임 시작 턴이므로 finished는 **반드시 false**로 유지합니다.
        - 응답은 반드시 위 JSON 형식으로만 반환하세요.
        """.formatted(breedName, breedName, breedName);
  }

  /**
   * 다음 스텝으로 넘어갈 때 사용하는 프롬프트.
   *
   * @param session
   *          DB에 저장된 세션 정보
   * @param lastView
   *          우리가 가진 마지막 상태 (프론트에 보냈던 값 기준)
   * @param chosenOptionId
   *          사용자가 고른 옵션 ID (예: "A", "B", "C")
   */
  public String buildNextPrompt(GameSession session, GameStateView lastView, String chosenOptionId) {

    String breedName = session.getBreedId();

    // 직전 턴 상태/옵션 JSON
    String lastStateJson = toStateJsonForPrompt(lastView);
    String lastOptionsJson = toOptionsJsonForPrompt(lastView != null ? lastView.getOptions() : null);

    // 선택한 옵션의 text 추출 (없으면 빈 문자열)
    String chosenOptionText = "";
    if (lastView != null && lastView.getOptions() != null) {
      for (GameOption opt : lastView.getOptions()) {
        if (chosenOptionId != null && chosenOptionId.equals(opt.getId())) {
          chosenOptionText = opt.getText();
          break;
        }
      }
    }

    int prevTimeHour = (lastView != null) ? lastView.getTimeHour() : session.getTimeHour();

    String prompt = """
        당신은 반려동물 돌봄 시뮬레이션 게임 엔진입니다.
        사용자는 24시간 동안 동물을 돌보는 게임을 진행합니다.

        ### 공통 게임 규칙
        - 다음날 아침 7시(timeHour = 31) 이상이 되면 finished=true로 설정하고 시뮬레이션을 종료합니다.
        - health, happiness, cost는 사용자의 선택에 따라 현실적으로 변화해야 합니다.
          - health : 0 ~ 150 범위
          - happiness : 0 ~ 150 범위
          - cost : 한국 원화 기준 누적 비용 (정수)

        ### 동물별 특별 규칙 (반드시 계속 유지할 것)
        - 이번 시뮬레이션의 대상 동물: "%s"
        - 강아지(대형견·소형견 포함):
            - 산책이 중요하며, 산책 중에 배변을 해결합니다.
            - 산책이 부족하거나 배변을 오래 참으면 health/happiness가 감소합니다.
        - 고양이:
            - 화장실 모래에서만 배변합니다.
            - 화장실 청결이 중요하며, 청소를 소홀하면 health/ happiness가 감소합니다.
            - 환경 변화와 낯선 사람에 예민합니다.
        - 새:
            - 새장 내 환경(물, 모이, 횃대, 목욕, 햇볕) 관리가 중요합니다.
            - 청결, 목욕을 소홀히 하면 health/happiness가 감소합니다.
        - 물고기:
            - 수질 관리(물갈이, pH, 수온)가 매우 중요합니다.
            - 먹이 양 조절이 중요하며, 과식하면 수질이 악화되고 health가 감소합니다.
            - 수온이 적정 범위를 벗어나면 health가 급격히 감소합니다.
        - 파충류:
            - 온습도 관리가 필수이며, 히터와 습도계를 활용해야 합니다.
            - 자외선 조사(UVB 램프)가 필요하며, 부족하면 health가 감소합니다.
            - 먹이는 살아있는 먹이 또는 냉동 먹이를 급여하며, 급여 주기를 지켜야 합니다.

        ### 1. 직전 턴 정보
        아래 JSON은 직전 턴에서 사용자에게 보여줬던 상태/선택지입니다.

        lastState:
        %s

        lastOptions:
        %s

        - 직전 턴의 timeHour 값: %d
        - 사용자가 직전 턴에서 실제로 선택한 옵션:
          - id: "%s"
          - text: "%s"

        ### 2. 선택 반영 규칙 (가장 중요한 규칙)
        1. 반드시 위에서 명시한 선택 **id="%s"**, **text="%s"** 이 실제로 실행된 뒤의 결과만을 계산해야 합니다.
        2. 이번 턴의 message에는 이 선택 하나의 결과만을 서술해야 합니다.
           - 사용자가 선택하지 않은 다른 행동(예: 산책, 목욕, 다른 메뉴)은 message에 등장시키지 마세요.
        3. health, happiness, cost 변화는 이 선택의 효과를 현실적으로 반영해야 합니다.

        ### 선택지(옵션) 설계 규칙 (자연스럽고 현실적인 선택 분포)
        - 각 턴의 옵션(options[])은 '누가 봐도 티 나는 극단적인 선택지'가 아니라
            **현실에서 반려동물을 키우다 보면 흔히 발생하는 자연스러운 선택들로 구성합니다.**
        - 선택지는 아래 세 유형 중 최소 두 가지 이상을 섞어 구성합니다.
        1) **적극적이고 이상적인 선택지**
        - 예: 산책을 나간다, 장난감으로 놀아준다, 사료를 급여한다 등
        - health/happiness가 자연스럽게 증가할 수 있음
        2) **겉보기엔 괜찮아 보이지만 완벽하진 않은 선택지** (이 부분이 핵심!)
        - 사용자가 의도적으로 나쁘려는 게 아니라 **‘현실에서 바빠서/피곤해서 종종 하게 되는 선택’**
        - 예:
            - “지금 일 때문에 바빠서 잠깐만 쓰다듬고 넘어간다”
            - “밖에 나갈 준비가 귀찮아 짧은 산책만 하고 돌아온다”
            - “게임 중이라 반응은 해주지만 제대로 신경 쓰지는 못한다”
            - “사료는 줬지만 물은 깜박했다”
            - health/happiness 변화는 작거나, 한쪽이 오르고 다른 한쪽이 미세하게 떨어지는 등
        **미묘한 결과**를 만들어야 합니다.
        3) **눈에 띄지 않지만 실제로는 좋지 않은 선택지**
        - 지나치게 부정적인 표현을 사용하지 말고,
        **실수·방심·게으름 같은 인간적인 상황**으로 표현합니다.
        - 예:
            - “피곤해서 산책을 조금 미룬다”
            - “핸드폰을 보다가 강아지가 부르는 걸 바로 알아채지 못한다”
            - “화장실 청소를 ‘조금만 있다가’ 하려다 까먹는다”
            - health/happiness는 확실히 감소하지만,
        **표현 자체는 부드럽고 자연스럽게** 적습니다.
        - 옵션 세 가지 모두 “누가 봐도 정답/오답처럼 보이도록 만들지 말고 **모두 현실적인 인간적 상황처럼 보이도록 구성해야 합니다.**
        - health/happiness는 선택지 난이도에 따라 다음 범위에서 변동합니다:
            - 이상적인 선택: +4 ~ +12
            - 애매한 선택: +1 ~ -4
            - 인간적 실수/게으름 선택: -6 ~ -15
            (하지만 표현은 부드럽고 자연스럽게)

        ### 3. timeHour 규칙
        - 이번 턴의 timeHour는 **반드시 이전 턴(%d)보다 크거나 같아야 합니다.**
          - timeHour가 이전 값보다 작아지는 경우(예: 21 → 9)는 허용되지 않습니다.
        - 일반적으로 timeHour는 이전 값에서 1~3 정도만 증가시키는 것이 자연스럽습니다.
          - 예: 이전 값이 10이면, 11 또는 12 또는 13 정도로 설정
        - 이번 턴이 게임 종료 턴이라면:
          - timeHour는 **반드시 31 이상**이어야 합니다.
          - 가능하면 31로 맞추는 것을 권장합니다.

        ### 4. 이번 턴에서 해야 할 일
        - 직전 상태(lastState)와 사용자의 실제 선택(id="%s", text="%s")을 기준으로
          이번 턴의 message / timeHour / health / happiness / cost / finished / options / finalScore 값을 계산하세요.
        - finished가 true인 경우 다음의 규칙을 반드시 지키세요.
            - timeHour는 반드시 31 이상이어야 합니다. (가능하면 31로 맞추세요.)
            - options는 반드시 빈 배열([])로 반환합니다.
            - 종합 점수(finalScore)를 계산합니다.
            - finalScore = (health + happiness) / 2
            - 소수점이 생기면 반올림하여 정수로 만듭니다.
                - 예: health=90, happiness=80 → (90+80)/2 = 85 → finalScore=85
            - message 작성 규칙:
                - 이번 턴까지의 전체 흐름을 요약하고, 건강/행복/비용의 최종 상태를 자연스럽게 설명합니다.
                - 사용자가 반려동물을 키우기에 어떤지에 대한 평가 멘트를 한국어로 포함합니다.
                    - 예: "키우기 좋아요.", "다시 고민해보셔도 좋을 것 같아요.", "더 공부하셔야 될 것 같아요." 등
            - **message의 마지막 문장은 반드시 정확히 아래 문장으로 끝나야 합니다.**
                - "시뮬레이션이 종료되었습니다."
        - finished가 false인 경우:
          - finalScore는 null이거나 생략해도 됩니다.
          - 사용자가 다음에 할 수 있는 2~3개의 선택지를 제안하세요.
          - options[].id는 "A", "B", "C" 순으로 사용하세요.
          - options[].text에는 실제 행동 설명을 자연스럽게 한국어로 적으세요.
          - 각 선택지는 동물의 관리 포인트(산책, 배변, 화장실, 새장 관리 등)를 반영해야 합니다.

        ### 5. 숫자 표기 규칙 (필수)
        - timeHour, health, happiness, cost, finished 값은 반드시 **단일 리터럴 값**으로만 작성하세요.
          - 예: 14, 85, 75, 82900, false
          - 절대 이렇게 쓰면 안 됩니다:
            - 2900 + 80000
            - 1,000
            - "3000원"
            - "50"
        - cost에는 "원"과 같은 단위를 붙이지 말고, 정수 숫자만 사용하세요.

        ### 6. 응답 JSON 형식 (반드시 지킬 것)
        아래 형식을 그대로 따르세요.
        JSON 바깥에는 아무 설명도 쓰지 마세요.
        ```json 같은 코드 블록도 절대 사용하지 마세요.

        {
          "message": "상황 설명 (자연스러운 한국어 한 문단)",
          "timeHour": 10,
          "health": 95,
          "happiness": 60,
          "cost": 3000,
          "finished": false,
          "finalScore": 0,
          "options": [
            { "id": "A", "text": "선택지1" },
            { "id": "B", "text": "선택지2" }
          ]
        }
        """;

    // %s / %d 순서: 9개 인자 (문자열/정수 혼합)
    return prompt.formatted(breedName, lastStateJson, // 1: lastState
        lastOptionsJson, // 2: lastOptions
        prevTimeHour, // 3: 직전 timeHour (int)
        chosenOptionId, // 4: 직전 선택 id
        escapeForPrompt(chosenOptionText), // 5: 직전 선택 text
        chosenOptionId, // 6: 규칙용 id
        escapeForPrompt(chosenOptionText), // 7: 규칙용 text
        prevTimeHour, // 8: timeHour 규칙에 들어가는 이전 timeHour
        chosenOptionId, // 9: 마지막 설명용 id (문장 내)
        escapeForPrompt(chosenOptionText) // 10: 마지막 설명용 text (문장 내)
    );
  }

  /**
   * lastView를 프롬프트에 넣기 위한 간단 JSON 문자열 (우리가 Gemini에게 맥락을 보여주기 위한 용도라 대략적인 구조만 있으면 됨)
   */
  private String toStateJsonForPrompt(GameStateView view) {
    if (view == null) {
      return "{}";
    }
    return String.format(
        "{ \"timeHour\": %d, \"health\": %d, \"happiness\": %d, \"cost\": %d, \"finished\": %b, \"message\": \"%s\" }",
        view.getTimeHour(), view.getHealth(), view.getHappiness(), view.getCost(), view.isFinished(),
        escapeForPrompt(view.getMessage()));
  }

  /**
   * options 리스트를 프롬프트에 넣기 위한 JSON 비슷한 문자열
   */
  private String toOptionsJsonForPrompt(List<GameOption> options) {
    if (options == null || options.isEmpty()) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < options.size(); i++) {
      GameOption opt = options.get(i);
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(String.format("{ \"id\": \"%s\", \"text\": \"%s\" }", escapeForPrompt(opt.getId()),
          escapeForPrompt(opt.getText())));
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * 프롬프트 안에 들어가는 문자열에서 큰따옴표/줄바꿈 정도만 간단히 이스케이프
   */
  private String escapeForPrompt(String text) {
    if (text == null) {
      return "";
    }
    return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", " ");
  }
}
