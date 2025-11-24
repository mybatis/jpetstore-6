package org.mybatis.jpetstore.domain.gamesimulation;

import java.io.Serializable;

public class GameSession implements Serializable {

    private String sessionId;
    private String accountId;
    private String breedId;
    private int timeHour;
    private int health;
    private int happiness;
    private int cost;
    private boolean finished;
    private String lastMessage;
    private String lastOptionJson;
    private int finalScore;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getBreedId() { return breedId; }
    public void setBreedId(String breedId) { this.breedId = breedId; }

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

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastOptionJson() { return lastOptionJson; }
    public void setLastOptionJson(String lastOptionJson) { this.lastOptionJson = lastOptionJson; }

    public int getFinalScore() { return finalScore; }
    public void setFinalScore(int finalScore) { this.finalScore = finalScore; }
}
