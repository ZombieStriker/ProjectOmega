package com.projectomega.main.config;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class ListSection extends AbstractSection implements List<Object> {
    private List<Object> list;

    protected ListSection(Section parent, List<Object> list) {
        super(parent);
        this.list = Objects.requireNonNull(list);
        if (this.list instanceof ListSection) {
            this.list = ((ListSection) this.list).list;
        }
    }

    @Override
    protected void setInScope(String key, Object value) {
        try {
            list.set(Integer.parseInt(key), value);
        } catch (NumberFormatException e) {
        }
        if (value instanceof AbstractSection) {
            ((AbstractSection) value).parent = this;
            ((AbstractSection) value).option = getOptions();
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        Set<String> keys = IntStream.range(0, list.size()).mapToObj(String::valueOf).collect(Collectors.toSet());
        if (deep) {
            keys = new HashSet<>(keys);
            for (Object value : list) {
                if (value instanceof Section) {
                    keys.addAll(((Section) value).getKeys(true));
                }
            }
        }
        return keys;
    }

    @Override
    protected Object getInScope(String key) {
        try {
            return list.get(Integer.parseInt(key));
        } catch (NumberFormatException e) {
        }
        return null;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return list.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<?> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<?> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        list.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
