package org.mybatis.jpetstore.domain.enums;

public enum HousingType {
  STUDIO("원룸"),
  APARTMENT("아파트"),
  HOUSE("단독주택");

  private final String displayName;

  HousingType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
