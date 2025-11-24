package org.mybatis.jpetstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mybatis.jpetstore.service.GeminiClient;
import org.mybatis.jpetstore.domain.gamesimulation.GameOption;
import org.mybatis.jpetstore.domain.gamesimulation.GameSession;
import org.mybatis.jpetstore.domain.gamesimulation.GameStateView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameSimulationService {

    private final GameSessionRepository gameSessionRepository;
    private final GeminiClient geminiClient;
    private final GamePromptBuilder promptBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameSimulationService(GameSessionRepository gameSessionRepository,GeminiClient geminiClient,GamePromptBuilder promptBuilder) {
        this.gameSessionRepository = gameSessionRepository;
        this.geminiClient = geminiClient;
        this.promptBuilder = promptBuilder;
    }

    public GameStateView startGame(String accountId, String breedId) throws JsonProcessingException {
        String prompt = promptBuilder.buildStartPrompt(accountId, breedId);
        String json;

        try {
            json = geminiClient.chat(prompt);
        } catch (Exception e) {
            throw new RuntimeException("Gemini API 호출 실패: 잠시 후 다시 시도해주세요.", e);

        }
        GameStateView view = parseGameStateJson(json);

        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession();
        session.setSessionId(sessionId);
        session.setAccountId(accountId);
        session.setBreedId(breedId);
        session.setTimeHour(view.getTimeHour());
        session.setHealth(view.getHealth());
        session.setHappiness(view.getHappiness());
        session.setCost(view.getCost());
        session.setFinished(view.isFinished());
        session.setLastMessage(view.getMessage());
        //마지막 응답내용 저장
        List<GameOption> options = view.getOptions();
        if (options != null) {
            String optionsJson = objectMapper.writeValueAsString(options);
            session.setLastOptionJson(optionsJson);
        }

        gameSessionRepository.saveNew(session);


        view.setSessionId(sessionId);
        return view;
    }

    public GameStateView nextStep(String sessionId, String chosenOptionId) throws JsonProcessingException {
        // 1. 세션 조회
        GameSession session = gameSessionRepository.findById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("GameSession not found: " + sessionId);
        }

        // 2. 직전 상태를 GameStateView 형태로 만들어서 프롬프트에 넘길 준비
        GameStateView lastView = new GameStateView();
        lastView.setSessionId(sessionId);
        lastView.setTimeHour(session.getTimeHour());
        lastView.setHealth(session.getHealth());
        lastView.setHappiness(session.getHappiness());
        lastView.setCost(session.getCost());
        lastView.setFinished(session.isFinished());
        lastView.setMessage(session.getLastMessage());

        if (session.getLastOptionJson() != null) {
            var type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, GameOption.class);
            List<GameOption> options = objectMapper.readValue(session.getLastOptionJson(), type);
            lastView.setOptions(options);
        }

        // 3. 프롬프트 생성 (GamePromptBuilder에 맞춰서)
        String prompt = promptBuilder.buildNextPrompt(session, lastView, chosenOptionId);

        // 4. Gemini 호출해서 JSON 문자열 받기
        String json = geminiClient.chat(prompt);

        // 5. JSON → GameStateView 파싱
        GameStateView newView = parseGameStateJson(json);
        newView.setSessionId(sessionId); // 세션 id 세팅
        // 최종점수 저장
        if (newView.isFinished()) {
            session.setFinalScore(newView.getFinalScore());
        }

        // 6. 세션 상태 업데이트 후 DB 저장
        session.setTimeHour(newView.getTimeHour());
        session.setHealth(newView.getHealth());
        session.setHappiness(newView.getHappiness());
        session.setCost(newView.getCost());
        session.setFinished(newView.isFinished());
        session.setLastMessage(newView.getMessage());

        if (newView.getOptions() != null) {
            session.setLastOptionJson(objectMapper.writeValueAsString(newView.getOptions()));
        }
        else {
            session.setLastOptionJson(null);
        }

        gameSessionRepository.update(session);

        // 7. 프론트/챗봇으로 내려줄 새 상태 반환
        return newView;
    }


    private GameStateView parseGameStateJson(String json) {
        try {
            return objectMapper.readValue(json, GameStateView.class);
        } catch (Exception e) {
            throw new RuntimeException("Gemini JSON 파싱 실패: " + json, e);
        }
    }
}
