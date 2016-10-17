package net.minecraft.server.v1_8_R3;

public abstract class TileEntityContainer extends TileEntity implements ITileEntityContainer, ITileInventory {

    private ChestLock a;

    public TileEntityContainer() {
        this.a = ChestLock.a;
    }

    @Override
	public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = ChestLock.b(nbttagcompound);
    }

    @Override
	public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.a != null) {
            this.a.a(nbttagcompound);
        }

    }

    @Override
	public boolean r_() {
        return this.a != null && !this.a.a();
    }

    @Override
	public ChestLock i() {
        return this.a;
    }

    @Override
	public void a(ChestLock chestlock) {
        this.a = chestlock;
    }

    @Override
	public IChatBaseComponent getScoreboardDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName(), new Object[0]);
    }
}
