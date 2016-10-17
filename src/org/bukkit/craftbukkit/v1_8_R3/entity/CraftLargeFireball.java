package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;

import net.minecraft.server.v1_8_R3.EntityLargeFireball;

public class CraftLargeFireball extends CraftFireball implements LargeFireball {
    public CraftLargeFireball(CraftServer server, EntityLargeFireball entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        getHandle().yield = (int) yield;
    }

    @Override
    public EntityLargeFireball getHandle() {
        return (EntityLargeFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftLargeFireball";
    }

    @Override
	public EntityType getType() {
        return EntityType.FIREBALL;
    }
}
