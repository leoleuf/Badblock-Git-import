package net.minecraft.server.v1_8_R3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class AttributeMapServer extends AttributeMapBase {

    private final Set<AttributeInstance> e = Sets.newHashSet();
    protected final Map<String, AttributeInstance> d = new InsensitiveStringMap();

    public AttributeMapServer() {}

    public AttributeModifiable e(IAttribute iattribute) {
        return (AttributeModifiable) super.a(iattribute);
    }

    public AttributeModifiable b(String s) {
        AttributeInstance attributeinstance = super.a(s);

        if (attributeinstance == null) {
            attributeinstance = this.d.get(s);
        }

        return (AttributeModifiable) attributeinstance;
    }

    @Override
	public AttributeInstance b(IAttribute iattribute) {
        AttributeInstance attributeinstance = super.b(iattribute);

        if (iattribute instanceof AttributeRanged && ((AttributeRanged) iattribute).g() != null) {
            this.d.put(((AttributeRanged) iattribute).g(), attributeinstance);
        }

        return attributeinstance;
    }

    @Override
	protected AttributeInstance c(IAttribute iattribute) {
        return new AttributeModifiable(this, iattribute);
    }

    @Override
	public void a(AttributeInstance attributeinstance) {
        if (attributeinstance.getAttribute().c()) {
            this.e.add(attributeinstance);
        }

        Iterator iterator = this.c.get(attributeinstance.getAttribute()).iterator();

        while (iterator.hasNext()) {
            IAttribute iattribute = (IAttribute) iterator.next();
            AttributeModifiable attributemodifiable = this.e(iattribute);

            if (attributemodifiable != null) {
                attributemodifiable.f();
            }
        }

    }

    public Set<AttributeInstance> getAttributes() {
        return this.e;
    }

    public Collection<AttributeInstance> c() {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = this.a().iterator();

        while (iterator.hasNext()) {
            AttributeInstance attributeinstance = (AttributeInstance) iterator.next();

            if (attributeinstance.getAttribute().c()) {
                hashset.add(attributeinstance);
            }
        }

        return hashset;
    }

    @Override
	public AttributeInstance a(String s) {
        return this.b(s);
    }

    @Override
	public AttributeInstance a(IAttribute iattribute) {
        return this.e(iattribute);
    }
}
