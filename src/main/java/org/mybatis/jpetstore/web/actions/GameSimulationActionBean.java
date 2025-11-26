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
package org.mybatis.jpetstore.web.actions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.gamesimulation.GameStateView;
import org.mybatis.jpetstore.service.GameSimulationService;

/**
 * 반려동물 돌봄 게임 시뮬레이션용 ActionBean. URL 예시: - 게임 시작: actions/GameSimulation.action?startGame=&categoryId=CATS - 다음 턴:
 * actions/GameSimulation.action?nextStep=&sessionId=...&optionId=A
 */
@SessionScope
public class GameSimulationActionBean extends AbstractActionBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String PET_SIMULATION = "/WEB-INF/jsp/game/PetSimulation.jsp";

  @SpringBean
  private transient GameSimulationService gameSimulationService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  // 요청 파라미터로 받을 것들
  private String categoryId; // 반려동물 카테고리 ID (CATS, DOGS, FISH, REPTILES, BIRDS 등)
  private String sessionId; // 게임 세션 식별자
  private String optionId; // 사용자가 고른 선택지 ID (A/B/C…)

  // ====== 파라미터 getter/setter (Stripes가 자동으로 바인딩) ======
  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  // ====== 게임 페이지 표시 ======
  public Resolution startPage() {
    return new ForwardResolution(PET_SIMULATION);
  }

  // ====== 게임 시작 (첫 턴) ======
  public Resolution startGame() {
    try {
      String accountId = resolveCurrentUsername(); // 로그인 안 돼 있으면 GUEST 등으로 처리

      GameStateView view = gameSimulationService.startGame(accountId, categoryId);

      String json = objectMapper.writeValueAsString(view);
      return new StreamingResolution("application/json", json);
    } catch (Exception e) {
      e.printStackTrace();
      // 실서비스면 공통 에러 JSON으로 빼는 게 좋음
      return new StreamingResolution("application/json", "{\"error\":\"failed_to_start_game\"}");
    }
  }

  // ====== 다음 턴 진행 ======
  public Resolution nextStep() {
    try {
      String accountId = resolveCurrentUsername(); // 필요하면 사용

      GameStateView view = gameSimulationService.nextStep(sessionId, optionId);

      String json = objectMapper.writeValueAsString(view);
      return new StreamingResolution("application/json", json);
    } catch (Exception e) {
      e.printStackTrace();
      return new StreamingResolution("application/json", "{\"error\":\"failed_to_continue_game\"}");
    }
  }

  // ====== 세션에서 로그인한 유저 ID 가져오기 ======
  private String resolveCurrentUsername() {
    HttpSession session = getContext().getRequest().getSession(false);
    if (session == null) {
      return "GUEST";
    }

    // jpetstore에서 AccountActionBean을 이렇게 세션에 넣어둠
    Object bean = session.getAttribute("/actions/Account.action");
    if (bean == null) {
      bean = session.getAttribute("accountBean");
    }

    if (bean instanceof AccountActionBean) {
      AccountActionBean accountBean = (AccountActionBean) bean;
      if (accountBean.isAuthenticated() && accountBean.getAccount() != null) {
        return accountBean.getAccount().getUsername();
      }
    }

    return "GUEST";
  }
}
