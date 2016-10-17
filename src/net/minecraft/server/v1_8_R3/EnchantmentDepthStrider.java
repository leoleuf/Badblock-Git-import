package net.minecraft.server.v1_8_R3;

public class EnchantmentDepthStrider extends Enchantment {

    public EnchantmentDepthStrider(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.ARMOR_FEET);
        this.c("waterWalker");
    }

    @Override
	public int a(int i) {
        return i * 10;
    }

    @Override
	public int b(int i) {
        return this.a(i) + 15;
    }

    @Override
	public int getMaxLevel() {
        return 3;
    }
}
