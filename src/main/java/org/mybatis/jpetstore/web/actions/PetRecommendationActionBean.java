package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.mybatis.jpetstore.service.PetRecommendationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScope
public class PetRecommendationActionBean extends AbstractActionBean {

    private static final long serialVersionUID = 1L;

    private static final String SURVEY_PAGE = "/WEB-INF/jsp/pet/RecommendationSurvey.jsp";
    private static final String RESULT_PAGE = "/WEB-INF/jsp/pet/RecommendationResult.jsp";

    @SpringBean
    private transient PetRecommendationService recommendationService;

    // 사용자 입력 속성
    private String homeHours;
    private String housing;
    private String budget;
    private String activityLevel;
    private String allergy;
    private String experience;

    // 추천 결과
    private List<PetRecommendationService.PetRecommendation> recommendations;

    @DefaultHandler
    public Resolution showSurvey() {
        return new ForwardResolution(SURVEY_PAGE);
    }

    public Resolution getRecommendation() {
        // 사용자 입력을 Map으로 변환
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("homeHours", homeHours != null ? homeHours : "4");
        userAttributes.put("housing", housing != null ? housing : "아파트");
        userAttributes.put("budget", budget != null ? budget : "10-30만원");
        userAttributes.put("activityLevel", activityLevel != null ? activityLevel : "보통");
        userAttributes.put("allergy", allergy != null ? allergy : "없음");
        userAttributes.put("experience", experience != null ? experience : "없음");

        // 추천 수행
        recommendations = recommendationService.recommendPet(userAttributes);

        return new ForwardResolution(RESULT_PAGE);
    }

    // Getters and Setters
    public String getHomeHours() {
        return homeHours;
    }

    public void setHomeHours(String homeHours) {
        this.homeHours = homeHours;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<PetRecommendationService.PetRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<PetRecommendationService.PetRecommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
