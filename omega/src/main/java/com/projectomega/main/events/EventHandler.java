package com.projectomega.main.events;

import com.projectomega.main.game.Omega;
import com.projectomega.main.plugin.OmegaPlugin;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;

public class EventHandler<T extends Event> {

    private final EnumMap<Priority, List<EventSubscription<T>>> subscriptions = new EnumMap<>(Priority.class);

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
        subscriptions.forEach(((priority, sub) -> sub.removeIf(pluginSub -> pluginSub.getPlugin() == plugin)));
    }

    public void fire(T event) {
        subscriptions.forEach(((priority, eventSubscriptions) -> eventSubscriptions.forEach(subscription -> {
            try {
                subscription.handle(event);
            } catch (Throwable throwable) {
                StackTraceElement[] elements = throwable.getStackTrace();
                List<StackTraceElement> stackTrace = new ArrayList<>(elements.length);
                Collections.addAll(stackTrace, elements);
                stackTrace.removeIf(c -> c.getClassName().contains(MethodHandle.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventHandler.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventSubscription.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventBus.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains(EventBus.RuntimeEventSubscription.class.getName()));
                stackTrace.removeIf(c -> c.getClassName().contains("java.util.stream"));
                throwable.setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
                Omega.getLogger().log(Level.WARNING, subscription.getListenerName() + " threw an exception while handling event", throwable);
            }
        })));
    }
}
