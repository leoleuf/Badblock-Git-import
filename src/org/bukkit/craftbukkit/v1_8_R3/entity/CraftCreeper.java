package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

import net.minecraft.server.v1_8_R3.EntityCreeper;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, EntityCreeper entity) {
        super(server, entity);
    }

    @Override
	public boolean isPowered() {
        return getHandle().isPowered();
    }

    @Override
	public void setPowered(boolean powered) {
        CraftServer server = this.server;
        Creeper entity = (Creeper) this.getHandle().getBukkitEntity();

        if (powered) {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().setPowered(true);
            }
        } else {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                getHandle().setPowered(false);
            }
        }
    }

    @Override
    public EntityCreeper getHandle() {
        return (EntityCreeper) entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    @Override
	public EntityType getType() {
        return EntityType.CREEPER;
    }
}
