package net.minecraft.server.v1_8_R3;

public class TileEntityEnchantTable extends TileEntity implements ITileEntityContainer {

    private String p;

    public TileEntityEnchantTable() {}

    @Override
	public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.p);
        }

    }

    @Override
	public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.p = nbttagcompound.getString("CustomName");
        }

    }

    @Override
	public String getName() {
        return this.hasCustomName() ? this.p : "container.enchant";
    }

    @Override
	public boolean hasCustomName() {
        return this.p != null && this.p.length() > 0;
    }

    public void a(String s) {
        this.p = s;
    }

    @Override
	public IChatBaseComponent getScoreboardDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]);
    }

    @Override
	public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerEnchantTable(playerinventory, this.world, this.position);
    }

    @Override
	public String getContainerName() {
        return "minecraft:enchanting_table";
    }
}
