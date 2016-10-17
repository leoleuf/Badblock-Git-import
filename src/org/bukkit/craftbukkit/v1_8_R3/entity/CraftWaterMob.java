package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.WaterMob;

import net.minecraft.server.v1_8_R3.EntityWaterAnimal;

public class CraftWaterMob extends CraftLivingEntity implements WaterMob {

    public CraftWaterMob(CraftServer server, EntityWaterAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityWaterAnimal getHandle() {
        return (EntityWaterAnimal) entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
