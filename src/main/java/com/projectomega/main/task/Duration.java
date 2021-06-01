package com.projectomega.main.task;

public class Duration {
    private long ticks;

    private Duration(long ticks) {
        this.ticks = ticks;
    }

    public static Duration ticks(long ticks) {
        return new Duration(ticks);
    }

    public static Duration seconds(long seconds) {
        return new Duration(seconds * 20L);
    }

    public static Duration minutes(long minutes) {
        return new Duration(minutes * 60 * 20L);
    }

    public static Duration hours(long hours) {
        return new Duration(hours * 60 * 60 * 20L);
    }

    public long getDurationInMillis() {
        return ticks * 50L;
    }

    public long getDurationInTicks() {
        return ticks;
    }

    public long getDurationInSeconds() {
        return ticks / 20L;
    }

    public long getDurationInMinutes() {
        return ticks / (60 * 20L);
    }

    public long getDurationInHours() {
        return ticks / (60 * 60 * 20L);
    }

    public Duration getDelta(Duration other) {
        return new Duration(Math.abs(ticks - other.ticks));
    }

    public Duration extendDuration(Duration other) {
        return new Duration(ticks + other.ticks);
    }

    public Duration reduceDuration(Duration other) {
        return new Duration(ticks - other.ticks);
    }
}
