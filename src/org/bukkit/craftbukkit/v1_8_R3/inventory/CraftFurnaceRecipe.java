package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.RecipesFurnace;

public class CraftFurnaceRecipe extends FurnaceRecipe implements CraftRecipe {
    public CraftFurnaceRecipe(ItemStack result, ItemStack source) {
        super(result, source.getType(), source.getDurability());
    }

    public static CraftFurnaceRecipe fromBukkitRecipe(FurnaceRecipe recipe) {
        if (recipe instanceof CraftFurnaceRecipe) {
            return (CraftFurnaceRecipe) recipe;
        }
        return new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
    }

    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();
        ItemStack input = this.getInput();
        RecipesFurnace.getInstance().registerRecipe(CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result));
    }
}
