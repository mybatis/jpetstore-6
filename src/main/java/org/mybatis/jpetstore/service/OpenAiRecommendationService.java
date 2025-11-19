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
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OpenAiRecommendationService {

  private static final Logger logger = LoggerFactory.getLogger(OpenAiRecommendationService.class);
  private static final String OPENAI_API_KEY;
  private static final String OPENAI_MODEL = "gpt-3.5-turbo"; // Or "gpt-4", "gpt-4o" etc.

  private final OpenAiService openAiService;

  static {
    String apiKey = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream is = OpenAiRecommendationService.class.getClassLoader()
          .getResourceAsStream("credentials/openai_credentials.json");
      if (is == null) {
        throw new IOException("openai_credentials.json not found in classpath.");
      }
      JsonNode rootNode = mapper.readTree(is);
      apiKey = rootNode.path("openai_api_key").asText();
      if (apiKey.isEmpty() || "YOUR_OPENAI_API_KEY_HERE".equals(apiKey)) {
        throw new IllegalArgumentException(
            "OPENAI_API_KEY not found or is a placeholder in openai_credentials.json. Please update the file.");
      }
    } catch (IOException e) {
      logger.error("Error reading openai_credentials.json: {}", e.getMessage());
      throw new RuntimeException("Failed to load OpenAI API key from credentials file.", e);
    }
    OPENAI_API_KEY = apiKey;
  }

  public OpenAiRecommendationService() {
    // Set a timeout for the OpenAI API calls
    this.openAiService = new OpenAiService(OPENAI_API_KEY, Duration.ofSeconds(30));
  }

  public String getRecommendation(Account account, List<Product> productList) {
    try {
      StringBuilder promptBuilder = new StringBuilder();
      promptBuilder.append(
          "Based on the following user preferences and available products, recommend 2 to 5 products from the list.\n");
      promptBuilder.append("User Preferences:\n");
      // promptBuilder.append(" - Favourite Category: ").append(account.getFavouriteCategoryId()).append("\n");
      // promptBuilder.append(" - Language Preference: ").append(account.getLanguagePreference()).append("\n");
      promptBuilder.append("  - Living Environment: ").append(account.getResidenceEnv()).append("\n");
      promptBuilder.append("  - Pet Care Period: ").append(account.getCarePeriod()).append("\n");
      promptBuilder.append("  - Pet Color Preference: ").append(account.getPetColorPref()).append("\n");
      promptBuilder.append("  - Pet Size Preference: ").append(account.getPetSizePref()).append("\n");
      promptBuilder.append("  - Activity Time: ").append(account.getActivityTime()).append("\n");
      promptBuilder.append("  - Diet Management: ").append(account.getDietManagement()).append("\n");
      promptBuilder.append("\nAvailable Products (Product ID - Name - Category):\n");
      for (Product product : productList) {
        promptBuilder.append("  - ").append(product.getProductId()).append(" - ").append(product.getName())
            .append(" - ").append(product.getCategoryId()).append("\n");
      }
      promptBuilder.append(
          "\nBased on the 'User Preferences', you MUST recommend between 2 and 5 products from the 'Available Products' list above. DO NOT recommend fewer than 2 or more than 5 products. Output the recommendations as a JSON array of objects, where each object has 'productId' and 'productName' fields. Do not include any other text or explanation outside the JSON. Example output:\n"
              + "[\n" + "    {\"productId\": \"FI-FW-01\", \"productName\": \"Koi\"},\n"
              + "    {\"productId\": \"FI-FW-02\", \"productName\": \"Goldfish\"}\n" + "]");

      String fullPrompt = promptBuilder.toString();
      ChatMessage userMessage = new ChatMessage("user", fullPrompt);

      ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model(OPENAI_MODEL)
          .messages(Collections.singletonList(userMessage)).maxTokens(500) // Limit the response length
          .temperature(0.7) // Creativity level
          .build();

      logger.info("Sending prompt to OpenAI API. Prompt length: {} characters", fullPrompt.length());
      logger.info("Full Prompt sent to OpenAI API:\n{}", fullPrompt);
      String response = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage()
          .getContent();

      logger.info("OpenAI API Response: {}", response);
      return response;

    } catch (Exception e) {
      logger.error("Error calling OpenAI API: {}", e.getMessage(), e);
      return "Error getting recommendation from OpenAI: " + e.getMessage();
    }
  }

  public List<String> listAvailableModels() {
    // This library does not directly expose a simple list of available chat models.
    // Hardcoding common ones for demonstration.
    return List.of("gpt-3.5-turbo", "gpt-4", "gpt-4o");
  }
}
