package net.minecraft.server.v1_8_R3;

public class EntityThrownExpBottle extends EntityProjectile {

    public EntityThrownExpBottle(World world) {
        super(world);
    }

    public EntityThrownExpBottle(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityThrownExpBottle(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    @Override
	protected float m() {
        return 0.07F;
    }

    @Override
	protected float j() {
        return 0.7F;
    }

    @Override
	protected float l() {
        return -20.0F;
    }

    @Override
	protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isClientSide) {
            // CraftBukkit - moved to after event
            // this.world.triggerEffect(2002, new BlockPosition(this), 0);
            int i = 3 + this.world.random.nextInt(5) + this.world.random.nextInt(5);

            // CraftBukkit start
            org.bukkit.event.entity.ExpBottleEvent event = org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory.callExpBottleEvent(this, i);
            i = event.getExperience();
            if (event.getShowEffect()) {
                this.world.triggerEffect(2002, new BlockPosition(this), 0);
            }
            // CraftBukkit end

            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
            }

            this.die();
        }

    }
}
