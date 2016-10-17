package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockWeb extends Block {

    public BlockWeb() {
        super(Material.WEB);
        this.a(CreativeModeTab.c);
    }

    @Override
	public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity) {
        entity.aA();
    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return null;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.STRING;
    }

    @Override
	protected boolean I() {
        return true;
    }
}
