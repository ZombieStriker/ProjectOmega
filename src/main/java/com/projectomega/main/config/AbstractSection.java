package com.projectomega.main.config;

import org.yaml.snakeyaml.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public abstract class AbstractSection implements Section {
    protected ConfigOption option = new ConfigOption();
    protected Section parent;

    public AbstractSection(Section parent) {
        this.parent = parent;
    }

    @Override
    public String dumpAsYaml() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowUnicode(true);
        options.setIndent(getOptions().getIndent());
        Yaml yaml = new Yaml(options);
        return yaml.dump(this);
    }

    @Override
    public MapSection createMapSection(String path) {
        MapSection section = new MapSection(null, new HashMap<>());
        set(path, section);
        return section;
    }

    @Override
    public ListSection createListSection(String path) {
        ListSection section = new ListSection(null, new ArrayList<>());
        set(path, section);
        return section;
    }

    @Override
    public Section getParent() {
        return parent;
    }

    @Override
    public void set(String path, Object value) {
        String[] split = path.split(Pattern.quote(getOptions().getPathSeparator()));
        Object route = this;
        for (int i = 0; i < split.length - 1 && route instanceof AbstractSection; i++) {
            Object newRoute = ((AbstractSection) route).getInScope(split[i]);
            if (newRoute == null) {
                newRoute = new MapSection((Section) route, new HashMap<>());
                ((AbstractSection) route).setInScope(split[i], newRoute);
            }
            route = newRoute;
        }
        if (route instanceof AbstractSection) {
            ((AbstractSection) route).setInScope(split[split.length - 1], value);
        }
    }

    @Override
    public Optional<MapSection> getMap(String path) {
        return get(path).map(value -> {
            if (value instanceof MapSection) {
                return (MapSection) value;
            }
            if (value instanceof Map) {
                return new MapSection(this, (Map<String, Object>) value);
            }
            return null;
        });
    }

    @Override
    public Optional<ListSection> getList(String path) {
        return get(path).map(value -> {
            if (value instanceof ListSection) {
                return (ListSection) value;
            }
            if (value instanceof List) {
                return new ListSection(this, (List<Object>) value);
            }
            return null;
        });
    }

    @Override
    public Optional<String> getString(String path) {
        return get(path).map(value -> value == null ? null : String.valueOf(value));
    }

    @Override
    public Optional<Integer> getInteger(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Integer.parseInt(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Double> getDouble(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Double.parseDouble(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Long> getLong(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Long.parseLong(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Short> getShort(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Short.parseShort(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Float> getFloat(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Float.parseFloat(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Byte> getByte(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null) {
                    return Byte.parseByte(value);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Character> getCharacter(String path) {
        return getString(path).map(value -> {
            try {
                if (value != null && value.length() > 0) {
                    return value.charAt(0);
                }
            } catch (NumberFormatException e) {
            }
            return null;
        });
    }

    @Override
    public Optional<Boolean> getBoolean(String path) {
        return getString(path).map(value -> {
            if ("true".equals(value)) {
                return true;
            }
            if ("false".equals(value)) {
                return false;
            }
            return null;
        });
    }

    @Override
    public <T> Optional<T> getObject(String path, Class<T> type) {
        return getMap(path).map(map -> {
            Method deserializeMethod = null;
            try {
                deserializeMethod = type.getDeclaredMethod("deserialize", MapSection.class);
            } catch (NoSuchMethodException e) {
            }
            if (deserializeMethod != null && Modifier.isStatic(deserializeMethod.getModifiers())) {
                MapSection fields = map.getMap("=="+type.getName()).orElse(null);
                if (fields != null) {
                    try {
                        Object result = deserializeMethod.invoke(null, fields);
                        if (type.isInstance(result)) {
                            return type.cast(result);
                        }
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getCause());
                    }
                }
            }
            return null;
        });
    }

    @Override
    public Optional<Object> get(String path) {
        String[] split = path.split(Pattern.quote(getOptions().getPathSeparator()));
        Object route = this;
        for (int i = 0; route instanceof AbstractSection && i < split.length; i++) {
            route = ((AbstractSection) route).getInScope(split[i]);
            if (route instanceof Map && !(route instanceof MapSection)) {
                route = new MapSection(this, (Map<String, Object>) route);
            } else if (route instanceof List && !(route instanceof ListSection)) {
                route = new ListSection(this, (List<Object>) route);
            }
        }
        return Optional.ofNullable(route);
    }

    protected abstract Object getInScope(String key);
    protected abstract void setInScope(String key, Object value);

    @Override
    public ConfigOption getOptions() {
        return option;
    }
}
