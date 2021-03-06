package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

import net.minecraft.server.v1_8_R3.EntitySkeleton;

public class CraftSkeleton extends CraftMonster implements Skeleton {

    public CraftSkeleton(CraftServer server, EntitySkeleton entity) {
        super(server, entity);
    }

    @Override
    public EntitySkeleton getHandle() {
        return (EntitySkeleton) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    @Override
	public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
	public SkeletonType getSkeletonType() {
        return SkeletonType.getType(getHandle().getSkeletonType());
    }

    @Override
	public void setSkeletonType(SkeletonType type) {
        Validate.notNull(type);
        getHandle().setSkeletonType(type.getId());
    }
}
