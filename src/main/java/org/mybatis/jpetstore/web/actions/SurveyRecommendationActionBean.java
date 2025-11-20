// test url is http://localhost:8080/jpetstore/actions/SurveyRecommendation.action
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

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.SurveyRecommendation;
import org.mybatis.jpetstore.mapper.SurveyRecommendationMapper;

public class SurveyRecommendationActionBean implements ActionBean {

  private static final String VIEW_SURVEY_RECOMMENDATIONS = "/WEB-INF/jsp/survey/SurveyRecommendationView.jsp";

  @SpringBean
  private transient SurveyRecommendationMapper surveyRecommendationMapper;

  private ActionBeanContext context;
  private List<SurveyRecommendation> surveyRecommendations;

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  public List<SurveyRecommendation> getSurveyRecommendations() {
    return surveyRecommendations;
  }

  public void setSurveyRecommendations(List<SurveyRecommendation> surveyRecommendations) {
    this.surveyRecommendations = surveyRecommendations;
  }

  @DefaultHandler
  public Resolution viewAllSurveyRecommendations() {
    surveyRecommendations = surveyRecommendationMapper.getSurveyRecommendations();
    return new ForwardResolution(VIEW_SURVEY_RECOMMENDATIONS);
  }
}
