package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EnumColor;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

    @Override
	public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().getColor().getColorIndex());
    }

    @Override
	public void setColor(DyeColor color) {
        getHandle().setColor(EnumColor.fromColorIndex(color.getWoolData()));
    }

    @Override
	public boolean isSheared() {
        return getHandle().isSheared();
    }

    @Override
	public void setSheared(boolean flag) {
        getHandle().setSheared(flag);
    }

    @Override
    public EntitySheep getHandle() {
        return (EntitySheep) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    @Override
	public EntityType getType() {
        return EntityType.SHEEP;
    }
}
