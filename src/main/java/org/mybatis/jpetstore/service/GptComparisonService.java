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
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GptComparisonService {

  private static final Logger logger = LoggerFactory.getLogger(GptComparisonService.class);
  private static final String OPENAI_API_KEY;
  private static final String OPENAI_MODEL = "gpt-3.5-turbo";

  private final OpenAiService openAiService;

  static {
    String apiKey = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream is = GptComparisonService.class.getClassLoader()
          .getResourceAsStream("credentials/openai_credentials.json");
      if (is == null) {
        throw new IOException("openai_credentials.json not found in classpath.");
      }
      JsonNode rootNode = mapper.readTree(is);
      apiKey = rootNode.path("openai_api_key").asText();
      if (apiKey.isEmpty() || "YOUR_OPENAI_API_KEY_HERE".equals(apiKey)) {
        throw new IllegalArgumentException("OPENAI_API_KEY not found or is a placeholder in openai_credentials.json.");
      }
    } catch (IOException e) {
      logger.error("Error reading openai_credentials.json: {}", e.getMessage());
      throw new RuntimeException("Failed to load OpenAI API key from credentials file.", e);
    }
    OPENAI_API_KEY = apiKey;
  }

  public GptComparisonService() {
    this.openAiService = new OpenAiService(OPENAI_API_KEY, Duration.ofSeconds(30));
  }

  public String compareItems(Item item1, Item item2, Account account) {
    try {
      StringBuilder promptBuilder = new StringBuilder();

      String productName1 = item1.getProduct().getName();
      String productName2 = item2.getProduct().getName();
      BigDecimal price1 = item1.getListPrice();
      BigDecimal price2 = item2.getListPrice();

      String residenceEnv = account.getResidenceEnv();
      String petSizePref = account.getPetSizePref();
      String activityTime = account.getActivityTime();
      String attr1Item1 = item1.getAttribute1();
      String attr1Item2 = item2.getAttribute1();

      promptBuilder.append("Please compare the following two pets based on the user's preferences.\n\n");

      promptBuilder.append("User Preferences:\n");
      promptBuilder.append("  - Living Environment: ").append(residenceEnv != null ? residenceEnv : "Not specified")
          .append("\n");
      promptBuilder.append("  - Pet Size Preference: ").append(petSizePref != null ? petSizePref : "Not specified")
          .append("\n");
      promptBuilder.append("  - Activity Time: ").append(activityTime != null ? activityTime : "Not specified")
          .append("\n\n");

      promptBuilder.append("Product 1: ").append(productName1).append("\n");
      promptBuilder.append("  - Variant: ").append(attr1Item1).append("\n");
      promptBuilder.append("  - Price: $").append(price1).append("\n\n");

      promptBuilder.append("Product 2: ").append(productName2).append("\n");
      promptBuilder.append("  - Variant: ").append(attr1Item2).append("\n");
      promptBuilder.append("  - Price: $").append(price2).append("\n\n");

      promptBuilder.append("Based on the user's preferences, please compare these two pets:\n");
      promptBuilder.append("1. Price Comparison (compare the prices)\n");
      promptBuilder.append("2. Living Environment Suitability (how suitable for their living environment)\n");
      promptBuilder.append("3. Pet Size Suitability (how suitable for their size preference)\n");
      promptBuilder.append("4. Activity Time Suitability (how suitable for their activity schedule)\n");
      promptBuilder.append("5. When Product 1 is better (specific conditions)\n");
      promptBuilder.append("6. When Product 2 is better (specific conditions)\n");
      promptBuilder.append("7. Final recommendation for this specific user\n\n");

      promptBuilder.append("Return your response ONLY in the following JSON format:\n");
      promptBuilder.append("{\n");
      promptBuilder.append("  \"price\": \"price comparison analysis based on the prices\",\n");
      promptBuilder
          .append("  \"living_environment\": \"how suitable each pet is for the user's living environment\",\n");
      promptBuilder.append("  \"pet_size\": \"how suitable each pet is for the user's pet size preference\",\n");
      promptBuilder
          .append("  \"activity_time\": \"how suitable each pet is for the user's activity time schedule\",\n");
      promptBuilder.append("  \"product1_better\": \"specific conditions where Product 1 is better for this user\",\n");
      promptBuilder.append("  \"product2_better\": \"specific conditions where Product 2 is better for this user\",\n");
      promptBuilder.append(
          "  \"recommendation\": \"final recommendation for which product is better for this specific user and why\"\n");
      promptBuilder.append("}\n");
      promptBuilder.append("Do NOT include any text outside of the JSON format.");

      String fullPrompt = promptBuilder.toString();
      ChatMessage userMessage = new ChatMessage("user", fullPrompt);

      ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model(OPENAI_MODEL)
          .messages(Collections.singletonList(userMessage)).maxTokens(1500).temperature(0.7).build();

      logger.info("Sending comparison prompt to OpenAI API.");
      logger.info("Full Prompt:\n{}", fullPrompt);

      String response = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage()
          .getContent();

      logger.info("OpenAI API Response: {}", response);
      return response;

    } catch (Exception e) {
      logger.error("Error calling OpenAI API for comparison: {}", e.getMessage(), e);
      return "Error getting comparison from OpenAI: " + e.getMessage();
    }
  }
}
