package org.mybatis.jpetstore.web.actions;

import java.io.Serializable;
import javax.servlet.http.HttpSession;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.jpetstore.domain.gamesimulation.GameStateView;
import org.mybatis.jpetstore.service.GameSimulationService;

/**
 * 반려동물 돌봄 게임 시뮬레이션용 ActionBean.
 *
 * URL 예시:
 *   - 게임 시작:  actions/GameSimulation.action?startGame=&breedId=CATS
 *   - 다음 턴:    actions/GameSimulation.action?nextStep=&sessionId=...&optionId=A
 */
@SessionScope
public class GameSimulationActionBean extends AbstractActionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private transient GameSimulationService gameSimulationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 요청 파라미터로 받을 것들
    private String breedId;   // 고양이, 강아지… 품종/종류 ID (CATS, DOGS 등)
    private String sessionId; // 게임 세션 식별자
    private String optionId;  // 사용자가 고른 선택지 ID (A/B/C…)

    // ====== 파라미터 getter/setter (Stripes가 자동으로 바인딩) ======
    public String getBreedId() {
        return breedId;
    }

    public void setBreedId(String breedId) {
        this.breedId = breedId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    // ====== 게임 시작 (첫 턴) ======
    // 호출 예: /jpetstore/actions/GameSimulation.action?startGame=&breedId=CATS
    public Resolution startGame() {
        try {
            String accountId = resolveCurrentUsername(); // 로그인 안 돼 있으면 GUEST 등으로 처리

            GameStateView view = gameSimulationService.startGame(accountId, breedId);

            String json = objectMapper.writeValueAsString(view);
            return new StreamingResolution("application/json", json);
        } catch (Exception e) {
            e.printStackTrace();
            // 실서비스면 공통 에러 JSON으로 빼는 게 좋음
            return new StreamingResolution("application/json",
                    "{\"error\":\"failed_to_start_game\"}");
        }
    }

    // ====== 다음 턴 진행 ======
    // 호출 예: /jpetstore/actions/GameSimulation.action?nextStep=&sessionId=...&optionId=A
    public Resolution nextStep() {
        try {
            String accountId = resolveCurrentUsername(); // 필요하면 사용

            GameStateView view = gameSimulationService.nextStep(sessionId, optionId);

            String json = objectMapper.writeValueAsString(view);
            return new StreamingResolution("application/json", json);
        } catch (Exception e) {
            e.printStackTrace();
            return new StreamingResolution("application/json",
                    "{\"error\":\"failed_to_continue_game\"}");
        }
    }

    // ====== 세션에서 로그인한 유저 ID 가져오기 ======
    private String resolveCurrentUsername() {
        HttpSession session = getContext().getRequest().getSession(false);
        if (session == null) {
            return "GUEST";
        }

        // jpetstore에서 AccountActionBean을 이렇게 세션에 넣어둠
        Object bean = session.getAttribute("/actions/Account.action");
        if (bean == null) {
            bean = session.getAttribute("accountBean");
        }

        if (bean instanceof AccountActionBean) {
            AccountActionBean accountBean = (AccountActionBean) bean;
            if (accountBean.isAuthenticated() && accountBean.getAccount() != null) {
                return accountBean.getAccount().getUsername();
            }
        }

        return "GUEST";
    }
}
