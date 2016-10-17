package net.minecraft.server.v1_8_R3;

public class SecondaryWorldData extends WorldData {

    private final WorldData b;

    public SecondaryWorldData(WorldData worlddata) {
        this.b = worlddata;
    }

    @Override
	public NBTTagCompound a() {
        return this.b.a();
    }

    @Override
	public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        return this.b.a(nbttagcompound);
    }

    @Override
	public long getSeed() {
        return this.b.getSeed();
    }

    @Override
	public int c() {
        return this.b.c();
    }

    @Override
	public int d() {
        return this.b.d();
    }

    @Override
	public int e() {
        return this.b.e();
    }

    @Override
	public long getTime() {
        return this.b.getTime();
    }

    @Override
	public long getDayTime() {
        return this.b.getDayTime();
    }

    @Override
	public NBTTagCompound i() {
        return this.b.i();
    }

    @Override
	public String getName() {
        return this.b.getName();
    }

    @Override
	public int l() {
        return this.b.l();
    }

    @Override
	public boolean isThundering() {
        return this.b.isThundering();
    }

    @Override
	public int getThunderDuration() {
        return this.b.getThunderDuration();
    }

    @Override
	public boolean hasStorm() {
        return this.b.hasStorm();
    }

    @Override
	public int getWeatherDuration() {
        return this.b.getWeatherDuration();
    }

    @Override
	public WorldSettings.EnumGamemode getGameType() {
        return this.b.getGameType();
    }

    @Override
	public void setTime(long i) {}

    @Override
	public void setDayTime(long i) {}

    @Override
	public void setSpawn(BlockPosition blockposition) {}

    @Override
	public void a(String s) {}

    @Override
	public void e(int i) {}

    @Override
	public void setThundering(boolean flag) {}

    @Override
	public void setThunderDuration(int i) {}

    @Override
	public void setStorm(boolean flag) {}

    @Override
	public void setWeatherDuration(int i) {}

    @Override
	public boolean shouldGenerateMapFeatures() {
        return this.b.shouldGenerateMapFeatures();
    }

    @Override
	public boolean isHardcore() {
        return this.b.isHardcore();
    }

    @Override
	public WorldType getType() {
        return this.b.getType();
    }

    @Override
	public void a(WorldType worldtype) {}

    @Override
	public boolean v() {
        return this.b.v();
    }

    @Override
	public void c(boolean flag) {}

    @Override
	public boolean w() {
        return this.b.w();
    }

    @Override
	public void d(boolean flag) {}

    @Override
	public GameRules x() {
        return this.b.x();
    }

    @Override
	public EnumDifficulty getDifficulty() {
        return this.b.getDifficulty();
    }

    @Override
	public void setDifficulty(EnumDifficulty enumdifficulty) {}

    @Override
	public boolean isDifficultyLocked() {
        return this.b.isDifficultyLocked();
    }

    @Override
	public void e(boolean flag) {}
}
