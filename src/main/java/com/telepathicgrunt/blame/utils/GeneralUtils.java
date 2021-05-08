package com.telepathicgrunt.blame.utils;

import java.util.Map;
import java.util.stream.Stream;

public class GeneralUtils {

    /**
     * Reverse the lookup of a map by finding all keys for a value
     * Source: https://www.baeldung.com/java-map-key-from-value
     */
    public static <K, V> Stream<K> getKeysForMapValue(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }
}
