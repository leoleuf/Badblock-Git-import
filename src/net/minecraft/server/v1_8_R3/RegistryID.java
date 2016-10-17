package net.minecraft.server.v1_8_R3;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class RegistryID<T> implements Registry<T> {

    private final IdentityHashMap<T, Integer> a = new IdentityHashMap(512);
    private final List<T> b = Lists.newArrayList();

    public RegistryID() {}

    public void a(T t0, int i) {
        this.a.put(t0, Integer.valueOf(i));

        while (this.b.size() <= i) {
            this.b.add(null);
        }

        this.b.set(i, t0);
    }

    public int b(T t0) {
        Integer integer = this.a.get(t0);

        return integer == null ? -1 : integer.intValue();
    }

    public final T a(int i) {
        return i >= 0 && i < this.b.size() ? this.b.get(i) : null;
    }

    @Override
	public Iterator<T> iterator() {
        return Iterators.filter(this.b.iterator(), Predicates.notNull());
    }
}
