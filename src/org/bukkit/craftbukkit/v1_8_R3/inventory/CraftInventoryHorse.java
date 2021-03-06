package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.IInventory;

public class CraftInventoryHorse extends CraftInventory implements HorseInventory {

    public CraftInventoryHorse(IInventory inventory) {
        super(inventory);
    }

    @Override
	public ItemStack getSaddle() {
        return getItem(0);
    }

    @Override
	public ItemStack getArmor() {
       return getItem(1);
    }

    @Override
	public void setSaddle(ItemStack stack) {
        setItem(0, stack);
    }

    @Override
	public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
