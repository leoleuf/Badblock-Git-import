package net.minecraft.server.v1_8_R3;

import java.util.List;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {

    private boolean a = true;
    private int b = -1;
    private BlockPosition c;

    public EntityMinecartHopper(World world) {
        super(world);
        this.c = BlockPosition.ZERO;
    }

    public EntityMinecartHopper(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.c = BlockPosition.ZERO;
    }

    @Override
	public EntityMinecartAbstract.EnumMinecartType s() {
        return EntityMinecartAbstract.EnumMinecartType.HOPPER;
    }

    @Override
	public IBlockData u() {
        return Blocks.HOPPER.getBlockData();
    }

    @Override
	public int w() {
        return 1;
    }

    @Override
	public int getSize() {
        return 5;
    }

    @Override
	public boolean e(EntityHuman entityhuman) {
        if (!this.world.isClientSide) {
            entityhuman.openContainer(this);
        }

        return true;
    }

    @Override
	public void a(int i, int j, int k, boolean flag) {
        boolean flag1 = !flag;

        if (flag1 != this.y()) {
            this.i(flag1);
        }

    }

    public boolean y() {
        return this.a;
    }

    public void i(boolean flag) {
        this.a = flag;
    }

    @Override
	public World getWorld() {
        return this.world;
    }

    @Override
	public double A() {
        return this.locX;
    }

    @Override
	public double B() {
        return this.locY + 0.5D;
    }

    @Override
	public double C() {
        return this.locZ;
    }

    @Override
	public void t_() {
        super.t_();
        if (!this.world.isClientSide && this.isAlive() && this.y()) {
            BlockPosition blockposition = new BlockPosition(this);

            if (blockposition.equals(this.c)) {
                --this.b;
            } else {
                this.m(0);
            }

            if (!this.E()) {
                this.m(0);
                if (this.D()) {
                    this.m(4);
                    this.update();
                }
            }
        }

    }

    public boolean D() {
        if (TileEntityHopper.a(this)) {
            return true;
        } else {
            List list = this.world.a(EntityItem.class, this.getBoundingBox().grow(0.25D, 0.0D, 0.25D), IEntitySelector.a);

            if (list.size() > 0) {
                TileEntityHopper.a((IInventory) this, (EntityItem) list.get(0));
            }

            return false;
        }
    }

    @Override
	public void a(DamageSource damagesource) {
        super.a(damagesource);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.a(Item.getItemOf(Blocks.HOPPER), 1, 0.0F);
        }

    }

    @Override
	protected void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("TransferCooldown", this.b);
    }

    @Override
	protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.b = nbttagcompound.getInt("TransferCooldown");
    }

    public void m(int i) {
        this.b = i;
    }

    public boolean E() {
        return this.b > 0;
    }

    @Override
	public String getContainerName() {
        return "minecraft:hopper";
    }

    @Override
	public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerHopper(playerinventory, this, entityhuman);
    }
}
