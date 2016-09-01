package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

import net.minecraft.server.v1_8_R3.EntitySquid;

public class CraftSquid extends CraftWaterMob implements Squid {

    public CraftSquid(CraftServer server, EntitySquid entity) {
        super(server, entity);
    }

    @Override
    public EntitySquid getHandle() {
        return (EntitySquid) entity;
    }

    @Override
    public String toString() {
        return "CraftSquid";
    }

    public EntityType getType() {
        return EntityType.SQUID;
    }
}
