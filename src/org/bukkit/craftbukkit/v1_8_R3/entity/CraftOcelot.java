package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

import net.minecraft.server.v1_8_R3.EntityOcelot;

public class CraftOcelot extends CraftTameableAnimal implements Ocelot {
    public CraftOcelot(CraftServer server, EntityOcelot wolf) {
        super(server, wolf);
    }

    @Override
    public EntityOcelot getHandle() {
        return (EntityOcelot) entity;
    }

    public Type getCatType() {
        return Type.getType(getHandle().getCatType());
    }

    public void setCatType(Type type) {
        Validate.notNull(type, "Cat type cannot be null");
        getHandle().setCatType(type.getId());
    }

    @Override
    public EntityType getType() {
        return EntityType.OCELOT;
    }
}
