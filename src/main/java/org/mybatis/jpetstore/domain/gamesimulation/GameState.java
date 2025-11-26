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

import java.util.List;

public class GameState {
  private int timeHour; // 7, 8, 9 ... 31
  private int health; // 0 ~ 100+
  private int happiness; // 0 ~ 100+
  private int cost; // 누적 비용(원)
  private String message; // “고양이가 배고파합니다…”
  private List<GameOption> options;
  private boolean finished; // 24시간 끝났는지

  public GameState() {
  }

  public GameState(int timeHour, int health, int happiness, int cost, String message) {
    this.timeHour = timeHour;
    this.health = health;
    this.happiness = happiness;
    this.cost = cost;
    this.message = message;
  }

  public int getTimeHour() {
    return timeHour;
  }

  public int getHealth() {
    return health;
  }

  public int getHappiness() {
    return happiness;
  }

  public int getCost() {
    return cost;
  }

  public String getMessage() {
    return message;
  }

  public List<GameOption> getOptions() {
    return options;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setTimeHour(int timeHour) {
    this.timeHour = timeHour;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public void setHappiness(int happiness) {
    this.happiness = happiness;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setOptions(List<GameOption> options) {
    this.options = options;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }
}
