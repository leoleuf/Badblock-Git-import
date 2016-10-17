package net.minecraft.server.v1_8_R3;

public class ItemBook extends Item {

    public ItemBook() {}

    @Override
	public boolean f_(ItemStack itemstack) {
        return itemstack.count == 1;
    }

    @Override
	public int b() {
        return 1;
    }
}
