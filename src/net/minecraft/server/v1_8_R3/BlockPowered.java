package net.minecraft.server.v1_8_R3;

public class BlockPowered extends Block {

    public BlockPowered(Material material, MaterialMapColor materialmapcolor) {
        super(material, materialmapcolor);
    }

    @Override
	public boolean isPowerSource() {
        return true;
    }

    @Override
	public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return 15;
    }
}
