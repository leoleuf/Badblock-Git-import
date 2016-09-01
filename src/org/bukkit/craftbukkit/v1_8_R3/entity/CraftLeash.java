package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

import net.minecraft.server.v1_8_R3.EntityLeash;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, EntityLeash entity) {
        super(server, entity);
    }

    @Override
    public EntityLeash getHandle() {
        return (EntityLeash) entity;
    }

    @Override
    public String toString() {
        return "CraftLeash";
    }

    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
