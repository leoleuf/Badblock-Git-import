package net.minecraft.server.v1_8_R3;

public class EnchantmentFlameArrows extends Enchantment {

    public EnchantmentFlameArrows(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.BOW);
        this.c("arrowFire");
    }

    @Override
	public int a(int i) {
        return 20;
    }

    @Override
	public int b(int i) {
        return 50;
    }

    @Override
	public int getMaxLevel() {
        return 1;
    }
}
