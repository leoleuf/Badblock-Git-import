package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Ageable;

import net.minecraft.server.v1_8_R3.EntityAgeable;

public class CraftAgeable extends CraftCreature implements Ageable {
    public CraftAgeable(CraftServer server, EntityAgeable entity) {
        super(server, entity);
    }

    @Override
	public int getAge() {
        return getHandle().getAge();
    }

    @Override
	public void setAge(int age) {
        getHandle().setAgeRaw(age);
    }

    @Override
	public void setAgeLock(boolean lock) {
        getHandle().ageLocked = lock;
    }

    @Override
	public boolean getAgeLock() {
        return getHandle().ageLocked;
    }

    @Override
	public void setBaby() {
        if (isAdult()) {
            setAge(-24000);
        }
    }

    @Override
	public void setAdult() {
        if (!isAdult()) {
            setAge(0);
        }
    }

    @Override
	public boolean isAdult() {
        return getAge() >= 0;
    }


    @Override
	public boolean canBreed() {
        return getAge() == 0;
    }

    @Override
	public void setBreed(boolean breed) {
        if (breed) {
            setAge(0);
        } else if (isAdult()) {
            setAge(6000);
        }
    }

    @Override
    public EntityAgeable getHandle() {
        return (EntityAgeable) entity;
    }

    @Override
    public String toString() {
        return "CraftAgeable";
    }
}
