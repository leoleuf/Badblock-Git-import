package net.minecraft.server.v1_8_R3;

public class TileEntityComparator extends TileEntity {

    private int a;

    public TileEntityComparator() {}

    @Override
	public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("OutputSignal", this.a);
    }

    @Override
	public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getInt("OutputSignal");
    }

    public int b() {
        return this.a;
    }

    public void a(int i) {
        this.a = i;
    }
}
