package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockLightStone extends Block {

    public BlockLightStone(Material material) {
        super(material);
        this.a(CreativeModeTab.b);
    }

    @Override
	public int getDropCount(int i, Random random) {
        return MathHelper.clamp(this.a(random) + random.nextInt(i + 1), 1, 4);
    }

    @Override
	public int a(Random random) {
        return 2 + random.nextInt(3);
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.GLOWSTONE_DUST;
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return MaterialMapColor.d;
    }
}
