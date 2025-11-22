package org.mybatis.jpetstore.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * JPetStore 스타일의 단순 Gemini 클라이언트.
 * - 동기 호출
 * - 예외는 RuntimeException으로 단순 처리
 * - 반환값: JSON 문자열 (우리 프롬프트에서 강제한 구조)
 */
public class GeminiClient {

    private String apiKey;      // applicationContext.xml 에서 주입
    private String model;       // 예: "gemini-2.0-flash"
    private String baseUrl = "https://generativelanguage.googleapis.com"; // 기본값

    private final RestTemplate restTemplate = new RestTemplate();

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 기본 모델로 채팅 호출
     */
    public String chat(String prompt) {
        return chatWithModel(prompt, this.model);
    }

    /**
     * 특정 모델명으로 호출하고 JSON 문자열만 반환
     */
    public String chatWithModel(String prompt, String modelName) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Gemini API key is not set");
        }
        if (modelName == null || modelName.isEmpty()) {
            throw new IllegalStateException("Gemini model name is not set");
        }

        Map<String, Object> body = createRequestBody(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/v1beta/models/" + modelName + ":generateContent?key=" + apiKey;

        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        String text = extractContent(response);
        return extractJsonFromGeminiResponse(text);
    }

    /**
     * Gemini API 요청 body 생성
     */
    private Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> body = new HashMap<>();

        Map<String, Object> contents = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);
        parts.add(part);

        contents.put("parts", parts);

        List<Map<String, Object>> contentsList = new ArrayList<>();
        contentsList.add(contents);

        body.put("contents", contentsList);

        // safetySettings (원하면 더 추가 가능)
        Map<String, Object> safetySettings = new HashMap<>();
        safetySettings.put("category", "HARM_CATEGORY_HARASSMENT");
        safetySettings.put("threshold", "BLOCK_NONE");

        List<Map<String, Object>> safetySettingsList = new ArrayList<>();
        safetySettingsList.add(safetySettings);

        body.put("safetySettings", safetySettingsList);

        return body;
    }

    /**
     * Gemini 응답에서 candidates[0].content.parts[0].text 추출
     */
    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> response) {
        if (response == null || !response.containsKey("candidates")) {
            throw new RuntimeException("Invalid Gemini response: no candidates");
        }

        List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) response.get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("Invalid Gemini response: empty candidates");
        }

        Map<String, Object> candidate = candidates.get(0);
        if (candidate == null || !candidate.containsKey("content")) {
            throw new RuntimeException("Invalid Gemini response: missing content");
        }

        Map<String, Object> content = (Map<String, Object>) candidate.get("content");
        if (content == null || !content.containsKey("parts")) {
            throw new RuntimeException("Invalid Gemini response: missing parts");
        }

        List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");

        if (parts == null || parts.isEmpty()) {
            throw new RuntimeException("Invalid Gemini response: empty parts");
        }

        Map<String, Object> firstPart = parts.get(0);
        if (firstPart == null || !firstPart.containsKey("text")) {
            throw new RuntimeException("Invalid Gemini response: missing text");
        }

        return (String) firstPart.get("text");
    }

    /**
     * Gemini 응답 text에서 JSON 부분만 추출
     * - ```json ... ``` 코드블록 제거
     * - ``` ... ``` 코드블록 제거
     * - 코드블록 없으면 그대로 반환
     */
    private String extractJsonFromGeminiResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            throw new RuntimeException("Empty Gemini text response");
        }

        String trimmed = response.trim();

        // ```json 으로 시작하는 경우
        if (trimmed.startsWith("```json")) {
            int startIndex = trimmed.indexOf("```json") + 7;
            int endIndex = trimmed.lastIndexOf("```");
            if (endIndex > startIndex) {
                return trimmed.substring(startIndex, endIndex).trim();
            }
        }

        // ``` 으로 시작 (json 태그 없이 코드블록)
        if (trimmed.startsWith("```")) {
            int startIndex = trimmed.indexOf("```") + 3;
            int endIndex = trimmed.lastIndexOf("```");
            if (endIndex > startIndex) {
                return trimmed.substring(startIndex, endIndex).trim();
            }
        }

        // 코드블록 없으면 그대로 반환 (우리가 프롬프트에서 JSON만 나오게 강제하는 경우)
        return trimmed;
    }
}
