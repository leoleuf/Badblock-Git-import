package net.minecraft.server.v1_8_R3;

public class EntityGiantZombie extends EntityMonster {

    public EntityGiantZombie(World world) {
        super(world);
        this.setSize(this.width * 6.0F, this.length * 6.0F);
    }

    @Override
	public float getHeadHeight() {
        return 10.440001F;
    }

    @Override
	protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(50.0D);
    }

    @Override
	public float a(BlockPosition blockposition) {
        return this.world.o(blockposition) - 0.5F;
    }
}
