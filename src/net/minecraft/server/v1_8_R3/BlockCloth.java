package net.minecraft.server.v1_8_R3;

public class BlockCloth extends Block {

    public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);

    public BlockCloth(Material material) {
        super(material);
        this.j(this.blockStateList.getBlockData().set(BlockCloth.COLOR, EnumColor.WHITE));
        this.a(CreativeModeTab.b);
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockCloth.COLOR).getColorIndex();
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockCloth.COLOR).e();
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockCloth.COLOR, EnumColor.fromColorIndex(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockCloth.COLOR).getColorIndex();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockCloth.COLOR});
    }
}
