package net.minecraft.server.v1_8_R3;

public class EnchantmentArrowDamage extends Enchantment {

    public EnchantmentArrowDamage(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.BOW);
        this.c("arrowDamage");
    }

    @Override
	public int a(int i) {
        return 1 + (i - 1) * 10;
    }

    @Override
	public int b(int i) {
        return this.a(i) + 15;
    }

    @Override
	public int getMaxLevel() {
        return 5;
    }
}
