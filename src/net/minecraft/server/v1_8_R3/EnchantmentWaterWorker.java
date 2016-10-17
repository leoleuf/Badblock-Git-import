package net.minecraft.server.v1_8_R3;

public class EnchantmentWaterWorker extends Enchantment {

    public EnchantmentWaterWorker(int i, MinecraftKey minecraftkey, int j) {
        super(i, minecraftkey, j, EnchantmentSlotType.ARMOR_HEAD);
        this.c("waterWorker");
    }

    @Override
	public int a(int i) {
        return 1;
    }

    @Override
	public int b(int i) {
        return this.a(i) + 40;
    }

    @Override
	public int getMaxLevel() {
        return 1;
    }
}
