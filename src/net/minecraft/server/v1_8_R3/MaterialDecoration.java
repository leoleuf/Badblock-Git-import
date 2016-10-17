package net.minecraft.server.v1_8_R3;

public class MaterialDecoration extends Material {

    public MaterialDecoration(MaterialMapColor materialmapcolor) {
        super(materialmapcolor);
        this.p();
    }

    @Override
	public boolean isBuildable() {
        return false;
    }

    @Override
	public boolean blocksLight() {
        return false;
    }

    @Override
	public boolean isSolid() {
        return false;
    }
}
