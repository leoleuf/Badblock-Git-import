package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockBookshelf extends Block {

    public BlockBookshelf() {
        super(Material.WOOD);
        this.a(CreativeModeTab.b);
    }

    @Override
	public int a(Random random) {
        return 3;
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.BOOK;
    }
}
