package net.minecraft.server.v1_8_R3;

public class InstantMobEffect extends MobEffectList {

    public InstantMobEffect(int i, MinecraftKey minecraftkey, boolean flag, int j) {
        super(i, minecraftkey, flag, j);
    }

    @Override
	public boolean isInstant() {
        return true;
    }

    @Override
	public boolean a(int i, int j) {
        return i >= 1;
    }
}
