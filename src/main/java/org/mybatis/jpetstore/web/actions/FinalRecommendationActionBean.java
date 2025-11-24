package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.domain.recommendation.FinalRecommendation;
import org.mybatis.jpetstore.service.FinalRecommendationService;

import java.io.Serializable;

@SessionScope
public class FinalRecommendationActionBean extends AbstractActionBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String FINAL_RECOMMENDATION = "/WEB-INF/jsp/recommendation/FinalRecommendation.jsp";

  @SpringBean
  private transient FinalRecommendationService finalRecommendationService;

  private String sessionId;
  private FinalRecommendation finalRecommendation;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public FinalRecommendation getFinalRecommendation() {
    return finalRecommendation;
  }

  public void setFinalRecommendation(FinalRecommendation finalRecommendation) {
    this.finalRecommendation = finalRecommendation;
  }

  @DefaultHandler
  public Resolution getRecommendation() {
    if (sessionId == null || sessionId.trim().isEmpty()) {
      setMessage("게임 세션 정보가 없습니다.");
      return new ForwardResolution(ERROR);
    }

    try {
      finalRecommendation = finalRecommendationService.getFinalRecommendation(sessionId);

      return new ForwardResolution(FINAL_RECOMMENDATION);

    } catch (Exception e) {
      e.printStackTrace();
      setMessage("최종 추천 중 오류가 발생했습니다: " + e.getMessage());
      return new ForwardResolution(ERROR);
    }
  }
}
