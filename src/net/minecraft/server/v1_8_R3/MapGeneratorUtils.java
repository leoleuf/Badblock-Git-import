package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.collect.Maps;

public class MapGeneratorUtils {

    public static <K, V> Map<K, V> b(Iterable<K> iterable, Iterable<V> iterable1) {
        return a(iterable, iterable1, Maps.newLinkedHashMap());
    }

    public static <K, V> Map<K, V> a(Iterable<K> iterable, Iterable<V> iterable1, Map<K, V> map) {
        Iterator<V> iterator = iterable1.iterator();
        Iterator<K> iterator1 = iterable.iterator();

        while (iterator1.hasNext()) {
            map.put(iterator1.next(), iterator.next());
        }

        if (iterator.hasNext()) {
            throw new NoSuchElementException();
        } else {
            return map;
        }
    }
}
