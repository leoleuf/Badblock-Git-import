package net.minecraft.server.v1_8_R3;

public class PathfinderGoalOcelotAttack extends PathfinderGoal {

    World a;
    EntityInsentient b;
    EntityLiving c;
    int d;

    public PathfinderGoalOcelotAttack(EntityInsentient entityinsentient) {
        this.b = entityinsentient;
        this.a = entityinsentient.world;
        this.a(3);
    }

    @Override
	public boolean a() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.c = entityliving;
            return true;
        }
    }

    @Override
	public boolean b() {
        return !this.c.isAlive() ? false : (this.b.h(this.c) > 225.0D ? false : !this.b.getNavigation().m() || this.a());
    }

    @Override
	public void d() {
        this.c = null;
        this.b.getNavigation().n();
    }

    @Override
	public void e() {
        this.b.getControllerLook().a(this.c, 30.0F, 30.0F);
        double d0 = this.b.width * 2.0F * this.b.width * 2.0F;
        double d1 = this.b.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
        double d2 = 0.8D;

        if (d1 > d0 && d1 < 16.0D) {
            d2 = 1.33D;
        } else if (d1 < 225.0D) {
            d2 = 0.6D;
        }

        this.b.getNavigation().a(this.c, d2);
        this.d = Math.max(this.d - 1, 0);
        if (d1 <= d0) {
            if (this.d <= 0) {
                this.d = 20;
                this.b.r(this.c);
            }
        }
    }
}
