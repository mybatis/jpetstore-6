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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.stereotype.Service;

/**
 * AI-based product recommendation service using Google Gemini API.
 *
 * @author JPetStore Team
 */
@Service
public class AIRecommendationService {

  private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent";
  private static final String API_KEY_ENV = "GEMINI_API_KEY";

  private final HttpClient httpClient;
  private final String apiKey;
  private final CatalogService catalogService;

  public AIRecommendationService(CatalogService catalogService) {
    this.httpClient = HttpClient.newHttpClient();
    this.apiKey = System.getenv(API_KEY_ENV);
    this.catalogService = catalogService;
  }

  /**
   * Get AI-powered product recommendations based on the current item.
   *
   * @param item
   *          the current item being viewed
   *
   * @return list of recommendation strings
   */
  public List<String> getRecommendations(Item item) {
    // API 키가 설정되지 않은 경우 데모 모드
    if (apiKey == null || apiKey.isEmpty()) {
      return getDemoRecommendations(item);
    }

    try {
      String prompt = buildPrompt(item);
      String requestBody = buildRequestBody(prompt);

      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GEMINI_API_URL + "?key=" + apiKey))
          .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return parseResponse(response.body());
      } else {
        System.err.println("Gemini API Error: " + response.statusCode() + " - " + response.body());
        return getDemoRecommendations(item);
      }

    } catch (Exception e) {
      System.err.println("Error calling Gemini API: " + e.getMessage());
      e.printStackTrace();
      return getDemoRecommendations(item);
    }
  }

  /**
   * Build a prompt for the AI based on item information and available products from database.
   */
  private String buildPrompt(Item item) {
    String productName = item.getProduct().getName();
    String categoryId = item.getProduct().getCategoryId();
    String description = item.getProduct().getDescription();
    String attributes = String.format("%s %s %s %s %s", nullSafe(item.getAttribute1()), nullSafe(item.getAttribute2()),
        nullSafe(item.getAttribute3()), nullSafe(item.getAttribute4()), nullSafe(item.getAttribute5())).trim();

    // 데이터베이스에서 같은 카테고리의 다른 제품들 조회
    List<Product> categoryProducts = catalogService.getProductListByCategory(categoryId);
    StringBuilder availableItems = new StringBuilder();
    int itemCount = 0;
    final int MAX_ITEMS = 15; // 프롬프트 길이 제한

    for (Product product : categoryProducts) {
      if (itemCount >= MAX_ITEMS)
        break;

      // 현재 보고 있는 제품은 제외
      if (product.getProductId().equals(item.getProduct().getProductId())) {
        continue;
      }

      List<Item> items = catalogService.getItemListByProduct(product.getProductId());
      for (Item availableItem : items) {
        if (itemCount >= MAX_ITEMS)
          break;

        // 재고가 있는 상품만 추천
        if (catalogService.isItemInStock(availableItem.getItemId())) {
          availableItems.append(String.format("  - [%s] %s (%s) - $%.2f\n", availableItem.getItemId(),
              product.getName(), nullSafe(availableItem.getAttribute1()), availableItem.getListPrice()));
          itemCount++;
        }
      }
    }

    String availableItemsList = availableItems.length() > 0 ? availableItems.toString()
        : "  (현재 같은 카테고리에 추천 가능한 다른 상품이 없습니다)";

    return String.format("당신은 JPetStore 애완동물 쇼핑몰의 AI 추천 도우미입니다.\n\n" + "고객이 현재 다음 상품을 보고 있습니다:\n" + "- 상품명: %s\n"
        + "- 카테고리: %s\n" + "- 설명: %s\n" + "- 특성: %s\n\n" + "다음은 현재 JPetStore에서 판매 중인 같은 카테고리의 상품 목록입니다:\n%s\n"
        + "위 목록에서 고객에게 함께 구매하면 좋을 상품 3가지를 추천해주세요.\n" + "각 추천은 다음 형식으로 작성해주세요 (반드시 [아이템ID]를 포함해야 합니다):\n"
        + "1. [아이템ID] 상품명: 간단한 추천 이유 (한 문장)\n" + "2. [아이템ID] 상품명: 간단한 추천 이유 (한 문장)\n"
        + "3. [아이템ID] 상품명: 간단한 추천 이유 (한 문장)\n\n" + "예시: 1. [EST-2] Small Angelfish: 대형 물고기와 함께 키우기 좋은 소형 어종입니다.\n\n"
        + "친근하고 도움이 되는 톤으로 작성해주세요.", productName, categoryId, description, attributes, availableItemsList);
  }

  /**
   * Build JSON request body for Gemini API.
   */
  private String buildRequestBody(String prompt) {
    // JSON 문자열 이스케이프 처리
    String escapedPrompt = prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
        .replace("\t", "\\t");

    return String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", escapedPrompt);
  }

  /**
   * Parse the Gemini API response and extract recommendations with item links.
   */
  private List<String> parseResponse(String responseBody) {
    List<String> recommendations = new ArrayList<>();

    try {
      // 간단한 JSON 파싱 (라이브러리 없이)
      // "text": "내용" 패턴을 찾음
      int textStart = responseBody.indexOf("\"text\": \"");
      if (textStart != -1) {
        textStart += 9; // "text": " 길이
        int textEnd = responseBody.indexOf("\"", textStart);

        if (textEnd != -1) {
          String content = responseBody.substring(textStart, textEnd);
          // 이스케이프 문자 복원
          content = content.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");

          // 줄바꿈으로 분리하여 추천 항목 추출
          String[] lines = content.split("\n");
          for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && (line.matches("^\\d+\\..*") || line.startsWith("-"))) {
              // [아이템ID] 패턴을 찾아서 링크로 변환
              String processedLine = convertItemIdToLink(line);
              recommendations.add(processedLine);
            }
          }
        }
      }

      // 파싱 실패 시 원본 반환
      if (recommendations.isEmpty()) {
        recommendations.add("AI 추천을 처리하는 중 문제가 발생했습니다.");
      }

    } catch (Exception e) {
      System.err.println("Error parsing Gemini response: " + e.getMessage());
      recommendations.add("AI 추천을 처리하는 중 문제가 발생했습니다.");
    }

    return recommendations;
  }

  /**
   * Convert item IDs in square brackets to clickable links. Example: "[EST-1] Large Angelfish" ->
   * "<a href='...'>EST-1</a> Large Angelfish"
   */
  private String convertItemIdToLink(String text) {
    // [EST-1] 같은 패턴 찾기
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\[([A-Z]+-\\d+)\\]");
    java.util.regex.Matcher matcher = pattern.matcher(text);

    if (matcher.find()) {
      String itemId = matcher.group(1);
      // [EST-1]을 링크로 변환
      String replacement = String.format("<a href='/jpetstore/actions/Catalog.action?viewItem=&itemId=%s'>%s</a>",
          itemId, itemId);
      return matcher.replaceFirst(replacement);
    }

    return text;
  }

  /**
   * Provide demo recommendations when API is not available. Uses actual database products.
   */
  private List<String> getDemoRecommendations(Item item) {
    List<String> recommendations = new ArrayList<>();
    String categoryId = item.getProduct().getCategoryId();

    try {
      // 데이터베이스에서 같은 카테고리의 다른 제품들 조회
      List<Product> categoryProducts = catalogService.getProductListByCategory(categoryId);
      int count = 0;

      for (Product product : categoryProducts) {
        if (count >= 3)
          break;

        // 현재 보고 있는 제품은 제외
        if (product.getProductId().equals(item.getProduct().getProductId())) {
          continue;
        }

        List<Item> items = catalogService.getItemListByProduct(product.getProductId());
        if (!items.isEmpty()) {
          Item firstItem = items.get(0);
          // 재고가 있는 상품만 추천
          if (catalogService.isItemInStock(firstItem.getItemId())) {
            String recommendation = String.format(
                "%d. <a href='/jpetstore/actions/Catalog.action?viewItem=&itemId=%s'>%s</a> %s: %s", count + 1,
                firstItem.getItemId(), firstItem.getItemId(), product.getName(), getDemoReasonByCategory(categoryId));
            recommendations.add(recommendation);
            count++;
          }
        }
      }

      // 추천할 제품이 없으면 일반 메시지
      if (recommendations.isEmpty()) {
        recommendations.add("현재 같은 카테고리에 추천할 다른 제품이 없습니다.");
      }

    } catch (Exception e) {
      System.err.println("Error getting demo recommendations: " + e.getMessage());
      recommendations.add("추천 상품을 불러오는 중 문제가 발생했습니다.");
    }

    return recommendations;
  }

  /**
   * Get generic recommendation reason by category.
   */
  private String getDemoReasonByCategory(String categoryId) {
    switch (categoryId.toUpperCase()) {
      case "FISH":
        return "함께 키우기 좋은 어종입니다";
      case "DOGS":
        return "반려견에게 필요한 제품입니다";
      case "CATS":
        return "고양이가 좋아할 제품입니다";
      case "BIRDS":
        return "새의 건강에 도움이 됩니다";
      case "REPTILES":
        return "파충류 사육에 유용합니다";
      default:
        return "함께 구매하면 좋은 제품입니다";
    }
  }

  /**
   * Null-safe string utility.
   */
  private String nullSafe(String str) {
    return str != null ? str : "";
  }
}
