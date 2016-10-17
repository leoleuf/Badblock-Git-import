package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.EntityBat;

public class CraftBat extends CraftAmbient implements Bat {
    public CraftBat(CraftServer server, EntityBat entity) {
        super(server, entity);
    }

    @Override
    public EntityBat getHandle() {
        return (EntityBat) entity;
    }

    @Override
    public String toString() {
        return "CraftBat";
    }

    @Override
	public EntityType getType() {
        return EntityType.BAT;
    }

    @Override
    public boolean isAwake() {
        return !getHandle().isAsleep();
    }

    @Override
    public void setAwake(boolean state) {
        getHandle().setAsleep(!state);
    }
}
