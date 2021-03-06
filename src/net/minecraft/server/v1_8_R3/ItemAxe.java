package net.minecraft.server.v1_8_R3;

import java.util.Set;

import com.google.common.collect.Sets;

public class ItemAxe extends ItemTool {

    private static final Set<Block> c = Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER});

    protected ItemAxe(Item.EnumToolMaterial item_enumtoolmaterial) {
        super(3.0F, item_enumtoolmaterial, ItemAxe.c);
    }

    @Override
	public float getDestroySpeed(ItemStack itemstack, Block block) {
        return block.getMaterial() != Material.WOOD && block.getMaterial() != Material.PLANT && block.getMaterial() != Material.REPLACEABLE_PLANT ? super.getDestroySpeed(itemstack, block) : this.a;
    }
}
