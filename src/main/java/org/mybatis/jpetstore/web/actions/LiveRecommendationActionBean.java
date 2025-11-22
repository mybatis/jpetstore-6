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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.SurveyRecommendation;
import org.mybatis.jpetstore.mapper.SurveyRecommendationMapper;
import org.mybatis.jpetstore.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActionBean that triggers a live AI-based recommendation using the user's survey answers.
 */
public class LiveRecommendationActionBean extends AbstractActionBean {

  private static final long serialVersionUID = -4105627783175794034L;
  private static final Logger logger = LoggerFactory.getLogger(LiveRecommendationActionBean.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String VIEW = "/WEB-INF/jsp/survey/LiveRecommendation.jsp";

  @SpringBean
  private transient CatalogService catalogService;

  @SpringBean
  private transient SurveyRecommendationMapper surveyRecommendationMapper;

  private Account account;
  private String recommendationJson;
  private List<Item> recommendedItems = new ArrayList<>();

  public Account getAccount() {
    return account;
  }

  public String getRecommendationJson() {
    return recommendationJson;
  }

  public List<Item> getRecommendedItems() {
    return recommendedItems;
  }

  @DefaultHandler
  public Resolution showRecommendation() {
    HttpSession session = context.getRequest().getSession(false);
    AccountActionBean accountBean = session == null ? null : (AccountActionBean) session.getAttribute("accountBean");

    if (accountBean == null || !accountBean.isAuthenticated()) {
      setMessage("Please sign in before requesting a recommendation.");
      return new RedirectResolution(AccountActionBean.class);
    }

    account = accountBean.getAccount();

    // Look up precomputed recommendations from SURVEY_RECOMMENDATIONS
    logger.info(
        "Requesting recommendation for account survey: residenceEnv='{}', carePeriod='{}', petColorPref='{}', petSizePref='{}', activityTime='{}', dietManagement='{}'",
        account.getResidenceEnv(), account.getCarePeriod(), account.getPetColorPref(), account.getPetSizePref(),
        account.getActivityTime(), account.getDietManagement());

    SurveyRecommendation rec = surveyRecommendationMapper.getSurveyRecommendationBySurvey(account.getResidenceEnv(),
        account.getCarePeriod(), account.getPetColorPref(), account.getPetSizePref(), account.getActivityTime(),
        account.getDietManagement());
    recommendationJson = rec == null ? "" : rec.getRecommendedJsonData();
    if (rec == null) {
      logger.warn("No SURVEY_RECOMMENDATIONS match found for the given survey answers.");
    }
    recommendedItems = buildRecommendedItems(recommendationJson);
    return new ForwardResolution(VIEW);
  }

  private List<Item> buildRecommendedItems(String rawJson) {
    List<Item> items = new ArrayList<>();
    if (rawJson == null || rawJson.isBlank()) {
      return items;
    }
    try {
      JsonNode array = OBJECT_MAPPER.readTree(rawJson);
      if (!array.isArray()) {
        return items;
      }
      Set<String> productIds = new HashSet<>();
      for (JsonNode node : array) {
        String productId = node.path("productId").asText(null);
        if (productId != null && !productId.isBlank() && productIds.add(productId)) {
          List<Item> list = catalogService.getItemListByProduct(productId);
          // Only take the first item for each product so the table matches the JSON rows.
          if (!list.isEmpty()) {
            items.add(list.get(0));
          }
        }
      }
    } catch (Exception e) {
      logger.warn("Failed to parse recommendation JSON: {}", e.getMessage());
    }
    return items;
  }
}
