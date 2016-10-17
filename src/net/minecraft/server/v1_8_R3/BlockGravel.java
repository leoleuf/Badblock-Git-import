package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockGravel extends BlockFalling {

    public BlockGravel() {}

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        if (i > 3) {
            i = 3;
        }

        return random.nextInt(10 - i * 3) == 0 ? Items.FLINT : Item.getItemOf(this);
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return MaterialMapColor.m;
    }
}
