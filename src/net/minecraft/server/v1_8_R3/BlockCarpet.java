package net.minecraft.server.v1_8_R3;

public class BlockCarpet extends Block {

    public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);

    protected BlockCarpet() {
        super(Material.WOOL);
        this.j(this.blockStateList.getBlockData().set(BlockCarpet.COLOR, EnumColor.WHITE));
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.a(true);
        this.a(CreativeModeTab.c);
        this.b(0);
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockCarpet.COLOR).e();
    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public void j() {
        this.b(0);
    }

    @Override
	public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
        this.b(0);
    }

    protected void b(int i) {
        byte b0 = 0;
        float f = 1 * (1 + b0) / 16.0F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
	public boolean canPlace(World world, BlockPosition blockposition) {
        return super.canPlace(world, blockposition) && this.e(world, blockposition);
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        this.e(world, blockposition, iblockdata);
    }

    private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!this.e(world, blockposition)) {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
            return false;
        } else {
            return true;
        }
    }

    private boolean e(World world, BlockPosition blockposition) {
        return !world.isEmpty(blockposition.down());
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockCarpet.COLOR).getColorIndex();
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockCarpet.COLOR, EnumColor.fromColorIndex(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockCarpet.COLOR).getColorIndex();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockCarpet.COLOR});
    }
}
