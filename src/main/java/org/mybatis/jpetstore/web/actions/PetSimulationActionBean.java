package org.mybatis.jpetstore.web.actions;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;

@SessionScope
public class PetSimulationActionBean extends AbstractActionBean {

    private static final long serialVersionUID = 1L;

    private static final String SIMULATION_PAGE = "/WEB-INF/jsp/pet/TamagotchiSimulation.jsp";

    private String petType; // FISH, DOGS, CATS, BIRDS, REPTILES
    private String petName;

    @DefaultHandler
    public Resolution startSimulation() {
        // 기본값 설정
        if (petType == null || petType.isEmpty()) {
            petType = "CATS";
        }
        if (petName == null || petName.isEmpty()) {
            petName = "나비";
        }

        return new ForwardResolution(SIMULATION_PAGE);
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }
}
