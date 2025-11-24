package org.mybatis.jpetstore.domain.gamesimulation;

import java.util.List;

public class GameState {
    private int timeHour;      // 7, 8, 9 ... 31
    private int health;        // 0 ~ 100+
    private int happiness;     // 0 ~ 100+
    private int cost;          // 누적 비용(원)
    private String message;    // “고양이가 배고파합니다…”
    private List<GameOption> options;
    private boolean finished;  // 24시간 끝났는지

    public GameState() {}

    public GameState(int timeHour, int health, int happiness, int cost, String message) {
        this.timeHour = timeHour;
        this.health = health;
        this.happiness = happiness;
        this.cost = cost;
        this.message = message;
    }

    public int getTimeHour() {return timeHour;}
    public int getHealth() {return health;}
    public int getHappiness() {return happiness;}
    public int getCost() {return cost;}
    public String getMessage() {return message;}
    public List<GameOption> getOptions() {return options;}
    public boolean isFinished() {return finished;}
    public void setTimeHour(int timeHour) {this.timeHour = timeHour;}
    public void setHealth(int health) {this.health = health;}
    public void setHappiness(int happiness) {this.happiness = happiness;}
    public void setCost(int cost) {this.cost = cost;}
    public void setMessage(String message) {this.message = message;}
    public void setOptions(List<GameOption> options) {this.options = options;}
    public void setFinished(boolean finished) {this.finished = finished;}
}
