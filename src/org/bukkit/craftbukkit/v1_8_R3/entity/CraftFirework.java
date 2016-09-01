package org.bukkit.craftbukkit.v1_8_R3.entity;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;

import java.util.Random;

public class CraftFirework extends CraftEntity implements Firework {
    private static final int FIREWORK_ITEM_INDEX = 8;

    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, EntityFireworks entity) {
        super(server, entity);

        ItemStack item = getHandle().getDataWatcher().getItemStack(FIREWORK_ITEM_INDEX);

        if (item == null) {
            item = new ItemStack(Items.FIREWORKS);
            getHandle().getDataWatcher().watch(FIREWORK_ITEM_INDEX, item);
        }

        this.item = CraftItemStack.asCraftMirror(item);

        // Ensure the item is a firework...
        if (this.item.getType() != Material.FIREWORK) {
            this.item.setType(Material.FIREWORK);
        }
    }

    @Override
    public EntityFireworks getHandle() {
        return (EntityFireworks) entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        item.setItemMeta(meta);

        // Copied from EntityFireworks constructor, update firework lifetime/power
        getHandle().expectedLifespan = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        getHandle().getDataWatcher().update(FIREWORK_ITEM_INDEX);
    }

    @Override
    public void detonate() {
        getHandle().expectedLifespan = 0;
    }
}
