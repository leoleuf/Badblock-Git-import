package net.minecraft.server.v1_8_R3;

public class MobEffectAbsorption extends MobEffectList {

    protected MobEffectAbsorption(int i, MinecraftKey minecraftkey, boolean flag, int j) {
        super(i, minecraftkey, flag, j);
    }

    @Override
	public void a(EntityLiving entityliving, AttributeMapBase attributemapbase, int i) {
        entityliving.setAbsorptionHearts(entityliving.getAbsorptionHearts() - 4 * (i + 1));
        super.a(entityliving, attributemapbase, i);
    }

    @Override
	public void b(EntityLiving entityliving, AttributeMapBase attributemapbase, int i) {
        entityliving.setAbsorptionHearts(entityliving.getAbsorptionHearts() + 4 * (i + 1));
        super.b(entityliving, attributemapbase, i);
    }
}
