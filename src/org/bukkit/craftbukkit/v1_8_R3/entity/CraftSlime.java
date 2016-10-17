package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

import net.minecraft.server.v1_8_R3.EntitySlime;

public class CraftSlime extends CraftLivingEntity implements Slime {

    public CraftSlime(CraftServer server, EntitySlime entity) {
        super(server, entity);
    }

    @Override
	public int getSize() {
        return getHandle().getSize();
    }

    @Override
	public void setSize(int size) {
        getHandle().setSize(size);
    }

    @Override
    public EntitySlime getHandle() {
        return (EntitySlime) entity;
    }

    @Override
    public String toString() {
        return "CraftSlime";
    }

    @Override
	public EntityType getType() {
        return EntityType.SLIME;
    }
}
