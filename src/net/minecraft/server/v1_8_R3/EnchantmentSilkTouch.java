package net.minecraft.server.v1_8_R3;

public class EnchantmentSilkTouch extends Enchantment {

    protected EnchantmentSilkTouch(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.DIGGER);
        this.c("untouching");
    }

    @Override
	public int a(int i) {
        return 15;
    }

    @Override
	public int b(int i) {
        return super.a(i) + 50;
    }

    @Override
	public int getMaxLevel() {
        return 1;
    }

    @Override
	public boolean a(Enchantment enchantment) {
        return super.a(enchantment) && enchantment.id != Enchantment.LOOT_BONUS_BLOCKS.id;
    }

    @Override
	public boolean canEnchant(ItemStack itemstack) {
        return itemstack.getItem() == Items.SHEARS ? true : super.canEnchant(itemstack);
    }
}
