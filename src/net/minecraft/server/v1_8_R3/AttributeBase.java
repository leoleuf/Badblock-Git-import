package net.minecraft.server.v1_8_R3;

public abstract class AttributeBase implements IAttribute {

    private final IAttribute a;
    private final String b;
    private final double c;
    private boolean d;

    protected AttributeBase(IAttribute iattribute, String s, double d0) {
        this.a = iattribute;
        this.b = s;
        this.c = d0;
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }

    @Override
	public String getName() {
        return this.b;
    }

    @Override
	public double b() {
        return this.c;
    }

    @Override
	public boolean c() {
        return this.d;
    }

    public AttributeBase a(boolean flag) {
        this.d = flag;
        return this;
    }

    @Override
	public IAttribute d() {
        return this.a;
    }

    @Override
	public int hashCode() {
        return this.b.hashCode();
    }

    @Override
	public boolean equals(Object object) {
        return object instanceof IAttribute && this.b.equals(((IAttribute) object).getName());
    }
}
