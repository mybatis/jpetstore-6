package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.recommendation.BreedRecommendation;
import org.mybatis.jpetstore.service.InitialRecommendationService;

import java.io.Serializable;
import java.util.List;

@SessionScope
public class InitialRecommendationActionBean extends AbstractActionBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String INITIAL_RECOMMENDATION = "/WEB-INF/jsp/recommendation/InitialRecommendation.jsp";

  @SpringBean
  private transient InitialRecommendationService initialRecommendationService;

  private List<BreedRecommendation> top3Breeds;

  public List<BreedRecommendation> getTop3Breeds() {
    return top3Breeds;
  }

  public void setTop3Breeds(List<BreedRecommendation> top3Breeds) {
    this.top3Breeds = top3Breeds;
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
      // Top 3 품종 추천 받기
      top3Breeds = initialRecommendationService.getTop3Breeds(account);

      return new ForwardResolution(INITIAL_RECOMMENDATION);

    } catch (Exception e) {
      e.printStackTrace();
      setMessage("추천 중 오류가 발생했습니다: " + e.getMessage());
      return new ForwardResolution(ERROR);
    }
  }
}
