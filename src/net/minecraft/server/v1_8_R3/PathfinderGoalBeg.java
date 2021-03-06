package net.minecraft.server.v1_8_R3;

public class PathfinderGoalBeg extends PathfinderGoal {

    private EntityWolf a;
    private EntityHuman b;
    private World c;
    private float d;
    private int e;

    public PathfinderGoalBeg(EntityWolf entitywolf, float f) {
        this.a = entitywolf;
        this.c = entitywolf.world;
        this.d = f;
        this.a(2);
    }

    @Override
	public boolean a() {
        this.b = this.c.findNearbyPlayer(this.a, this.d);
        return this.b == null ? false : this.a(this.b);
    }

    @Override
	public boolean b() {
        return !this.b.isAlive() ? false : (this.a.h(this.b) > this.d * this.d ? false : this.e > 0 && this.a(this.b));
    }

    @Override
	public void c() {
        this.a.p(true);
        this.e = 40 + this.a.bc().nextInt(40);
    }

    @Override
	public void d() {
        this.a.p(false);
        this.b = null;
    }

    @Override
	public void e() {
        this.a.getControllerLook().a(this.b.locX, this.b.locY + this.b.getHeadHeight(), this.b.locZ, 10.0F, this.a.bQ());
        --this.e;
    }

    private boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        return itemstack == null ? false : (!this.a.isTamed() && itemstack.getItem() == Items.BONE ? true : this.a.d(itemstack));
    }
}
