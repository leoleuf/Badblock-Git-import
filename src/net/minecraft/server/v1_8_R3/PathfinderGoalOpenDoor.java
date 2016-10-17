package net.minecraft.server.v1_8_R3;

public class PathfinderGoalOpenDoor extends PathfinderGoalDoorInteract {

    boolean g;
    int h;

    public PathfinderGoalOpenDoor(EntityInsentient entityinsentient, boolean flag) {
        super(entityinsentient);
        this.a = entityinsentient;
        this.g = flag;
    }

    @Override
	public boolean b() {
        return this.g && this.h > 0 && super.b();
    }

    @Override
	public void c() {
        this.h = 20;
        this.c.setDoor(this.a.world, this.b, true);
    }

    @Override
	public void d() {
        if (this.g) {
            this.c.setDoor(this.a.world, this.b, false);
        }

    }

    @Override
	public void e() {
        --this.h;
        super.e();
    }
}
