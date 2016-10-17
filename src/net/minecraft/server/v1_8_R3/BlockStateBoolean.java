package net.minecraft.server.v1_8_R3;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

public class BlockStateBoolean extends BlockState<Boolean> {

    private final ImmutableSet<Boolean> a = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

    protected BlockStateBoolean(String s) {
        super(s, Boolean.class);
    }

    @Override
	public Collection<Boolean> c() {
        return this.a;
    }

    public static BlockStateBoolean of(String s) {
        return new BlockStateBoolean(s);
    }

    @Override
	public String a(Boolean obool) {
        return obool.toString();
    }
}
