package com.projectomega.main.game.chat;

import com.github.cliftonlabs.json_simple.JsonObject;

public class JsonChatElement {

    private String text;
    private JsonClickAction actionClickEvent;
    private String clickEvent;
    private JsonHoverAction actionHoverEvent;
    private String hoverEvent;
    private String insertion;

    public JsonChatElement(String message){
        this.text = message;
    }
    public JsonChatElement(String message, String insertion){
        this.text = message;
        this.insertion = insertion;
    }
    public JsonChatElement setClickEventAction(JsonClickAction action, String value){
        clickEvent = value;
        actionClickEvent = action;
        return this;
    }
    public JsonChatElement setHoverEventAction(JsonHoverAction action, String value){
        this.actionHoverEvent = action;
        this.hoverEvent = value;
        return this;
    }
    public JsonChatElement setInsertion(String value){
        this.insertion = value;
        return this;
    }


    public JsonObject build(){
       // return "{\"text\":\""+text+"\""+(clickEvent!=null?",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg Herobrine \"}":"")+(hoverEvent!=null?",\"hoverEvent\":{\"action\":\"show_entity\",\"value\":\"{id:f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2,name:Herobrine}\"}":"")+",\"insertion\":\"Herobrine\"}";
        JsonObject obj = new JsonObject();
        obj.put("text",text);
        if(clickEvent!=null){
            JsonObject clickeventobject = new JsonObject();
            clickeventobject.put("action",actionClickEvent.getAction());
            clickeventobject.put("value",clickEvent);
            obj.put("clickEvent",clickeventobject);
        }
        if(hoverEvent!=null){
            JsonObject clickeventobject = new JsonObject();
            clickeventobject.put("action",actionHoverEvent.getAction());
            clickeventobject.put("value",hoverEvent);
            obj.put("hoverEvent",clickeventobject);
        }
        if(insertion!=null){
            obj.put("insertion",insertion);
        }
        return obj;
    }
}
