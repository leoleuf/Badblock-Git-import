package net.minecraft.server.v1_8_R3;

public abstract class EntityAmbient extends EntityInsentient implements IAnimal {

    public EntityAmbient(World world) {
        super(world);
    }

    @Override
	public boolean cb() {
        return false;
    }

    @Override
	protected boolean a(EntityHuman entityhuman) {
        return false;
    }
}
