package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.EntityEnderSignal;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, EntityEnderSignal entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderSignal getHandle() {
        return (EntityEnderSignal) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }
}