package com.projectomega.main.events;

import com.projectomega.main.plugin.*;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an event listener
 */
public interface EventSubscription<T extends Event> {

    /**
     * Handles the event
     *
     * @param event event to handle
     */
    void handle(@NotNull T event) throws Throwable;

    default String getListenerName() {
        return getClass().getName();
    }

    default Priority getPriority() {
        return Priority.NORMAL;
    }

    default OmegaPlugin getPlugin() {
        return null;
    }

    static @NotNull Map<Class<?>, List<EventSubscription<?>>> readSubscriptions(@NotNull Object instance) {
        Class<?> listenerClass = (instance instanceof Class ? ((Class<?>) instance) : instance.getClass());
        OmegaPlugin plugin = OmegaPlugin.getPlugin(listenerClass);
        Map<Class<?>, List<EventSubscription<?>>> sub = new HashMap<>();
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (method.getParameterCount() != 1) continue;
            EventListener listener = method.getAnnotation(EventListener.class);
            if (listener == null) continue;
            Class<?> eventType = method.getParameterTypes()[0];
            sub.putIfAbsent(eventType, new ArrayList<>());
            Priority priority = listener.priority();
            String name = listenerClass.getName() + "." + method.getName() + "(" + eventType.getSimpleName() + ")";
            try {
                method.trySetAccessible();
            } catch (Throwable t) {
                if (!method.isAccessible()) method.setAccessible(true);
            }
            try {
                MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(instance);
                sub.get(eventType).add(new EventBus.RuntimeEventSubscription(handle::invokeWithArguments, name, priority, plugin));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (Field field : listenerClass.getDeclaredFields()) {
            if (!EventSubscription.class.isAssignableFrom(field.getType())) continue;
            EventListener listener = field.getAnnotation(EventListener.class);
            if (listener == null) continue;
            Class<?> eventType;
            try {
                eventType = (Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                sub.putIfAbsent(eventType, new ArrayList<>());
            } catch (ClassCastException c) {
                throw new IllegalArgumentException("Couldn't evaluate event type in field " + listenerClass.getName() + "." + field.getName() + ". Is it missing generics?");
            }
            String name = listenerClass.getName() + "." + field.getName() + "(" + eventType.getSimpleName() + ")";
            try {
                field.trySetAccessible();
            } catch (Throwable t) {
                if (!field.isAccessible()) field.setAccessible(true);
            }
            try {
                EventSubscription<?> subscription = (EventSubscription<?>) field.get(instance);
                sub.get(eventType).add(new EventBus.RuntimeEventSubscription(subscription, name, listener.priority(), plugin));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sub;
    }

}
