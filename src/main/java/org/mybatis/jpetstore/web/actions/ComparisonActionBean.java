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

import com.fasterxml.jackson.databind.ObjectMapper; // ← 추가

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.service.CatalogService;
import org.mybatis.jpetstore.service.GptComparisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComparisonActionBean implements ActionBean {

  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(ComparisonActionBean.class);

  private ActionBeanContext context;
  private static final ObjectMapper objectMapper = new ObjectMapper(); // ← 추가

  @SpringBean
  private CatalogService catalogService;

  @SpringBean
  private GptComparisonService gptComparisonService;

  @Override
  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  @Override
  public ActionBeanContext getContext() {
    return context;
  }

  @DefaultHandler
  @DontValidate
  public Resolution compareItems() {
    try {
      String itemId1Str = context.getRequest().getParameter("itemId1");
      String itemId2Str = context.getRequest().getParameter("itemId2");

      logger.info("Comparing items: {} vs {}", itemId1Str, itemId2Str);

      Item item1 = catalogService.getItem(itemId1Str);
      Item item2 = catalogService.getItem(itemId2Str);

      HttpSession session = context.getRequest().getSession();
      AccountActionBean accountBean = (AccountActionBean) session.getAttribute("accountBean");
      Account account = new Account();

      if (accountBean != null) {
        account = accountBean.getAccount();
      }

      // GPT 분석 결과 받기
      String gptAnalysisStr = gptComparisonService.compareItems(item1, item2, account);

      logger.info("GPT Analysis String: {}", gptAnalysisStr);

      // ✅ Map으로 응답 데이터 구조화
      Map<String, Object> response = new HashMap<>();
      response.put("item1_id", item1.getItemId());
      response.put("item2_id", item2.getItemId());
      response.put("item1_name", item1.getItemId());
      response.put("item2_name", item2.getItemId());
      response.put("item1_price", "$" + item1.getListPrice());
      response.put("item2_price", "$" + item2.getListPrice());
      response.put("user_living_environment",
          account.getResidenceEnv() != null ? account.getResidenceEnv() : "Not specified");
      response.put("user_pet_size", account.getPetSizePref() != null ? account.getPetSizePref() : "Not specified");
      response.put("user_activity_time",
          account.getActivityTime() != null ? account.getActivityTime() : "Not specified");

      // ✅ gpt_analysis를 Object로 파싱해서 넣기
      try {
        Object gptAnalysisObj = objectMapper.readValue(gptAnalysisStr, Object.class);
        response.put("gpt_analysis", gptAnalysisObj);
        logger.info("GPT Analysis parsed successfully");
      } catch (Exception e) {
        logger.error("Failed to parse GPT analysis JSON: {}", e.getMessage(), e);
        response.put("gpt_analysis", new HashMap<>()); // 빈 객체
      }

      // ✅ Jackson으로 안전하게 JSON 변환
      String fullResponse = objectMapper.writeValueAsString(response);

      logger.info("Final Response JSON: {}", fullResponse);

      return new StreamingResolution("application/json", fullResponse);

    } catch (Exception e) {
      logger.error("Error in compareItems: {}", e.getMessage(), e);
      try {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new StreamingResolution("application/json", objectMapper.writeValueAsString(errorMap));
      } catch (Exception jsonError) {
        // JSON 변환도 실패하면 수동으로
        return new StreamingResolution("application/json", "{\"error\": \"Internal server error\"}");
      }
    }
  }
}
