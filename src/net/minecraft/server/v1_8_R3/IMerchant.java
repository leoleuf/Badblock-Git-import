package net.minecraft.server.v1_8_R3;

public interface IMerchant {

    void a_(EntityHuman entityhuman);

    EntityHuman v_();

    MerchantRecipeList getOffers(EntityHuman entityhuman);

    void a(MerchantRecipe merchantrecipe);

    void a_(ItemStack itemstack);

    IChatBaseComponent getScoreboardDisplayName();
}
