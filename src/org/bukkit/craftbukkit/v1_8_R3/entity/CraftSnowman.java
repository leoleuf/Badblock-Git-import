package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

import net.minecraft.server.v1_8_R3.EntitySnowman;

public class CraftSnowman extends CraftGolem implements Snowman {
    public CraftSnowman(CraftServer server, EntitySnowman entity) {
        super(server, entity);
    }

    @Override
    public EntitySnowman getHandle() {
        return (EntitySnowman) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }

    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
