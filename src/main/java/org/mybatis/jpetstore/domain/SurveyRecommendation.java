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
package org.mybatis.jpetstore.domain;

import java.io.Serializable;

public class SurveyRecommendation implements Serializable {

  private static final long serialVersionUID = 1L;

  private int surveyRecommendationId;
  private String residenceEnv;
  private String carePeriod;
  private String petColorPref;
  private String petSizePref;
  private String activityTime;
  private String dietManagement;
  private String recommendedJsonData;

  public int getSurveyRecommendationId() {
    return surveyRecommendationId;
  }

  public void setSurveyRecommendationId(int surveyRecommendationId) {
    this.surveyRecommendationId = surveyRecommendationId;
  }

  public String getResidenceEnv() {
    return residenceEnv;
  }

  public void setResidenceEnv(String residenceEnv) {
    this.residenceEnv = residenceEnv;
  }

  public String getCarePeriod() {
    return carePeriod;
  }

  public void setCarePeriod(String carePeriod) {
    this.carePeriod = carePeriod;
  }

  public String getPetColorPref() {
    return petColorPref;
  }

  public void setPetColorPref(String petColorPref) {
    this.petColorPref = petColorPref;
  }

  public String getPetSizePref() {
    return petSizePref;
  }

  public void setPetSizePref(String petSizePref) {
    this.petSizePref = petSizePref;
  }

  public String getActivityTime() {
    return activityTime;
  }

  public void setActivityTime(String activityTime) {
    this.activityTime = activityTime;
  }

  public String getDietManagement() {
    return dietManagement;
  }

  public void setDietManagement(String dietManagement) {
    this.dietManagement = dietManagement;
  }

  public String getRecommendedJsonData() {
    return recommendedJsonData;
  }

  public void setRecommendedJsonData(String recommendedJsonData) {
    this.recommendedJsonData = recommendedJsonData;
  }

  @Override
  public String toString() {
    return "SurveyRecommendation{" + "surveyRecommendationId=" + surveyRecommendationId + ", residenceEnv='"
        + residenceEnv + "'" + ", carePeriod='" + carePeriod + "'" + ", petColorPref='" + petColorPref + "'"
        + ", petSizePref='" + petSizePref + "'" + ", activityTime='" + activityTime + "'" + ", dietManagement='"
        + dietManagement + "'" + ", recommendedJsonData='" + recommendedJsonData + "'" + '}';
  }
}
