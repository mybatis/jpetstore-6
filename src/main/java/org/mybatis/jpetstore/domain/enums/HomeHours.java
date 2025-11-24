package org.mybatis.jpetstore.domain.enums;

public enum HomeHours {
  LESS_THAN_2("2시간 미만"),
  TWO_TO_SIX("2-6시간"),
  MORE_THAN_6("6시간 이상");

  private final String displayName;

  HomeHours(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
