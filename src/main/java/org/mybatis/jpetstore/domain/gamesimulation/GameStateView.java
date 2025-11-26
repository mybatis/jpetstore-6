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
package org.mybatis.jpetstore.domain.gamesimulation;

import java.io.Serializable;
import java.util.List;

public class GameStateView implements Serializable {
  private static final long serialVersionUID = 1234L;

  private String sessionId; // 이걸로 다음 스텝 요청
  private int timeHour; // 7, 8, 9...
  private int health;
  private int happiness;
  private int cost;
  private boolean finished;
  private String message; // “고양이가 배고파합니다…”
  private List<GameOption> options;
  private int finalScore;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public int getTimeHour() {
    return timeHour;
  }

  public void setTimeHour(int timeHour) {
    this.timeHour = timeHour;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getHappiness() {
    return happiness;
  }

  public void setHappiness(int happiness) {
    this.happiness = happiness;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<GameOption> getOptions() {
    return options;
  }

  public void setOptions(List<GameOption> options) {
    this.options = options;
  }

  public int getFinalScore() {
    return finalScore;
  }

  public void setFinalScore(int finalScore) {
    this.finalScore = finalScore;
  }
}
