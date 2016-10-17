package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockWoodenStep extends BlockStepAbstract {

    public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.of("variant", BlockWood.EnumLogVariant.class);

    public BlockWoodenStep() {
        super(Material.WOOD);
        IBlockData iblockdata = this.blockStateList.getBlockData();

        if (!this.l()) {
            iblockdata = iblockdata.set(BlockStepAbstract.HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
        }

        this.j(iblockdata.set(BlockWoodenStep.VARIANT, BlockWood.EnumLogVariant.OAK));
        this.a(CreativeModeTab.b);
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockWoodenStep.VARIANT).c();
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Item.getItemOf(Blocks.WOODEN_SLAB);
    }

    @Override
	public String b(int i) {
        return super.a() + "." + BlockWood.EnumLogVariant.a(i).d();
    }

    @Override
	public IBlockState<?> n() {
        return BlockWoodenStep.VARIANT;
    }

    @Override
	public Object a(ItemStack itemstack) {
        return BlockWood.EnumLogVariant.a(itemstack.getData() & 7);
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        IBlockData iblockdata = this.getBlockData().set(BlockWoodenStep.VARIANT, BlockWood.EnumLogVariant.a(i & 7));

        if (!this.l()) {
            iblockdata = iblockdata.set(BlockStepAbstract.HALF, (i & 8) == 0 ? BlockStepAbstract.EnumSlabHalf.BOTTOM : BlockStepAbstract.EnumSlabHalf.TOP);
        }

        return iblockdata;
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockWoodenStep.VARIANT).a();

        if (!this.l() && iblockdata.get(BlockStepAbstract.HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return this.l() ? new BlockStateList(this, new IBlockState[] { BlockWoodenStep.VARIANT}) : new BlockStateList(this, new IBlockState[] { BlockStepAbstract.HALF, BlockWoodenStep.VARIANT});
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockWoodenStep.VARIANT).a();
    }
}
