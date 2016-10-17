package net.minecraft.server.v1_8_R3;

public class BlockHalfTransparent extends Block {

    private boolean a;

    protected BlockHalfTransparent(Material material, boolean flag) {
        this(material, flag, material.r());
    }

    protected BlockHalfTransparent(Material material, boolean flag, MaterialMapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.a = flag;
    }

    @Override
	public boolean c() {
        return false;
    }
}
