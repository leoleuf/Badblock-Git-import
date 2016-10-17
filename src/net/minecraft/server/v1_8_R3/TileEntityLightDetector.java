package net.minecraft.server.v1_8_R3;

import fr.badblock.minecraftserver.BadblockConfig;

public class TileEntityLightDetector extends TileEntity implements IUpdatePlayerListBox {

    public TileEntityLightDetector() {}

    @Override
	public void c() {
        if (this.world != null && !this.world.isClientSide && this.world.getTime() % 20L == 0L) {
            this.e = this.w();
            if (this.e instanceof BlockDaylightDetector) {
                ((BlockDaylightDetector) this.e).f(this.world, this.position);
            }
        }

    }
    
    @Override
    public boolean mustUpdatePlayerListBox(){
    	return BadblockConfig.config.tileEntities.tickLightDetector;
    }
}
