package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public class BlockLeaves1 extends BlockLeaves {

    public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.a("variant", BlockWood.EnumLogVariant.class, new Predicate() {
        public boolean a(BlockWood.EnumLogVariant blockwood_enumlogvariant) {
            return blockwood_enumlogvariant.a() < 4;
        }

        @Override
		public boolean apply(Object object) {
            return this.a((BlockWood.EnumLogVariant) object);
        }
    });

    public BlockLeaves1() {
        this.j(this.blockStateList.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)).set(BlockLeaves.DECAYABLE, Boolean.valueOf(true)));
    }

    @Override
	protected void a(World world, BlockPosition blockposition, IBlockData iblockdata, int i) {
        if (iblockdata.get(BlockLeaves1.VARIANT) == BlockWood.EnumLogVariant.OAK && world.random.nextInt(i) == 0) {
            a(world, blockposition, new ItemStack(Items.APPLE, 1, 0));
        }

    }

    @Override
	protected int d(IBlockData iblockdata) {
        return iblockdata.get(BlockLeaves1.VARIANT) == BlockWood.EnumLogVariant.JUNGLE ? 40 : super.d(iblockdata);
    }

    @Override
	protected ItemStack i(IBlockData iblockdata) {
        return new ItemStack(Item.getItemOf(this), 1, iblockdata.get(BlockLeaves1.VARIANT).a());
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockLeaves1.VARIANT, this.b(i)).set(BlockLeaves.DECAYABLE, Boolean.valueOf((i & 4) == 0)).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf((i & 8) > 0));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockLeaves1.VARIANT).a();

        if (!iblockdata.get(BlockLeaves.DECAYABLE).booleanValue()) {
            i |= 4;
        }

        if (iblockdata.get(BlockLeaves.CHECK_DECAY).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    @Override
	public BlockWood.EnumLogVariant b(int i) {
        return BlockWood.EnumLogVariant.a((i & 3) % 4);
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockLeaves1.VARIANT, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE});
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockLeaves1.VARIANT).a();
    }

    @Override
	public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity) {
        if (!world.isClientSide && entityhuman.bZ() != null && entityhuman.bZ().getItem() == Items.SHEARS) {
            entityhuman.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
            a(world, blockposition, new ItemStack(Item.getItemOf(this), 1, iblockdata.get(BlockLeaves1.VARIANT).a()));
        } else {
            super.a(world, entityhuman, blockposition, iblockdata, tileentity);
        }
    }
}
