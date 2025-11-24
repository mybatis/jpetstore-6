package org.mybatis.jpetstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.mybatis.jpetstore.domain.recommendation.BreedRecommendation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InitialRecommendationService {

  private final GeminiClient geminiClient;
  private final CatalogService catalogService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public InitialRecommendationService(GeminiClient geminiClient, CatalogService catalogService) {
    this.geminiClient = geminiClient;
    this.catalogService = catalogService;
  }

  public List<BreedRecommendation> getTop3Breeds(Account account) {
    // 1. DB에서 모든 품종 정보 가져오기
    List<Product> allProducts = getAllProducts();

    // 2. 프롬프트 생성
    String prompt = buildPrompt(account, allProducts);

    // 3. Gemini API 호출
    String response;
    try {
      response = geminiClient.chat(prompt);
    } catch (Exception e) {
      throw new RuntimeException("Gemini API 호출 실패", e);
    }

    // 4. JSON 파싱
    return parseRecommendations(response);
  }

  private List<Product> getAllProducts() {
    List<Product> allProducts = new ArrayList<>();

    // 모든 카테고리의 제품 가져오기
    String[] categories = {"FISH", "DOGS", "CATS", "REPTILES", "BIRDS"};
    for (String categoryId : categories) {
      List<Product> products = catalogService.getProductListByCategory(categoryId);
      if (products != null) {
        allProducts.addAll(products);
      }
    }

    return allProducts;
  }

  private String buildPrompt(Account account, List<Product> products) {
    StringBuilder sb = new StringBuilder();

    sb.append("당신은 반려동물 추천 전문가입니다.\n\n");
    sb.append("사용자의 라이프스타일 정보:\n");
    sb.append("- 나이: ").append(account.getAge() != null ? account.getAge() : "미제공").append("\n");
    sb.append("- 직업: ").append(account.getOccupation() != null ? account.getOccupation() : "미제공").append("\n");
    sb.append("- 하루 재택시간: ").append(getHomeHoursDisplay(account.getHomeHours())).append("\n");
    sb.append("- 주거형태: ").append(getHousingTypeDisplay(account.getHousingType())).append("\n");
    sb.append("- 월 예산: ").append(getMonthlyBudgetDisplay(account.getMonthlyBudget())).append("\n");
    sb.append("- 알레르기 여부: ").append(account.getHasAllergy() != null && account.getHasAllergy() ? "있음" : "없음").append("\n\n");

    sb.append("JPetStore 데이터베이스에 등록된 반려동물 품종 목록:\n");
    for (Product product : products) {
      sb.append("- ID: ").append(product.getProductId())
        .append(", 이름: ").append(product.getName())
        .append(", 카테고리: ").append(product.getCategoryId())
        .append(", 설명: ").append(product.getDescription())
        .append("\n");
    }

    sb.append("\n위 정보를 바탕으로 사용자에게 가장 적합한 반려동물 품종 3개를 추천해주세요.\n");
    sb.append("응답은 반드시 아래 JSON 형식으로만 작성하세요. JSON 코드 블록(```json)을 사용하지 말고, 순수 JSON만 반환하세요:\n\n");
    sb.append("[\n");
    sb.append("  {\n");
    sb.append("    \"breedId\": \"제품ID (예: FI-SW-01)\",\n");
    sb.append("    \"breedName\": \"제품명 (예: Angelfish)\",\n");
    sb.append("    \"confidence\": 85,\n");
    sb.append("    \"reason\": \"추천 이유 (1-2문장)\"\n");
    sb.append("  }\n");
    sb.append("]\n\n");
    sb.append("주의사항:\n");
    sb.append("- 반드시 위에 나열된 품종 목록에서만 선택하세요\n");
    sb.append("- breedId는 정확히 제품ID와 일치해야 합니다\n");
    sb.append("- confidence는 0-100 사이의 정수입니다\n");
    sb.append("- 정확히 3개의 품종을 추천하세요\n");

    return sb.toString();
  }

  private String getHomeHoursDisplay(String homeHours) {
    if (homeHours == null) return "미제공";
    switch (homeHours) {
      case "LESS_THAN_2": return "2시간 미만";
      case "TWO_TO_SIX": return "2-6시간";
      case "MORE_THAN_6": return "6시간 이상";
      default: return homeHours;
    }
  }

  private String getHousingTypeDisplay(String housingType) {
    if (housingType == null) return "미제공";
    switch (housingType) {
      case "STUDIO": return "원룸";
      case "APARTMENT": return "아파트";
      case "HOUSE": return "단독주택";
      default: return housingType;
    }
  }

  private String getMonthlyBudgetDisplay(String monthlyBudget) {
    if (monthlyBudget == null) return "미제공";
    switch (monthlyBudget) {
      case "UNDER_100K": return "10만원 미만";
      case "BETWEEN_100K_300K": return "10-30만원";
      case "OVER_300K": return "30만원 이상";
      default: return monthlyBudget;
    }
  }

  private List<BreedRecommendation> parseRecommendations(String jsonResponse) {
    List<BreedRecommendation> recommendations = new ArrayList<>();

    try {
      // JSON 응답 파싱
      JsonNode rootNode = objectMapper.readTree(jsonResponse);

      // 배열인지 확인
      if (rootNode.isArray()) {
        for (JsonNode node : rootNode) {
          BreedRecommendation recommendation = new BreedRecommendation();
          recommendation.setBreedId(node.get("breedId").asText());
          recommendation.setBreedName(node.get("breedName").asText());
          recommendation.setConfidence(node.get("confidence").asInt());
          recommendation.setReason(node.get("reason").asText());
          recommendations.add(recommendation);
        }
      }

      // 정확히 3개가 아니면 예외 발생
      if (recommendations.size() != 3) {
        throw new RuntimeException("Gemini API returned " + recommendations.size() + " recommendations instead of 3");
      }

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse Gemini API response", e);
    }

    return recommendations;
  }
}
