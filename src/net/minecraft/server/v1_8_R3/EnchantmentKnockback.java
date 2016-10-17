package net.minecraft.server.v1_8_R3;

public class EnchantmentKnockback extends Enchantment {

    protected EnchantmentKnockback(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.WEAPON);
        this.c("knockback");
    }

    @Override
	public int a(int i) {
        return 5 + 20 * (i - 1);
    }

    @Override
	public int b(int i) {
        return super.a(i) + 50;
    }

    @Override
	public int getMaxLevel() {
        return 2;
    }
}
