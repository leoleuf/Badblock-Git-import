package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

public class InsensitiveStringMap<V> implements Map<String, V> {

    private final Map<String, V> a = Maps.newLinkedHashMap();

    public InsensitiveStringMap() {}

    @Override
	public int size() {
        return this.a.size();
    }

    @Override
	public boolean isEmpty() {
        return this.a.isEmpty();
    }

    @Override
	public boolean containsKey(Object object) {
        return this.a.containsKey(object.toString().toLowerCase());
    }

    @Override
	public boolean containsValue(Object object) {
        return this.a.containsKey(object);
    }

    @Override
	public V get(Object object) {
        return this.a.get(object.toString().toLowerCase());
    }

    public V a(String s, V v0) {
        return this.a.put(s.toLowerCase(), v0);
    }

    @Override
	public V remove(Object object) {
        return this.a.remove(object.toString().toLowerCase());
    }

    @Override
	public void putAll(Map<? extends String, ? extends V> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            this.a((String) entry.getKey(), (V) entry.getValue());
        }

    }

    @Override
	public void clear() {
        this.a.clear();
    }

    @Override
	public Set<String> keySet() {
        return this.a.keySet();
    }

    @Override
	public Collection<V> values() {
        return this.a.values();
    }

    @Override
	public Set<Entry<String, V>> entrySet() {
        return this.a.entrySet();
    }

    @Override
	public V put(String object, V object1) {
        return this.a(object, object1);
    }
}
