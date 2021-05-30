package com.projectomega.main.game.chat;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonChatBuilder {

    public static final String TEXT = "text";
    private String translate = null;
    private List<JsonChatElement> elements = new ArrayList<>();
    public static final String CHAT_TYPE_TEXT = "chat.type.text";


    public JsonChatBuilder() {

    }

    public JsonChatBuilder setTranslate(String text){
        translate = text;
        return this;
    }

    public void removeElement(int index) {
        this.elements.remove(index);
    }

    public int getElementsSize() {
        return elements.size();
    }

    public JsonChatElement getElement(int index) {
        return elements.get(index);
    }

    public void addElement(JsonChatElement element) {
        elements.add(element);
    }

    public void setElement(int index, JsonChatElement element) {
        elements.set(index, element);
    }


    public String build() {
        JsonObject jsonObject = new JsonObject();
        if (translate != null)
            jsonObject.put("translate", translate);
        JsonArray array = new JsonArray();
        for (JsonChatElement element : elements) {
            array.add(element.build());
        }
        jsonObject.put("with", array);
        return jsonObject.toJson();
    }

    public JsonChatBuilder add(JsonChatElement jsonChatElement) {
        elements.add(jsonChatElement);
        return this;
    }
}
