package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockGlass extends BlockHalfTransparent {

    public BlockGlass(Material material, boolean flag) {
        super(material, flag);
        this.a(CreativeModeTab.b);
    }

    @Override
	public int a(Random random) {
        return 0;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	protected boolean I() {
        return true;
    }
}
