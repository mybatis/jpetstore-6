package org.mybatis.jpetstore.mapper;

import org.mybatis.jpetstore.domain.gamesimulation.GameSession;

public interface GameSessionMapper {

  void insertGameSession(GameSession session);

  void updateGameSession(GameSession session);

  GameSession getGameSession(String sessionId);
}
