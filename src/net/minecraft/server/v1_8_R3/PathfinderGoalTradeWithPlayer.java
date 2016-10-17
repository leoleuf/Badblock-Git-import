package net.minecraft.server.v1_8_R3;

public class PathfinderGoalTradeWithPlayer extends PathfinderGoal {

    private EntityVillager a;

    public PathfinderGoalTradeWithPlayer(EntityVillager entityvillager) {
        this.a = entityvillager;
        this.a(5);
    }

    @Override
	public boolean a() {
        if (!this.a.isAlive()) {
            return false;
        } else if (this.a.V()) {
            return false;
        } else if (!this.a.onGround) {
            return false;
        } else if (this.a.velocityChanged) {
            return false;
        } else {
            EntityHuman entityhuman = this.a.v_();

            return entityhuman == null ? false : (this.a.h(entityhuman) > 16.0D ? false : entityhuman.activeContainer instanceof Container);
        }
    }

    @Override
	public void c() {
        this.a.getNavigation().n();
    }

    @Override
	public void d() {
        this.a.a_((EntityHuman) null);
    }
}
