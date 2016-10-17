package net.minecraft.server.v1_8_R3;

public class ItemLeaves extends ItemBlock {

    private final BlockLeaves b;

    public ItemLeaves(BlockLeaves blockleaves) {
        super(blockleaves);
        this.b = blockleaves;
        this.setMaxDurability(0);
        this.a(true);
    }

    @Override
	public int filterData(int i) {
        return i | 4;
    }

    @Override
	public String e_(ItemStack itemstack) {
        return super.getName() + "." + this.b.b(itemstack.getData()).d();
    }
}
