package org.mybatis.jpetstore.domain.recommendation;

import java.io.Serializable;

public class BreedRecommendation implements Serializable {

  private static final long serialVersionUID = 1L;

  private String breedId;
  private String breedName;
  private int confidence;
  private String reason;

  public BreedRecommendation() {
  }

  public BreedRecommendation(String breedId, String breedName, int confidence, String reason) {
    this.breedId = breedId;
    this.breedName = breedName;
    this.confidence = confidence;
    this.reason = reason;
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
}
