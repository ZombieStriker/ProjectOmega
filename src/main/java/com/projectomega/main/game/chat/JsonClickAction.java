package com.projectomega.main.game.chat;

public enum JsonClickAction {

    SUGGEST_COMMAND("suggest_command"),
    OPEN_URL("open_url"),
    ;

    private String action;

    JsonClickAction(String j){
        this.action = j;
    }
    public String getAction(){
        return action;
    }
}
