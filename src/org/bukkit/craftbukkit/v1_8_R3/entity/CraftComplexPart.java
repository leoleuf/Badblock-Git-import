package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;

public class CraftComplexPart extends CraftEntity implements ComplexEntityPart {
    public CraftComplexPart(CraftServer server, EntityComplexPart entity) {
        super(server, entity);
    }

    @Override
	public ComplexLivingEntity getParent() {
        return (ComplexLivingEntity) ((EntityEnderDragon) getHandle().owner).getBukkitEntity();
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent cause) {
        getParent().setLastDamageCause(cause);
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return getParent().getLastDamageCause();
    }

    @Override
    public EntityComplexPart getHandle() {
        return (EntityComplexPart) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexPart";
    }

    @Override
	public EntityType getType() {
        return EntityType.COMPLEX_PART;
    }
}
