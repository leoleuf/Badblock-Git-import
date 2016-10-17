package net.minecraft.server.v1_8_R3;

public class BlockPotatoes extends BlockCrops {

    public BlockPotatoes() {}

    @Override
	protected Item l() {
        return Items.POTATO;
    }

    @Override
	protected Item n() {
        return Items.POTATO;
    }

    @Override
	public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        super.dropNaturally(world, blockposition, iblockdata, f, i);
        if (!world.isClientSide) {
            if (iblockdata.get(BlockCrops.AGE).intValue() >= 7 && world.random.nextInt(50) == 0) {
                a(world, blockposition, new ItemStack(Items.POISONOUS_POTATO));
            }

        }
    }
}
