package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

import net.minecraft.server.v1_8_R3.EntityPig;

public class CraftPig extends CraftAnimals implements Pig {
    public CraftPig(CraftServer server, EntityPig entity) {
        super(server, entity);
    }

    @Override
	public boolean hasSaddle() {
        return getHandle().hasSaddle();
    }

    @Override
	public void setSaddle(boolean saddled) {
        getHandle().setSaddle(saddled);
    }

    @Override
	public EntityPig getHandle() {
        return (EntityPig) entity;
    }

    @Override
    public String toString() {
        return "CraftPig";
    }

    @Override
	public EntityType getType() {
        return EntityType.PIG;
    }
}
