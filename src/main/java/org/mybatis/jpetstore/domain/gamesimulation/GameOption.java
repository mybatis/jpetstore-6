package org.mybatis.jpetstore.domain.gamesimulation;

public class GameOption {
    private String id;   // "A", "B", ...
    private String text; // 버튼에 보여줄 문구

    public GameOption() {}

    public GameOption(String id, String text) {
        this.id = id;
        this.text = text;
    }

    //getter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

}

