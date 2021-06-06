package com.projectomega.main.events;


import com.projectomega.main.plugin.OmegaPlugin;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes") // we had to.
public class EventBus {

    public static final EventBus INSTANCE = new EventBus();

    private final Map<Class<? extends Event>, EventHandler> handlerMap = new ConcurrentHashMap<>();

    public void register(@NotNull Object instance) {
        if (instance instanceof EventSubscription) {
            Class<?> eventType = ((Class<?>) ((ParameterizedType) instance.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
            handlerMap.computeIfAbsent(eventType.asSubclass(Event.class), key -> new EventHandler()).register((EventSubscription) instance);
            return;
        }
        EventSubscription.readSubscriptions(instance)
                .forEach((type, listeners) -> {
                    EventHandler handler = handlerMap.computeIfAbsent(
                            type.asSubclass(Event.class),
                            key -> new EventHandler());
                    listeners.forEach(handler::register);
                });
    }

    public void unregister(OmegaPlugin plugin) {
        handlerMap.forEach((type, handler) -> handler.unregister(plugin));
    }

    public <T extends Event> T post(@NotNull T event) {
        EventHandler<T> handler = handlerMap.get(event.getClass());
        if (handler != null) {
            handler.fire(event);
        }
        return event;
    }

    /**
     * An event subscription whose methods return runtime-evaluated values.
     */
    @AllArgsConstructor static final class RuntimeEventSubscription implements EventSubscription {

        private final EventSubscription listener;
        private final String name;
        private final Priority priority;
        private final OmegaPlugin plugin;

        @Override public void handle(@NotNull Event event) throws Throwable {
            listener.handle(event);
        }

        @Override public String getListenerName() {
            return name;
        }

        @Override public Priority getPriority() {
            return priority;
        }
    }
}
