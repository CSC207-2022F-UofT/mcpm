package org.hydev.mcpm.utils;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Simple unmodifiable generic pair
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public record Pair<K, V>(K k, V v) implements Map.Entry<K, V>
{
    /**
     * Exception when trying to modify an unmodifiable field
     */
    public static class UnmodifiableException extends RuntimeException {}

    @Override
    public K getKey()
    {
        return k;
    }

    @Override
    public V getValue()
    {
        return v;
    }

    @Override
    public V setValue(V v)
    {
        // Cannot be modified
        throw new UnmodifiableException();
    }

    /**
     * Collect a stream of pairs to a map
     *
     * @return Collector
     */
    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap()
    {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    /**
     * Simpler constructor
     *
     * @param k Key
     * @param v Value
     * @return Pair[K, V]
     */
    public static <K, V> Pair<K, V> of(K k, V v)
    {
        return new Pair<>(k, v);
    }

    @Override
    public String toString()
    {
        return k.toString() + ": " + v.toString();
    }
}
