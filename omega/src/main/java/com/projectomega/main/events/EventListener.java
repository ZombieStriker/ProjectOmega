package com.projectomega.main.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {

    /**
     * The event priority. The higher the priority, the earlier the listener
     * is invoked.
     *
     * @return The event priority.
     */
    Priority priority() default Priority.NORMAL;

}
