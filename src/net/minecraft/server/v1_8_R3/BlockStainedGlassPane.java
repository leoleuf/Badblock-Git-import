package net.minecraft.server.v1_8_R3;

public class BlockStainedGlassPane extends BlockThin {

    public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);

    public BlockStainedGlassPane() {
        super(Material.SHATTERABLE, false);
        this.j(this.blockStateList.getBlockData().set(BlockThin.NORTH, Boolean.valueOf(false)).set(BlockThin.EAST, Boolean.valueOf(false)).set(BlockThin.SOUTH, Boolean.valueOf(false)).set(BlockThin.WEST, Boolean.valueOf(false)).set(BlockStainedGlassPane.COLOR, EnumColor.WHITE));
        this.a(CreativeModeTab.c);
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockStainedGlassPane.COLOR).getColorIndex();
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockStainedGlassPane.COLOR).e();
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockStainedGlassPane.COLOR, EnumColor.fromColorIndex(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockStainedGlassPane.COLOR).getColorIndex();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockThin.NORTH, BlockThin.EAST, BlockThin.WEST, BlockThin.SOUTH, BlockStainedGlassPane.COLOR});
    }

    @Override
	public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!world.isClientSide) {
            BlockBeacon.f(world, blockposition);
        }

    }

    @Override
	public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!world.isClientSide) {
            BlockBeacon.f(world, blockposition);
        }

    }
}
