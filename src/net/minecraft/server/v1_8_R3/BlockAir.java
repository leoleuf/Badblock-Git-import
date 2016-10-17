package net.minecraft.server.v1_8_R3;

public class BlockAir extends Block {

    protected BlockAir() {
        super(Material.AIR);
    }

    @Override
	public int b() {
        return -1;
    }

    @Override
	public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return null;
    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public boolean a(IBlockData iblockdata, boolean flag) {
        return false;
    }

    @Override
	public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {}

    @Override
	public boolean a(World world, BlockPosition blockposition) {
        return true;
    }
}
