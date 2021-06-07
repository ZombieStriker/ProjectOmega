package com.projectomega.main.events.asm;

import com.google.common.collect.MapMaker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class GeneratedClassDefiner {

    static final GeneratedClassDefiner INSTANCE = new GeneratedClassDefiner();
    private final ConcurrentMap<ClassLoader, GeneratedClassLoader> loaders = new MapMaker().weakKeys().makeMap();;

    private GeneratedClassDefiner() {
    }

    public Class<?> defineClass(final ClassLoader parentLoader, final String name, final byte[] data) {
        final GeneratedClassLoader loader = loaders.computeIfAbsent(parentLoader, GeneratedClassLoader::new);
        synchronized (loader.getClassLoadingLock(name)) {
            if (loader.hasClass(name)) {
                throw new IllegalStateException(name + " already defined");
            }
            final Class<?> c = loader.define(name, data);
            assert c.getName().equals(name);
            return c;
        }
    }

    private static class GeneratedClassLoader extends ClassLoader {

        protected GeneratedClassLoader(final ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(final String name, final byte[] data) {
            synchronized (getClassLoadingLock(name)) {
                assert !hasClass(name);
                final Class<?> c = defineClass(name, data, 0, data.length);
                resolveClass(c);
                return c;
            }
        }

        public Object getClassLoadingLock(final String name) {
            return super.getClassLoadingLock(name);
        }

        public boolean hasClass(final String name) {
            synchronized (getClassLoadingLock(name)) {
                try {
                    Class.forName(name);
                    return true;
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }

        static {
            ClassLoader.registerAsParallelCapable();
        }
    }
}
