package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;

public interface IBlockData {

    Collection<IBlockState> a();

    <T extends Comparable<T>> T get(IBlockState<T> iblockstate);

    <T extends Comparable<T>, V extends T> IBlockData set(IBlockState<T> iblockstate, Comparable<?> v0);

    <T extends Comparable<T>> IBlockData a(IBlockState<T> iblockstate);

    ImmutableMap<IBlockState, Comparable> b();

    Block getBlock();
}
