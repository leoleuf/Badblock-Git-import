package net.minecraft.server.v1_8_R3;

import java.util.Random;

import fr.badblock.minecraftserver.BadblockConfig;

public class BlockRepeater extends BlockDiodeAbstract {

    public static final BlockStateBoolean LOCKED = BlockStateBoolean.of("locked");
    public static final BlockStateInteger DELAY = BlockStateInteger.of("delay", 1, 4);

    protected BlockRepeater(boolean flag) {
        super(flag);
        this.j(this.blockStateList.getBlockData().set(BlockDirectional.FACING, EnumDirection.NORTH).set(BlockRepeater.DELAY, Integer.valueOf(1)).set(BlockRepeater.LOCKED, Boolean.valueOf(false)));
    }

    @Override
	public String getName() {
        return LocaleI18n.get("item.diode.name");
    }

    @Override
	public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockdata.set(BlockRepeater.LOCKED, Boolean.valueOf(this.b(iblockaccess, blockposition, iblockdata)));
    }

    @Override
	public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (!entityhuman.abilities.mayBuild) {
            return false;
        } else {
            world.setTypeAndData(blockposition, iblockdata.a(BlockRepeater.DELAY), 3);
            return true;
        }
    }

    @Override
	protected int d(IBlockData iblockdata) {
        return iblockdata.get(BlockRepeater.DELAY).intValue() * 2;
    }

    @Override
	protected IBlockData e(IBlockData iblockdata) {
        Integer integer = iblockdata.get(BlockRepeater.DELAY);
        Boolean obool = iblockdata.get(BlockRepeater.LOCKED);
        EnumDirection enumdirection = iblockdata.get(BlockDirectional.FACING);

        return Blocks.POWERED_REPEATER.getBlockData().set(BlockDirectional.FACING, enumdirection).set(BlockRepeater.DELAY, integer).set(BlockRepeater.LOCKED, obool);
    }

    @Override
	protected IBlockData k(IBlockData iblockdata) {
        Integer integer = iblockdata.get(BlockRepeater.DELAY);
        Boolean obool = iblockdata.get(BlockRepeater.LOCKED);
        EnumDirection enumdirection = iblockdata.get(BlockDirectional.FACING);

        return Blocks.UNPOWERED_REPEATER.getBlockData().set(BlockDirectional.FACING, enumdirection).set(BlockRepeater.DELAY, integer).set(BlockRepeater.LOCKED, obool);
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.REPEATER;
    }

    @Override
	public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        return this.c(iblockaccess, blockposition, iblockdata) > 0;
    }

    @Override
	protected boolean c(Block block) {
        return d(block);
    }

    @Override
	public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        super.remove(world, blockposition, iblockdata);

        if(!BadblockConfig.config.redstone.useDiodes)
     		return;

        
        this.h(world, blockposition, iblockdata);
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockDirectional.FACING, EnumDirection.fromType2(i)).set(BlockRepeater.LOCKED, Boolean.valueOf(false)).set(BlockRepeater.DELAY, Integer.valueOf(1 + (i >> 2)));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockDirectional.FACING).b();

        i |= iblockdata.get(BlockRepeater.DELAY).intValue() - 1 << 2;
        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockDirectional.FACING, BlockRepeater.DELAY, BlockRepeater.LOCKED});
    }
}
