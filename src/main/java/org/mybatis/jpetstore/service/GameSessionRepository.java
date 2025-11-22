package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.gamesimulation.GameSession;
import org.mybatis.jpetstore.mapper.GameSessionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameSessionRepository {

    private final GameSessionMapper gameSessionMapper;

    public GameSessionRepository(GameSessionMapper mapper) {
        this.gameSessionMapper = mapper;
    }

    @Transactional
    public void saveNew(GameSession session) {
        gameSessionMapper.insertGameSession(session);
    }

    @Transactional
    public void update(GameSession session) {
        gameSessionMapper.updateGameSession(session);
    }

    @Transactional(readOnly = true)
    public GameSession findById(String sessionId) {
        return gameSessionMapper.getGameSession(sessionId);
    }

}
