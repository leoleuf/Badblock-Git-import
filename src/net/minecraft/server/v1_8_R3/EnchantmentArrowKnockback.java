package net.minecraft.server.v1_8_R3;

public class EnchantmentArrowKnockback extends Enchantment {

    public EnchantmentArrowKnockback(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.BOW);
        this.c("arrowKnockback");
    }

    @Override
	public int a(int i) {
        return 12 + (i - 1) * 20;
    }

    @Override
	public int b(int i) {
        return this.a(i) + 25;
    }

    @Override
	public int getMaxLevel() {
        return 2;
    }
}
