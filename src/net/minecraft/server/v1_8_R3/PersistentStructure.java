package net.minecraft.server.v1_8_R3;

public class PersistentStructure extends PersistentBase {

    private NBTTagCompound b = new NBTTagCompound();

    public PersistentStructure(String s) {
        super(s);
    }

    @Override
	public void a(NBTTagCompound nbttagcompound) {
        this.b = nbttagcompound.getCompound("Features");
    }

    @Override
	public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.set("Features", this.b);
    }

    public void a(NBTTagCompound nbttagcompound, int i, int j) {
        this.b.set(b(i, j), nbttagcompound);
    }

    public static String b(int i, int j) {
        return "[" + i + "," + j + "]";
    }

    public NBTTagCompound a() {
        return this.b;
    }
}
