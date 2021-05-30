package com.projectomega.main.events;

public abstract  class EventListener<T> {

    public abstract void onCall(T event);
}
