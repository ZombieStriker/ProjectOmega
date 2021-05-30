package com.projectomega.main.game.events;

import com.projectomega.main.game.events.types.PlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

    public static HashMap<Class<Event>, List<EventListener>> listeners = new HashMap<>();

    public static void call(Event event) {
        if (listeners.containsKey(event.getClass()))
            for (EventListener listener : listeners.get(event.getClass())) {
                listener.onCall(event);
            }
    }

    public static void registerEventListener(Event event, EventListener listener) {
        List<EventListener> eventlisteners = listeners.get(event.getClass());
        if (eventlisteners == null) {
            eventlisteners = new ArrayList<>();
            listeners.put((Class<Event>) event.getClass(), eventlisteners);
        }
        eventlisteners.add(listener);
    }

}
