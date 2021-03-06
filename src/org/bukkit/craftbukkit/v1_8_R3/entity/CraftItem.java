package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityItem;

public class CraftItem extends CraftEntity implements Item {
    private final EntityItem item;

    public CraftItem(CraftServer server, Entity entity, EntityItem item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, EntityItem entity) {
        this(server, entity, entity);
    }

    @Override
	public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.getItemStack());
    }

    @Override
	public void setItemStack(ItemStack stack) {
        item.setItemStack(CraftItemStack.asNMSCopy(stack));
    }

    @Override
	public int getPickupDelay() {
        return item.pickupDelay;
    }

    @Override
	public void setPickupDelay(int delay) {
        item.pickupDelay = Math.min(delay, Short.MAX_VALUE);
    }

    @Override
    public String toString() {
        return "CraftItem";
    }

    @Override
	public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
