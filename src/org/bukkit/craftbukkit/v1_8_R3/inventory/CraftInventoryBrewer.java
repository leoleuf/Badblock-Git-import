package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.IInventory;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {
    public CraftInventoryBrewer(IInventory inventory) {
        super(inventory);
    }

    @Override
	public ItemStack getIngredient() {
        return getItem(3);
    }

    @Override
	public void setIngredient(ItemStack ingredient) {
        setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        return (BrewingStand) inventory.getOwner();
    }
}
