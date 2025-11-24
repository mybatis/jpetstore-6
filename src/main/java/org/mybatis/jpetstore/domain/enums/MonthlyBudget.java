package org.mybatis.jpetstore.domain.enums;

public enum MonthlyBudget {
  UNDER_100K("10만원 미만"),
  BETWEEN_100K_300K("10-30만원"),
  OVER_300K("30만원 이상");

  private final String displayName;

  MonthlyBudget(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
