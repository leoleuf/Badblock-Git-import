package net.minecraft.server.v1_8_R3;

public class PathfinderGoalLeapAtTarget extends PathfinderGoal {

    EntityInsentient a;
    EntityLiving b;
    float c;

    public PathfinderGoalLeapAtTarget(EntityInsentient entityinsentient, float f) {
        this.a = entityinsentient;
        this.c = f;
        this.a(5);
    }

    @Override
	public boolean a() {
        this.b = this.a.getGoalTarget();
        if (this.b == null) {
            return false;
        } else {
            double d0 = this.a.h(this.b);

            return d0 >= 4.0D && d0 <= 16.0D ? (!this.a.onGround ? false : this.a.bc().nextInt(5) == 0) : false;
        }
    }

    @Override
	public boolean b() {
        return !this.a.onGround;
    }

    @Override
	public void c() {
        double d0 = this.b.locX - this.a.locX;
        double d1 = this.b.locZ - this.a.locZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        this.a.motX += d0 / f * 0.5D * 0.800000011920929D + this.a.motX * 0.20000000298023224D;
        this.a.motZ += d1 / f * 0.5D * 0.800000011920929D + this.a.motZ * 0.20000000298023224D;
        this.a.motY = this.c;
    }
}
