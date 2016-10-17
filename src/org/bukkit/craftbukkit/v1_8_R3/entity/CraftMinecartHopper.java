package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

import net.minecraft.server.v1_8_R3.EntityMinecartHopper;

final class CraftMinecartHopper extends CraftMinecart implements HopperMinecart {
    private final CraftInventory inventory;

    CraftMinecartHopper(CraftServer server, EntityMinecartHopper entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartHopper{" + "inventory=" + inventory + '}';
    }

    @Override
	public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }

    @Override
	public Inventory getInventory() {
        return inventory;
    }
}
