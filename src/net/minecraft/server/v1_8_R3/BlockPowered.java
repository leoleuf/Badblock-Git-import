package net.minecraft.server.v1_8_R3;

import fr.badblock.minecraftserver.BadblockConfig;

public class BlockPowered extends Block {

    public BlockPowered(Material material, MaterialMapColor materialmapcolor) {
        super(material, materialmapcolor);
    }

    @Override
	public boolean isPowerSource() {
        return BadblockConfig.config.redstone.usePoweredBlocks;
    }

    @Override
	public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection) {
        return BadblockConfig.config.redstone.usePoweredBlocks ? 15 : 0;
    }
}
