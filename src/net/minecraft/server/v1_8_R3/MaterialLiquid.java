package net.minecraft.server.v1_8_R3;

public class MaterialLiquid extends Material {

    public MaterialLiquid(MaterialMapColor materialmapcolor) {
        super(materialmapcolor);
        this.i();
        this.n();
    }

    @Override
	public boolean isLiquid() {
        return true;
    }

    @Override
	public boolean isSolid() {
        return false;
    }

    @Override
	public boolean isBuildable() {
        return false;
    }
}
