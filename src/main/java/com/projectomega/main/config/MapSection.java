package com.projectomega.main.config;

import org.jetbrains.annotations.*;

import java.util.*;

public class MapSection extends AbstractSection implements Map<String, Object> {
    private Map<String, Object> map;

    protected MapSection(Section parent, Map<String, Object> map) {
        super(parent);
        this.map = Objects.requireNonNull(map);
        if (this.map instanceof MapSection) {
            this.map = ((MapSection) this.map).map;
        }
    }

    @Override
    protected void setInScope(String key, Object value) {
        map.put(key, value);
        if (value instanceof AbstractSection) {
            ((AbstractSection) value).parent = this;
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        if (deep) {
            Set<String> keys = new HashSet<>(map.keySet());
            for (Object value : map.values()) {
                if (value instanceof Section) {
                    keys.addAll(((Section) value).getKeys(true));
                }
            }
            return keys;
        }
        return map.keySet();
    }

    @Override
    public Object getInScope(String key) {
        return map.get(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
