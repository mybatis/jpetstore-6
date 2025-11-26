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

import java.io.Serializable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.recommendation.CategoryRecommendation;
import org.mybatis.jpetstore.service.InitialRecommendationService;

@SessionScope
public class InitialRecommendationActionBean extends AbstractActionBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String INITIAL_RECOMMENDATION = "/WEB-INF/jsp/recommendation/InitialRecommendation.jsp";

  @SpringBean
  private transient InitialRecommendationService initialRecommendationService;

  private List<CategoryRecommendation> top5Categories;

  public List<CategoryRecommendation> getTop5Categories() {
    return top5Categories;
  }

  public void setTop5Categories(List<CategoryRecommendation> top5Categories) {
    this.top5Categories = top5Categories;
  }

  @DefaultHandler
  public Resolution getRecommendations() {
    // 세션에서 account 가져오기
    AccountActionBean accountBean = (AccountActionBean) getContext().getRequest().getSession()
        .getAttribute("/actions/Account.action");

    if (accountBean == null || !accountBean.isAuthenticated()) {
      setMessage("로그인이 필요합니다.");
      return new ForwardResolution(ERROR);
    }

    Account account = accountBean.getAccount();

    // 라이프스타일 정보가 입력되지 않은 경우 체크
    if (account.getAge() == null || account.getHomeHours() == null) {
      setMessage("회원정보를 먼저 입력해주세요.");
      return new ForwardResolution(ERROR);
    }

    try {
      // Top 5 카테고리 추천 받기
      top5Categories = initialRecommendationService.getTop5Categories(account);

      return new ForwardResolution(INITIAL_RECOMMENDATION);

    } catch (Exception e) {
      e.printStackTrace();
      setMessage("추천 중 오류가 발생했습니다: " + e.getMessage());
      return new ForwardResolution(ERROR);
    }
  }
}
