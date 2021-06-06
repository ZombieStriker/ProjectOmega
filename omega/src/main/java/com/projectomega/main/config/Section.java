package com.projectomega.main.config;

import org.yaml.snakeyaml.*;

import java.util.*;

public interface Section {
    Section getParent();
    Optional<MapSection> getMap(String path);
    Optional<Boolean> getBoolean(String path);
    Optional<ListSection> getList(String path);
    Optional<String> getString(String path);
    Optional<Integer> getInteger(String path);
    Optional<Double> getDouble(String path);
    Optional<Long> getLong(String path);
    Optional<Short> getShort(String path);
    Optional<Float> getFloat(String path);
    Optional<Byte> getByte(String path);
    Optional<Character> getCharacter(String path);
    Optional<Object> get(String path);
    void set(String path, Object value);
    Set<String> getKeys(boolean deep);
    <T> Optional<T> getObject(String path, Class<T> type);
    ConfigOption getOptions();

    MapSection createMapSection(String path);
    ListSection createListSection(String path);

    String dumpAsYaml();

    static Section loadSectionFromYaml(String yaml) {
        Yaml y = new Yaml();
        Object value = y.load(yaml);
        if (value instanceof Map) {
            return new MapSection(null, (Map<String, Object>) value);
        }
        if (value instanceof List) {
            return new ListSection(null, (List<Object>) value);
        }
        return null;
    }

}
