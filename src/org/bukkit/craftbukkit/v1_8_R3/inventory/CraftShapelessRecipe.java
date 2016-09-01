package org.bukkit.craftbukkit.v1_8_R3.inventory;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.ShapelessRecipes;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private ShapelessRecipes recipe;

    public CraftShapelessRecipe(ItemStack result) {
        super(result);
    }

    public CraftShapelessRecipe(ItemStack result, ShapelessRecipes recipe) {
        this(result);
        this.recipe = recipe;
    }

    public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe) recipe;
        }
        CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getResult());
        for (ItemStack ingred : recipe.getIngredientList()) {
            ret.addIngredient(ingred.getType(), ingred.getDurability());
        }
        return ret;
    }

    public void addToCraftingManager() {
        List<ItemStack> ingred = this.getIngredientList();
        Object[] data = new Object[ingred.size()];
        int i = 0;
        for (ItemStack mdata : ingred) {
            int id = mdata.getTypeId();
            short dmg = mdata.getDurability();
            data[i] = new net.minecraft.server.v1_8_R3.ItemStack(CraftMagicNumbers.getItem(id), 1, dmg);
            i++;
        }
        CraftingManager.getInstance().registerShapelessRecipe(CraftItemStack.asNMSCopy(this.getResult()), data);
    }
}
