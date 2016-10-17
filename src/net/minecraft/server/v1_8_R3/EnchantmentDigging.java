package net.minecraft.server.v1_8_R3;

public class EnchantmentDigging extends Enchantment {

    protected EnchantmentDigging(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.DIGGER);
        this.c("digging");
    }

    @Override
	public int a(int i) {
        return 1 + 10 * (i - 1);
    }

    @Override
	public int b(int i) {
        return super.a(i) + 50;
    }

    @Override
	public int getMaxLevel() {
        return 5;
    }

    @Override
	public boolean canEnchant(ItemStack itemstack) {
        return itemstack.getItem() == Items.SHEARS ? true : super.canEnchant(itemstack);
    }
}
