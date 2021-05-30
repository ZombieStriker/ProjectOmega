package com.projectomega.main.events;


import com.projectomega.main.game.Omega;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@SuppressWarnings("rawtypes") // we had to.
public class EventBus {

    public static final EventBus INSTANCE = new EventBus();

    private final Map<Class<? extends Event>, List<EventSubscription>> subscriptions = new ConcurrentHashMap<>();

    public void register(@NotNull Object instance) {
        if (instance instanceof EventSubscription) {
            Class<?> eventType = ((Class<?>) ((ParameterizedType) instance.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
            subscriptions.computeIfAbsent(eventType.asSubclass(Event.class), c -> new ArrayList<>()).add((EventSubscription<?>) instance);
            return;
        }
        EventSubscription.readSubscriptions(instance)
                .forEach((type, listeners) -> subscriptions.computeIfAbsent(type.asSubclass(Event.class), c -> new ArrayList<>()).addAll(listeners));
    }

    public <T extends Event> T post(@NotNull T event) {
        List<EventSubscription> subs = subscriptions.get(event.getClass());
        if (subs == null || subs.isEmpty()) return event; // event has no subscriptions
        subs.stream().sorted(Comparator.comparingInt(s -> s.getPriority().getPriority())).forEach(sub -> {
            try {
                sub.handle(event);
            } catch (Throwable throwable) {
                List<StackTraceElement> stackTrace = new ArrayList<>();
                Collections.addAll(stackTrace, throwable.getStackTrace());
                stackTrace.removeIf(c -> c.getClassName().contains(MethodHandle.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventSubscription.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventBus.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(RuntimeEventSubscription.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains("java.util.stream"));
                throwable.setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
                Omega.getLogger().log(Level.WARNING, sub.getListenerName() + " threw an exception while handling event", throwable);
            }
        });
        return event;
    }

    private static class EventException extends RuntimeException {

        public EventException(EventSubscription<?> subscription, Throwable cause) {
            super(subscription.getListenerName() + " threw an error when handling event", cause);
            setStackTrace(cause.getStackTrace());
        }
    }

    /**
     * An event subscription whose methods return runtime-evaluated values.
     */
    @AllArgsConstructor static final class RuntimeEventSubscription implements EventSubscription {

        private final EventSubscription listener;
        private final String name;
        private final Priority priority;

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
