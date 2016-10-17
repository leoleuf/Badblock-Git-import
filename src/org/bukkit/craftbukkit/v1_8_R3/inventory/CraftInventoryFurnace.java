package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.TileEntityFurnace;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory {
    public CraftInventoryFurnace(TileEntityFurnace inventory) {
        super(inventory);
    }

    @Override
	public ItemStack getResult() {
        return getItem(2);
    }

    @Override
	public ItemStack getFuel() {
        return getItem(1);
    }

    @Override
	public ItemStack getSmelting() {
        return getItem(0);
    }

    @Override
	public void setFuel(ItemStack stack) {
        setItem(1,stack);
    }

    @Override
	public void setResult(ItemStack stack) {
        setItem(2,stack);
    }

    @Override
	public void setSmelting(ItemStack stack) {
        setItem(0,stack);
    }

    @Override
    public Furnace getHolder() {
        return (Furnace) inventory.getOwner();
    }
}
