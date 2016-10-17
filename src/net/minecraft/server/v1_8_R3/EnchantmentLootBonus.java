package net.minecraft.server.v1_8_R3;

public class EnchantmentLootBonus extends Enchantment {

    protected EnchantmentLootBonus(int i, MinecraftKey minecraftkey, int j, EnchantmentSlotType enchantmentslottype) {
        super(i, minecraftkey, j, enchantmentslottype);
        if (enchantmentslottype == EnchantmentSlotType.DIGGER) {
            this.c("lootBonusDigger");
        } else if (enchantmentslottype == EnchantmentSlotType.FISHING_ROD) {
            this.c("lootBonusFishing");
        } else {
            this.c("lootBonus");
        }

    }

    @Override
	public int a(int i) {
        return 15 + (i - 1) * 9;
    }

    @Override
	public int b(int i) {
        return super.a(i) + 50;
    }

    @Override
	public int getMaxLevel() {
        return 3;
    }

    @Override
	public boolean a(Enchantment enchantment) {
        return super.a(enchantment) && enchantment.id != Enchantment.SILK_TOUCH.id;
    }
}
