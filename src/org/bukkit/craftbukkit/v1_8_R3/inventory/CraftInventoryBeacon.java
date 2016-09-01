package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.TileEntityBeacon;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory {
    public CraftInventoryBeacon(TileEntityBeacon beacon) {
        super(beacon);
    }

    public void setItem(ItemStack item) {
        setItem(0, item);
    }

    public ItemStack getItem() {
        return getItem(0);
    }
}
