package com.projectomega.main.events;

import com.projectomega.main.events.types.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

    public static HashMap<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

    public static void call(Event event) {
        if (listeners.containsKey(event.getClass()))
            for (EventListener listener : listeners.get(event.getClass())) {
                listener.onCall(event);
            }
    }

    public static void registerEventListener(Class<? extends Event> event, EventListener listener) {
        List<EventListener> eventlisteners = listeners.get(event.getClass());
        if (eventlisteners == null) {
            eventlisteners = new ArrayList<>();
            listeners.put(event, eventlisteners);
        }
        eventlisteners.add(listener);
    }

}
