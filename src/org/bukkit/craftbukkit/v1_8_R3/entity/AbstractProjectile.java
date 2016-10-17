package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Projectile;

public abstract class AbstractProjectile extends CraftEntity implements Projectile {

    private boolean doesBounce;

    public AbstractProjectile(CraftServer server, net.minecraft.server.v1_8_R3.Entity entity) {
        super(server, entity);
        doesBounce = false;
    }

    @Override
	public boolean doesBounce() {
        return doesBounce;
    }

    @Override
	public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

}
