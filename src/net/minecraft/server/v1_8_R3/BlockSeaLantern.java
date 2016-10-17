package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockSeaLantern extends Block {

    public BlockSeaLantern(Material material) {
        super(material);
        this.a(CreativeModeTab.b);
    }

    @Override
	public int a(Random random) {
        return 2 + random.nextInt(2);
    }

    @Override
	public int getDropCount(int i, Random random) {
        return MathHelper.clamp(this.a(random) + random.nextInt(i + 1), 1, 5);
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.PRISMARINE_CRYSTALS;
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return MaterialMapColor.p;
    }

    @Override
	protected boolean I() {
        return true;
    }
}
