package net.minecraft.server.v1_8_R3;

public class ItemShears extends Item {

    public ItemShears() {
        this.c(1);
        this.setMaxDurability(238);
        this.a(CreativeModeTab.i);
    }

    @Override
	public boolean a(ItemStack itemstack, World world, Block block, BlockPosition blockposition, EntityLiving entityliving) {
        if (block.getMaterial() != Material.LEAVES && block != Blocks.WEB && block != Blocks.TALLGRASS && block != Blocks.VINE && block != Blocks.TRIPWIRE && block != Blocks.WOOL) {
            return super.a(itemstack, world, block, blockposition, entityliving);
        } else {
            itemstack.damage(1, entityliving);
            return true;
        }
    }

    @Override
	public boolean canDestroySpecialBlock(Block block) {
        return block == Blocks.WEB || block == Blocks.REDSTONE_WIRE || block == Blocks.TRIPWIRE;
    }

    @Override
	public float getDestroySpeed(ItemStack itemstack, Block block) {
        return block != Blocks.WEB && block.getMaterial() != Material.LEAVES ? (block == Blocks.WOOL ? 5.0F : super.getDestroySpeed(itemstack, block)) : 15.0F;
    }
}
