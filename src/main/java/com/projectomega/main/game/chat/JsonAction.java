package com.projectomega.main.game.chat;

public enum JsonAction {

    SUGGEST_COMMAND("suggest_command"),
    SHOW_ENTITY("show_entity")
    ;

    private String action;

    JsonAction(String j){
        this.action = j;
    }
    public String getAction(){
        return action;
    }
}
