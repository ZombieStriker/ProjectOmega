package com.projectomega.main.events;

import com.projectomega.main.game.*;
import com.projectomega.main.plugin.*;

import java.lang.invoke.*;
import java.util.*;
import java.util.logging.*;

public class EventHandler<T extends Event> {
    private EnumMap<Priority, List<EventSubscription<T>>> subscriptions = new EnumMap<>(Priority.class);

    public void register(EventSubscription<T> eventSubscription) {
        subscriptions.computeIfAbsent(eventSubscription.getPriority(), key -> new ArrayList<>())
                .add(eventSubscription);
    }

    public void unregister(EventSubscription<T> eventSubscription) {
        List<EventSubscription<T>> subscriptions = this.subscriptions.get(eventSubscription.getPriority());
        if (subscriptions != null) {
            subscriptions.remove(eventSubscription);
        }
    }

    public void unregister(OmegaPlugin plugin) {
        subscriptions.forEach(((priority, eventSubscriptions) -> {
            eventSubscriptions.removeIf(eventSubscription -> eventSubscription.getPlugin() == plugin);
        }));
    }

    public void fire(T event) {
        subscriptions.forEach(((priority, eventSubscriptions) -> {
            eventSubscriptions.forEach(subscriptions -> {
                try {
                    subscriptions.handle(event);
                } catch (Throwable throwable) {
                    StackTraceElement[] elements = throwable.getStackTrace();
                    List<StackTraceElement> stackTrace = new ArrayList<>(elements.length);
                    for (StackTraceElement element : elements) stackTrace.add(element);
                    stackTrace.removeIf(c -> c.getClassName().contains(MethodHandle.class.getName()));
                    stackTrace.removeIf(c -> c.getClassName().contains(EventSubscription.class.getName()));
                    stackTrace.removeIf(c -> c.getClassName().contains(EventBus.class.getName()));
                    stackTrace.removeIf(c -> c.getClassName().contains(EventBus.RuntimeEventSubscription.class.getName()));
                    stackTrace.removeIf(c -> c.getClassName().contains("java.util.stream"));
                    throwable.setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
                    Omega.getLogger().log(Level.WARNING, subscriptions.getListenerName() + " threw an exception while handling event", throwable);
                }
            });
        }));
    }
}
