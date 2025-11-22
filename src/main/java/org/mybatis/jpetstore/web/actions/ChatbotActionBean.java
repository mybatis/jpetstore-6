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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.service.ChatbotService;

/**
 * The Class ChatbotActionBean.
 *
 * @author JPetStore Team
 */
public class ChatbotActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 1L;

  @SpringBean
  private transient ChatbotService chatbotService;

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Handle chatbot message and return JSON response.
   *
   * @return JSON response with chatbot reply
   */
  @DefaultHandler
  public Resolution chat() {
    try {
      String response = chatbotService.getChatResponse(message);

      // JSON 응답 생성
      String jsonResponse = String.format("{\"success\": true, \"response\": \"%s\"}", escapeJson(response));

      // UTF-8 바이트 스트림으로 변환
      byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
      InputStream inputStream = new ByteArrayInputStream(bytes);

      return new StreamingResolution("application/json; charset=UTF-8", inputStream);

    } catch (Exception e) {
      System.err.println("Chatbot error: " + e.getMessage());
      e.printStackTrace();

      String errorJson = "{\"success\": false, \"response\": \"죄송합니다. 일시적인 오류가 발생했습니다.\"}";
      byte[] errorBytes = errorJson.getBytes(StandardCharsets.UTF_8);
      InputStream errorStream = new ByteArrayInputStream(errorBytes);

      return new StreamingResolution("application/json; charset=UTF-8", errorStream);
    }
  }

  /**
   * Escape special characters for JSON.
   */
  private String escapeJson(String str) {
    if (str == null) {
      return "";
    }
    return str.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t",
        "\\t");
  }
}
