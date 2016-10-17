package net.minecraft.server.v1_8_R3;

public class SourceBlock implements ISourceBlock {

    private final World a;
    private final BlockPosition b;

    public SourceBlock(World world, BlockPosition blockposition) {
        this.a = world;
        this.b = blockposition;
    }

    @Override
	public World getWorld() {
        return this.a;
    }

    @Override
	public double getX() {
        return this.b.getX() + 0.5D;
    }

    @Override
	public double getY() {
        return this.b.getY() + 0.5D;
    }

    @Override
	public double getZ() {
        return this.b.getZ() + 0.5D;
    }

    @Override
	public BlockPosition getBlockPosition() {
        return this.b;
    }

    @Override
	public int f() {
        IBlockData iblockdata = this.a.getType(this.b);

        return iblockdata.getBlock().toLegacyData(iblockdata);
    }

    @Override
	public <T extends TileEntity> T getTileEntity() {
        return (T) this.a.getTileEntity(this.b);
    }
}
