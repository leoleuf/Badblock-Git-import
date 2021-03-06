package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftContainer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_8_R3.BlockAnvil;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockWorkbench;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.ITileEntityContainer;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;
import net.minecraft.server.v1_8_R3.TileEntityBrewingStand;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.TileEntityDropper;
import net.minecraft.server.v1_8_R3.TileEntityEnchantTable;
import net.minecraft.server.v1_8_R3.TileEntityFurnace;
import net.minecraft.server.v1_8_R3.TileEntityHopper;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final EntityHuman entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
        enderChest = new CraftInventory(entity.getEnderChest());
    }

    @Override
	public String getName() {
        return getHandle().getName();
    }

    @Override
	public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
	public EntityEquipment getEquipment() {
        return inventory;
    }

    @Override
	public Inventory getEnderChest() {
        return enderChest;
    }

    @Override
	public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    @Override
	public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    @Override
	public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(getHandle().inventory.getCarried());
    }

    @Override
	public void setItemOnCursor(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);
        getHandle().inventory.setCarried(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayer) getHandle()).broadcastCarriedItem(); // Send set slot for cursor
        }
    }

    @Override
	public boolean isSleeping() {
        return getHandle().sleeping;
    }

    @Override
	public int getSleepTicks() {
        return getHandle().sleepTicks;
    }

    @Override
	public boolean isOp() {
        return op;
    }

    @Override
	public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    @Override
	public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
	public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    @Override
	public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
	public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
	public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
	public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
	public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    @Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
	public GameMode getGameMode() {
        return mode;
    }

    @Override
	public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public EntityHuman getHandle() {
        return (EntityHuman) entity;
    }

    public void setHandle(final EntityHuman entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }

    @Override
	public InventoryView getOpenInventory() {
        return getHandle().activeContainer.getBukkitView();
    }

    @Override
	public InventoryView openInventory(Inventory inventory) {
        if(!(getHandle() instanceof EntityPlayer)) return null;
        EntityPlayer player = (EntityPlayer) getHandle();
        InventoryType type = inventory.getType();
        Container formerContainer = getHandle().activeContainer;

        IInventory iinventory = (inventory instanceof CraftInventory) ? ((CraftInventory) inventory).getInventory() : new org.bukkit.craftbukkit.v1_8_R3.inventory.InventoryWrapper(inventory);

        switch (type) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
                getHandle().openContainer(iinventory);
                break;
            case DISPENSER:
                if (iinventory instanceof TileEntityDispenser) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dispenser");
                }
                break;
            case DROPPER:
                if (iinventory instanceof TileEntityDropper) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dropper");
                }
                break;
            case FURNACE:
                if (iinventory instanceof TileEntityFurnace) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:furnace");
                }
                break;
            case WORKBENCH:
                openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;
            case BREWING:
                if (iinventory instanceof TileEntityBrewingStand) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:brewing_stand");
                }
                break;
            case ENCHANTING:
                openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;
            case HOPPER:
                if (iinventory instanceof TileEntityHopper) {
                    getHandle().openContainer(iinventory);
                } else if (iinventory instanceof EntityMinecartHopper) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:hopper");
                }
                break;
            case BEACON:
                if (iinventory instanceof TileEntityBeacon) {
                    getHandle().openContainer(iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:beacon");
                }
                break;
            case ANVIL:
                if (iinventory instanceof BlockAnvil.TileEntityContainerAnvil) {
                    getHandle().openTileEntity((BlockAnvil.TileEntityContainerAnvil) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:anvil");
                }
                break;
            case CREATIVE:
            case CRAFTING:
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
        }
        if (getHandle().activeContainer == formerContainer) {
            return null;
        }
        getHandle().activeContainer.checkReachable = false;
        return getHandle().activeContainer.getBukkitView();
    }

    private void openCustomInventory(Inventory inventory, EntityPlayer player, String windowType) {
        if (player.playerConnection == null) return;
        Container container = new CraftContainer(inventory, this, player.nextContainerCounter());

        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if(container == null) return;

        String title = container.getBukkitView().getTitle();
        int size = container.getBukkitView().getTopInventory().getSize();

        // Special cases
        if (windowType.equals("minecraft:crafting_table") 
                || windowType.equals("minecraft:anvil")
                || windowType.equals("minecraft:enchanting_table")
                ) {
            size = 0;
        }

        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, windowType, new ChatComponentText(title), size));
        getHandle().activeContainer = container;
        getHandle().activeContainer.addSlotListener(player);
    }

    @Override
	public InventoryView openWorkbench(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }
        getHandle().openTileEntity(new BlockWorkbench.TileEntityContainerWorkbench(getHandle().world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            getHandle().activeContainer.checkReachable = false;
        }
        return getHandle().activeContainer.getBukkitView();
    }

    @Override
	public InventoryView openEnchanting(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }

        // If there isn't an enchant table we can force create one, won't be very useful though.
        TileEntity container = getHandle().world.getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        if (container == null && force) {
            container = new TileEntityEnchantTable();
        }
        getHandle().openTileEntity((ITileEntityContainer) container);

        if (force) {
            getHandle().activeContainer.checkReachable = false;
        }
        return getHandle().activeContainer.getBukkitView();
    }

    @Override
	public void openInventory(InventoryView inventory) {
        if (!(getHandle() instanceof EntityPlayer)) return; // TODO: NPC support?
        if (((EntityPlayer) getHandle()).playerConnection == null) return;
        if (getHandle().activeContainer != getHandle().defaultContainer) {
            // fire INVENTORY_CLOSE if one already open
            ((EntityPlayer)getHandle()).playerConnection.a(new PacketPlayInCloseWindow(getHandle().activeContainer.windowId));
        }
        EntityPlayer player = (EntityPlayer) getHandle();
        Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, player.nextContainerCounter());
        }

        // Trigger an INVENTORY_OPEN event
        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }

        // Now open the window
        InventoryType type = inventory.getType();
        String windowType = CraftContainer.getNotchInventoryType(type);
        String title = inventory.getTitle();
        int size = inventory.getTopInventory().getSize();
        player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, windowType, new ChatComponentText(title), size));
        player.activeContainer = container;
        player.activeContainer.addSlotListener(player);
    }

    @Override
	public void closeInventory() {
        getHandle().closeInventory();
    }

    @Override
	public boolean isBlocking() {
        return getHandle().isBlocking();
    }

    @Override
	public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    @Override
	public int getExpToLevel() {
        return getHandle().getExpToLevel();
    }
}
