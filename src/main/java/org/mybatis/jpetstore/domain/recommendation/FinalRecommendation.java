package org.mybatis.jpetstore.domain.recommendation;

import java.io.Serializable;

public class FinalRecommendation implements Serializable {

  private static final long serialVersionUID = 1L;

  private String breedId;
  private String breedName;
  private int confidence;
  private String reason;
  private int gameScore;
  private String gameInsight;
  private Integer initialRank;

  public FinalRecommendation() {
  }

  public FinalRecommendation(String breedId, String breedName, int confidence, String reason,
      int gameScore, String gameInsight, Integer initialRank) {
    this.breedId = breedId;
    this.breedName = breedName;
    this.confidence = confidence;
    this.reason = reason;
    this.gameScore = gameScore;
    this.gameInsight = gameInsight;
    this.initialRank = initialRank;
  }

  public String getBreedId() {
    return breedId;
  }

  public void setBreedId(String breedId) {
    this.breedId = breedId;
  }

  public String getBreedName() {
    return breedName;
  }

  public void setBreedName(String breedName) {
    this.breedName = breedName;
  }

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getGameScore() {
    return gameScore;
  }

  public void setGameScore(int gameScore) {
    this.gameScore = gameScore;
  }

  public String getGameInsight() {
    return gameInsight;
  }

  public void setGameInsight(String gameInsight) {
    this.gameInsight = gameInsight;
  }

  public Integer getInitialRank() {
    return initialRank;
  }

  public void setInitialRank(Integer initialRank) {
    this.initialRank = initialRank;
  }
}
