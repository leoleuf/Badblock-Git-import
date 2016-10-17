package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class AttributeModifiable implements AttributeInstance {

    private final AttributeMapBase a;
    private final IAttribute b;
    private final Map<Integer, Set<AttributeModifier>> c = Maps.newHashMap();
    private final Map<String, Set<AttributeModifier>> d = Maps.newHashMap();
    private final Map<UUID, AttributeModifier> e = Maps.newHashMap();
    private double f;
    private boolean g = true;
    private double h;

    public AttributeModifiable(AttributeMapBase attributemapbase, IAttribute iattribute) {
        this.a = attributemapbase;
        this.b = iattribute;
        this.f = iattribute.b();

        for (int i = 0; i < 3; ++i) {
            this.c.put(Integer.valueOf(i), Sets.newHashSet());
        }

    }

    @Override
	public IAttribute getAttribute() {
        return this.b;
    }

    @Override
	public double b() {
        return this.f;
    }

    @Override
	public void setValue(double d0) {
        if (d0 != this.b()) {
            this.f = d0;
            this.f();
        }
    }

    @Override
	public Collection<AttributeModifier> a(int i) {
        return this.c.get(Integer.valueOf(i));
    }

    @Override
	public Collection<AttributeModifier> c() {
        HashSet hashset = Sets.newHashSet();

        for (int i = 0; i < 3; ++i) {
            hashset.addAll(this.a(i));
        }

        return hashset;
    }

    @Override
	public AttributeModifier a(UUID uuid) {
        return this.e.get(uuid);
    }

    @Override
	public boolean a(AttributeModifier attributemodifier) {
        return this.e.get(attributemodifier.a()) != null;
    }

    @Override
	public void b(AttributeModifier attributemodifier) {
        if (this.a(attributemodifier.a()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        } else {
        	Set object = this.d.get(attributemodifier.b());

            if (object == null) {
                object = Sets.newHashSet();
                this.d.put(attributemodifier.b(), object);
            }

            ((Set) this.c.get(Integer.valueOf(attributemodifier.c()))).add(attributemodifier);
            object.add(attributemodifier);
            this.e.put(attributemodifier.a(), attributemodifier);
            this.f();
        }
    }

    protected void f() {
        this.g = true;
        this.a.a(this);
    }

    @Override
	public void c(AttributeModifier attributemodifier) {
        for (int i = 0; i < 3; ++i) {
            Set set = this.c.get(Integer.valueOf(i));

            set.remove(attributemodifier);
        }

        Set set1 = this.d.get(attributemodifier.b());

        if (set1 != null) {
            set1.remove(attributemodifier);
            if (set1.isEmpty()) {
                this.d.remove(attributemodifier.b());
            }
        }

        this.e.remove(attributemodifier.a());
        this.f();
    }

    @Override
	public double getValue() {
        if (this.g) {
            this.h = this.g();
            this.g = false;
        }

        return this.h;
    }

    private double g() {
        double d0 = this.b();

        AttributeModifier attributemodifier;

        for (Iterator iterator = this.b(0).iterator(); iterator.hasNext(); d0 += attributemodifier.d()) {
            attributemodifier = (AttributeModifier) iterator.next();
        }

        double d1 = d0;

        Iterator iterator1;
        AttributeModifier attributemodifier1;

        for (iterator1 = this.b(1).iterator(); iterator1.hasNext(); d1 += d0 * attributemodifier1.d()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        for (iterator1 = this.b(2).iterator(); iterator1.hasNext(); d1 *= 1.0D + attributemodifier1.d()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        return this.b.a(d1);
    }

    private Collection<AttributeModifier> b(int i) {
        HashSet hashset = Sets.newHashSet(this.a(i));

        for (IAttribute iattribute = this.b.d(); iattribute != null; iattribute = iattribute.d()) {
            AttributeInstance attributeinstance = this.a.a(iattribute);

            if (attributeinstance != null) {
                hashset.addAll(attributeinstance.a(i));
            }
        }

        return hashset;
    }
}
