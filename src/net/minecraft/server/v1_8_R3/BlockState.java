package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;

public abstract class BlockState<T extends Comparable<T>> implements IBlockState<T> {

    private final Class<T> a;
    private final String b;

    protected BlockState(String s, Class<T> oclass) {
        this.a = oclass;
        this.b = s;
    }

    @Override
	public String a() {
        return this.b;
    }

    @Override
	public Class<T> b() {
        return this.a;
    }

    @Override
	public String toString() {
        return Objects.toStringHelper(this).add("name", this.b).add("clazz", this.a).add("values", this.c()).toString();
    }

    @Override
	public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            BlockState blockstate = (BlockState) object;

            return this.a.equals(blockstate.a) && this.b.equals(blockstate.b);
        } else {
            return false;
        }
    }

    @Override
	public int hashCode() {
        return 31 * this.a.hashCode() + this.b.hashCode();
    }
}
