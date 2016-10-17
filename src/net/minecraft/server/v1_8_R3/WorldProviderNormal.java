package net.minecraft.server.v1_8_R3;

public class WorldProviderNormal extends WorldProvider {

    public WorldProviderNormal() {}

    @Override
	public String getName() {
        return "Overworld";
    }

    @Override
	public String getSuffix() {
        return "";
    }
}
