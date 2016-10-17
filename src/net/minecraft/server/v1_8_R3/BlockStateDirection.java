package net.minecraft.server.v1_8_R3;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class BlockStateDirection extends BlockStateEnum<EnumDirection> {

    protected BlockStateDirection(String s, Collection<EnumDirection> collection) {
        super(s, EnumDirection.class, collection);
    }

    public static BlockStateDirection of(String s) {
        return of(s, Predicates.alwaysTrue());
    }

    public static BlockStateDirection of(String s, Predicate<EnumDirection> predicate) {
        return a(s, Collections2.filter(Lists.newArrayList(EnumDirection.values()), predicate));
    }

    public static BlockStateDirection a(String s, Collection<EnumDirection> collection) {
        return new BlockStateDirection(s, collection);
    }
}
