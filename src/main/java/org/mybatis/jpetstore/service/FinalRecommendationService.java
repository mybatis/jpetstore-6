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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.gamesimulation.GameSession;
import org.mybatis.jpetstore.domain.recommendation.FinalRecommendation;
import org.springframework.stereotype.Service;

@Service
public class FinalRecommendationService {

  private final GeminiClient geminiClient;
  private final CatalogService catalogService;
  private final GameSessionRepository gameSessionRepository;
  private final AccountService accountService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public FinalRecommendationService(GeminiClient geminiClient, CatalogService catalogService,
      GameSessionRepository gameSessionRepository, AccountService accountService) {
    this.geminiClient = geminiClient;
    this.catalogService = catalogService;
    this.gameSessionRepository = gameSessionRepository;
    this.accountService = accountService;
  }

  public FinalRecommendation getFinalRecommendation(String sessionId) {
    GameSession session = gameSessionRepository.findById(sessionId);
    if (session == null) {
      throw new IllegalArgumentException("게임 세션을 찾을 수 없습니다: " + sessionId);
    }

    if (!session.isFinished()) {
      throw new IllegalStateException("게임이 아직 종료되지 않았습니다.");
    }

    Account account = accountService.getAccount(session.getAccountId());
    if (account == null) {
      throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다: " + session.getAccountId());
    }

    // 게임에서 플레이한 카테고리의 품종들만 가져오기
    String categoryId = session.getBreedId(); // breedId는 실제로 categoryId를 저장
    List<Product> categoryProducts = getProductsByCategory(categoryId);

    String prompt = buildPrompt(account, session, categoryProducts, categoryId);

    String response;
    try {
      response = geminiClient.chat(prompt);
    } catch (Exception e) {
      throw new RuntimeException("Gemini API 호출 실패", e);
    }

    return parseRecommendation(response, categoryId, session.getFinalScore());
  }

  private List<Product> getProductsByCategory(String categoryId) {
    List<Product> products = catalogService.getProductListByCategory(categoryId);
    if (products == null) {
      return new ArrayList<>();
    }
    return products;
  }

  private String buildPrompt(Account account, GameSession session, List<Product> products, String categoryId) {
    StringBuilder sb = new StringBuilder();

    sb.append("당신은 반려동물 추천 전문가입니다.\n\n");
    sb.append("사용자의 라이프스타일 정보:\n");
    sb.append("- 나이: ").append(account.getAge() != null ? account.getAge() : "미제공").append("\n");
    sb.append("- 직업: ").append(account.getOccupation() != null ? account.getOccupation() : "미제공").append("\n");
    sb.append("- 하루 재택시간: ").append(getHomeHoursDisplay(account.getHomeHours())).append("\n");
    sb.append("- 주거형태: ").append(getHousingTypeDisplay(account.getHousingType())).append("\n");
    sb.append("- 월 예산: ").append(getMonthlyBudgetDisplay(account.getMonthlyBudget())).append("\n");
    sb.append("- 알레르기 여부: ").append(account.getHasAllergy() != null && account.getHasAllergy() ? "있음" : "없음")
        .append("\n\n");

    sb.append("사용자가 24시간 반려동물 돌봄 시뮬레이션 게임을 플레이한 결과:\n");
    sb.append("- 게임 카테고리: ").append(categoryId).append("\n");
    sb.append("- 최종 점수: ").append(session.getFinalScore()).append("점\n");
    sb.append("- 최종 건강도: ").append(session.getHealth()).append("\n");
    sb.append("- 최종 행복도: ").append(session.getHappiness()).append("\n");
    sb.append("- 총 비용: ").append(session.getCost()).append("원\n\n");

    sb.append("JPetStore의 ").append(categoryId).append(" 카테고리 내 반려동물 품종 목록:\n");
    for (Product product : products) {
      sb.append("- ID: ").append(product.getProductId()).append(", 이름: ").append(product.getName()).append(", 설명: ")
          .append(product.getDescription()).append("\n");
    }

    sb.append("\n위 정보를 바탕으로 ").append(categoryId).append(" 카테고리 내에서 사용자에게 가장 적합한 반려동물 품종 1개를 최종 추천해주세요.\n");
    sb.append("게임 플레이를 통해 발견된 사용자의 행동 패턴(예: 비용 관리 성향, 건강/행복도 우선순위 등)을 분석하고, ");
    sb.append("이를 라이프스타일 정보와 결합하여 최적의 품종을 선택하세요.\n\n");
    sb.append("응답은 반드시 아래 JSON 형식으로만 작성하세요. JSON 코드 블록(```json)을 사용하지 말고, 순수 JSON만 반환하세요:\n\n");
    sb.append("{\n");
    sb.append("  \"breedId\": \"제품ID (예: FI-SW-01)\",\n");
    sb.append("  \"breedName\": \"제품명 (예: Angelfish)\",\n");
    sb.append("  \"confidence\": 90,\n");
    sb.append("  \"reason\": \"최종 추천 이유 (2-3문장, 라이프스타일 + 게임 결과 종합)\",\n");
    sb.append("  \"gameInsight\": \"게임에서 발견된 사용자의 행동 패턴 분석 (2-3문장)\"\n");
    sb.append("}\n\n");
    sb.append("주의사항:\n");
    sb.append("- 반드시 위에 나열된 ").append(categoryId).append(" 카테고리의 품종 목록에서만 선택하세요\n");
    sb.append("- breedId는 정확히 제품ID와 일치해야 합니다\n");
    sb.append("- confidence는 0-100 사이의 정수입니다\n");
    sb.append("- 게임 결과를 반드시 반영하세요\n");

    return sb.toString();
  }

  private String getHomeHoursDisplay(String homeHours) {
    if (homeHours == null)
      return "미제공";
    switch (homeHours) {
      case "LESS_THAN_2":
        return "2시간 미만";
      case "TWO_TO_SIX":
        return "2-6시간";
      case "MORE_THAN_6":
        return "6시간 이상";
      default:
        return homeHours;
    }
  }

  private String getHousingTypeDisplay(String housingType) {
    if (housingType == null)
      return "미제공";
    switch (housingType) {
      case "STUDIO":
        return "원룸";
      case "APARTMENT":
        return "아파트";
      case "HOUSE":
        return "단독주택";
      default:
        return housingType;
    }
  }

  private String getMonthlyBudgetDisplay(String monthlyBudget) {
    if (monthlyBudget == null)
      return "미제공";
    switch (monthlyBudget) {
      case "UNDER_100K":
        return "10만원 미만";
      case "BETWEEN_100K_300K":
        return "10-30만원";
      case "OVER_300K":
        return "30만원 이상";
      default:
        return monthlyBudget;
    }
  }

  private FinalRecommendation parseRecommendation(String jsonResponse, String gameBreedId, int gameScore) {
    try {
      JsonNode rootNode = objectMapper.readTree(jsonResponse);

      FinalRecommendation recommendation = new FinalRecommendation();
      recommendation.setBreedId(rootNode.get("breedId").asText());
      recommendation.setBreedName(rootNode.get("breedName").asText());
      recommendation.setConfidence(rootNode.get("confidence").asInt());
      recommendation.setReason(rootNode.get("reason").asText());
      recommendation.setGameInsight(rootNode.get("gameInsight").asText());
      recommendation.setGameScore(gameScore);
      recommendation.setInitialRank(null);

      return recommendation;

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse Gemini API response", e);
    }
  }
}
