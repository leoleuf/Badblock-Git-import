package net.minecraft.server.v1_8_R3;

import java.util.Set;

import com.google.common.collect.Sets;

public class ItemSpade extends ItemTool {

    private static final Set<Block> c = Sets.newHashSet(new Block[] { Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND});

    public ItemSpade(Item.EnumToolMaterial item_enumtoolmaterial) {
        super(1.0F, item_enumtoolmaterial, ItemSpade.c);
    }

    @Override
	public boolean canDestroySpecialBlock(Block block) {
        return block == Blocks.SNOW_LAYER ? true : block == Blocks.SNOW;
    }
}
