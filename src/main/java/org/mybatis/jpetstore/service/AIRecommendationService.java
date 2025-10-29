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

  public AIRecommendationService() {
    this.httpClient = HttpClient.newHttpClient();
    // API 키를 환경 변수에서 읽기
    this.apiKey = System.getenv(API_KEY_ENV);
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
   * Build a prompt for the AI based on item information.
   */
  private String buildPrompt(Item item) {
    String productName = item.getProduct().getName();
    String categoryId = item.getProduct().getCategoryId();
    String description = item.getProduct().getDescription();
    String attributes = String.format("%s %s %s %s %s", nullSafe(item.getAttribute1()), nullSafe(item.getAttribute2()),
        nullSafe(item.getAttribute3()), nullSafe(item.getAttribute4()), nullSafe(item.getAttribute5())).trim();

    return String.format(
        "당신은 애완동물 용품 쇼핑몰의 AI 추천 도우미입니다.\n\n" + "고객이 현재 다음 상품을 보고 있습니다:\n" + "- 상품명: %s\n" + "- 카테고리: %s\n"
            + "- 설명: %s\n" + "- 특성: %s\n\n" + "이 고객에게 함께 구매하면 좋을 상품 3가지를 추천해주세요.\n" + "각 추천은 다음 형식으로 작성해주세요:\n"
            + "1. [상품명]: [간단한 추천 이유 (한 문장)]\n" + "2. [상품명]: [간단한 추천 이유 (한 문장)]\n" + "3. [상품명]: [간단한 추천 이유 (한 문장)]\n\n"
            + "실제 JPetStore에 있을 법한 상품을 추천하고, 친근하고 도움이 되는 톤으로 작성해주세요.",
        productName, categoryId, description, attributes);
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
   * Parse the Gemini API response and extract recommendations.
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
              // 숫자나 - 로 시작하는 줄만 추가
              recommendations.add(line);
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
   * Provide demo recommendations when API is not available.
   */
  private List<String> getDemoRecommendations(Item item) {
    List<String> recommendations = new ArrayList<>();
    String categoryId = item.getProduct().getCategoryId();

    switch (categoryId.toUpperCase()) {
      case "FISH":
        recommendations.add("1. 고급 어류 사료: 물고기의 건강한 성장과 선명한 색상 유지에 필수적입니다.");
        recommendations.add("2. 수족관 여과 시스템: 깨끗한 수질 관리로 물고기의 건강을 지켜줍니다.");
        recommendations.add("3. 수초 세트: 자연스러운 서식 환경을 제공하여 물고기의 스트레스를 줄여줍니다.");
        break;
      case "DOGS":
        recommendations.add("1. 프리미엄 강아지 사료: 균형 잡힌 영양으로 반려견의 건강을 책임집니다.");
        recommendations.add("2. 강아지 장난감: 혼자 있는 시간에도 즐겁게 놀 수 있도록 도와줍니다.");
        recommendations.add("3. 산책용 목줄과 하네스: 안전하고 편안한 산책을 위한 필수 용품입니다.");
        break;
      case "CATS":
        recommendations.add("1. 고양이 캣타워: 고양이의 본능적인 등반 욕구를 충족시켜줍니다.");
        recommendations.add("2. 고양이 모래: 청결한 화장실 환경 유지로 스트레스를 줄여줍니다.");
        recommendations.add("3. 고양이 간식: 사랑하는 반려묘와의 교감을 깊게 해줍니다.");
        break;
      case "BIRDS":
        recommendations.add("1. 새 모이: 다양한 영양소가 풍부한 고품질 모이로 건강을 지켜줍니다.");
        recommendations.add("2. 새장 장식품: 새가 즐겁게 놀 수 있는 환경을 만들어줍니다.");
        recommendations.add("3. 새 목욕 용품: 깃털 관리와 위생에 필수적인 아이템입니다.");
        break;
      case "REPTILES":
        recommendations.add("1. 파충류 사료: 종에 맞는 영양 공급으로 건강을 유지합니다.");
        recommendations.add("2. 온도 조절 램프: 파충류에게 필수적인 적정 온도를 제공합니다.");
        recommendations.add("3. 바닥재: 자연 서식지와 유사한 환경을 조성해줍니다.");
        break;
      default:
        recommendations.add("1. 애완동물 종합 영양제: 모든 반려동물의 건강을 지원합니다.");
        recommendations.add("2. 애완동물 위생 용품: 청결한 환경 유지로 질병을 예방합니다.");
        recommendations.add("3. 애완동물 장난감: 재미있는 놀이로 스트레스를 해소해줍니다.");
    }

    return recommendations;
  }

  /**
   * Null-safe string utility.
   */
  private String nullSafe(String str) {
    return str != null ? str : "";
  }
}
