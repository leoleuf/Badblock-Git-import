package net.minecraft.server.v1_8_R3;

public class EnchantmentOxygen extends Enchantment {

    public EnchantmentOxygen(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.ARMOR_HEAD);
        this.c("oxygen");
    }

    @Override
	public int a(int i) {
        return 10 * i;
    }

    @Override
	public int b(int i) {
        return this.a(i) + 30;
    }

    @Override
	public int getMaxLevel() {
        return 3;
    }
}
