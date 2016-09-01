package org.bukkit.craftbukkit.v1_8_R3.inventory;

import org.bukkit.inventory.MerchantInventory;

import net.minecraft.server.v1_8_R3.InventoryMerchant;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {
    public CraftInventoryMerchant(InventoryMerchant merchant) {
        super(merchant);
    }
}
