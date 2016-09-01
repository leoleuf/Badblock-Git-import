package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

import net.minecraft.server.v1_8_R3.EntityLiving;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, EntityLiving entity) {
        super(server, entity);
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
