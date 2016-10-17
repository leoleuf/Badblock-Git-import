package net.minecraft.server.v1_8_R3;

public class MaterialGas extends Material {

    public MaterialGas(MaterialMapColor materialmapcolor) {
        super(materialmapcolor);
        this.i();
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
