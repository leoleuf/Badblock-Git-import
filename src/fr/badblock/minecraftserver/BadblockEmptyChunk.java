package fr.badblock.minecraftserver;

import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

import com.google.common.base.Predicate;

import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.World;

public class BadblockEmptyChunk extends Chunk {
	public static BiPredicate<Integer, Integer> empty = ((i, j) -> false);
	public static BiPredicate<Integer, Integer> light = ((i, j) -> false);
	
	public BadblockEmptyChunk(World world, int i, int j) {
		super(world, i, j);
	}

	@Override
	public boolean a(int i, int j) {
		return i == this.locX && j == this.locZ;
	}

	@Override
	public int b(int i, int j) {
		return 0;
	}

	@Override
	public void initLighting() {}

	@Override
	public Block getType(BlockPosition blockposition) {
		return Blocks.AIR;
	}

	@Override
	public int b(BlockPosition blockposition) {
		return 255;
	}

	@Override
	public int c(BlockPosition blockposition) {
		return 0;
	}

	@Override
	public int getBrightness(EnumSkyBlock enumskyblock, BlockPosition blockposition) {
		return enumskyblock.c;
	}

	@Override
	public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) {}

	@Override
	public int a(BlockPosition blockposition, int i) {
		return 0;
	}

	@Override
	public void a(Entity entity) {}

	@Override
	public void b(Entity entity) {}

	@Override
	public void a(Entity entity, int i) {}

	@Override
	public boolean d(BlockPosition blockposition) {
		return false;
	}

	@Override
	public TileEntity a(BlockPosition blockposition, Chunk.EnumTileEntityState chunk_enumtileentitystate) {
		return null;
	}

	@Override
	public void a(TileEntity tileentity) {}

	@Override
	public void a(BlockPosition blockposition, TileEntity tileentity) {}

	@Override
	public void e(BlockPosition blockposition) {}

	@Override
	public void addEntities() {}

	@Override
	public void removeEntities() {}

	@Override
	public void e() {}

	@Override
	public void a(Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, Predicate<? super Entity> predicate) {}

	@Override
	public <T extends Entity> void a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, Predicate<? super T> predicate) {}

	@Override
	public boolean a(boolean flag) {
		return false;
	}

	@Override
	public Random a(long i) {
		return new Random(this.getWorld().getSeed() + this.locX * this.locX * 4987142 + this.locX * 5947611 + this.locZ * this.locZ * 4392871L + this.locZ * 389711 ^ i);
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean c(int i, int j) {
		return true;
	}
}
