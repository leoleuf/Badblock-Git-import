package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class RegistryMaterials<K, V> extends RegistrySimple<K, V> implements Registry<V> {

    protected final RegistryID<V> a = new RegistryID();
    protected final Map<V, K> b;

    public RegistryMaterials() {
        this.b = ((BiMap) this.c).inverse();
    }

    public void a(int i, K k0, V v0) {
        this.a.a(v0, i);
        this.a(k0, v0);
    }

    @Override
	protected Map<K, V> b() {
        return HashBiMap.create();
    }

    @Override
	public V get(K k0) {
        return super.get(k0);
    }

    public K c(V v0) {
        return this.b.get(v0);
    }

    @Override
	public boolean d(K k0) {
        return super.d(k0);
    }

    public int b(V v0) {
        return this.a.b(v0);
    }

    public V a(int i) {
        return this.a.a(i);
    }

    @Override
	public Iterator<V> iterator() {
        return this.a.iterator();
    }
}
