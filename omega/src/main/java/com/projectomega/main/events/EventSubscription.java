package com.projectomega.main.events;

import com.projectomega.main.events.asm.ASMEventExecutor;
import com.projectomega.main.events.asm.ASMEventExecutor.EventExecutor;
import com.projectomega.main.plugin.OmegaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    /**
     * Returns the human-friendly name of this listener
     *
     * @return The listener name
     */
    default String getListenerName() {
        return getClass().getName();
    }

    /**
     * Returns the priority of this listener. The higher, the earlier it is invoked.
     *
     * @return The listener priority
     */
    default Priority getPriority() {
        return Priority.NORMAL;
    }

    /**
     * Returns the plugin holding this listener
     *
     * @return The plugin
     */
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
            Class<? extends Event> eventType;
            try {
                eventType = method.getParameterTypes()[0].asSubclass(Event.class);
            } catch (ClassCastException c) {
                throw new IllegalArgumentException("Event parameter " + method.getParameterTypes()[0].getName() + " does not extend " + Event.class);
            }
            sub.putIfAbsent(eventType, new ArrayList<>());
            Priority priority = listener.priority();
            String name = listenerClass.getName() + "." + method.getName() + "(" + eventType.getSimpleName() + ")";
            try {
                method.trySetAccessible();
            } catch (Throwable t) {
                if (!method.isAccessible()) method.setAccessible(true);
            }
            try {
                if (Modifier.isStatic(method.getModifiers())) { // use method handles for static methods
                    MethodHandle handle = MethodHandles.lookup().unreflect(method);
                    sub.get(eventType).add(new EventBus.RuntimeEventSubscription(handle::invokeWithArguments, name, priority, plugin));
                } else {
                    // use ASM to generate direct invocation
                    EventExecutor executor = ASMEventExecutor.createEventExecutor(method, eventType);
                    sub.get(eventType).add(new EventBus.RuntimeEventSubscription(event -> executor.execute(instance, event),
                            name, priority, plugin));
                }
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
