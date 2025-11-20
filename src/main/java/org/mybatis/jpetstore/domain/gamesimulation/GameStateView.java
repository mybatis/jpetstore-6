package org.mybatis.jpetstore.domain.gamesimulation;

import java.io.Serializable;
import java.util.List;

public class GameStateView implements Serializable {

    private String sessionId;      // 이걸로 다음 스텝 요청
    private int timeHour;          // 7, 8, 9...
    private int health;
    private int happiness;
    private int cost;
    private boolean finished;
    private String message;        // “고양이가 배고파합니다…”
    private List<GameOption> options;
    private int finalScore;


    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getTimeHour() { return timeHour; }
    public void setTimeHour(int timeHour) { this.timeHour = timeHour; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getHappiness() { return happiness; }
    public void setHappiness(int happiness) { this.happiness = happiness; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<GameOption> getOptions() { return options; }
    public void setOptions(List<GameOption> options) { this.options = options; }

    public int getFinalScore() { return finalScore; }
    public void setFinalScore(int finalScore) { this.finalScore = finalScore; }
}
