package net.minecraft.server.v1_8_R3;

public class PathfinderGoalMoveTowardsTarget extends PathfinderGoal {

    private EntityCreature a;
    private EntityLiving b;
    private double c;
    private double d;
    private double e;
    private double f;
    private float g;

    public PathfinderGoalMoveTowardsTarget(EntityCreature entitycreature, double d0, float f) {
        this.a = entitycreature;
        this.f = d0;
        this.g = f;
        this.a(1);
    }

    @Override
	public boolean a() {
        this.b = this.a.getGoalTarget();
        if (this.b == null) {
            return false;
        } else if (this.b.h(this.a) > this.g * this.g) {
            return false;
        } else {
            Vec3D vec3d = RandomPositionGenerator.a(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));

            if (vec3d == null) {
                return false;
            } else {
                this.c = vec3d.a;
                this.d = vec3d.b;
                this.e = vec3d.c;
                return true;
            }
        }
    }

    @Override
	public boolean b() {
        return !this.a.getNavigation().m() && this.b.isAlive() && this.b.h(this.a) < this.g * this.g;
    }

    @Override
	public void d() {
        this.b = null;
    }

    @Override
	public void c() {
        this.a.getNavigation().a(this.c, this.d, this.e, this.f);
    }
}
