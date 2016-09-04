package net.minecraft.server.v1_8_R3;

public class TileEntityEnchantTable extends TileEntity implements ITileEntityContainer {

    private String p;

    public TileEntityEnchantTable() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.p);
        }

    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.p = nbttagcompound.getString("CustomName");
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.p : "container.enchant";
    }

    public boolean hasCustomName() {
        return this.p != null && this.p.length() > 0;
    }

    public void a(String s) {
        this.p = s;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return (IChatBaseComponent) (this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]));
    }

    public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerEnchantTable(playerinventory, this.world, this.position);
    }

    public String getContainerName() {
        return "minecraft:enchanting_table";
    }
}
