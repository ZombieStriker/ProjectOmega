package com.projectomega.main.game.chat;

public enum JsonHoverAction {

    SHOW_ENTITY("show_entity")
    ;

    private String action;

    JsonHoverAction(String j){
        this.action = j;
    }
    public String getAction(){
        return action;
    }
}
