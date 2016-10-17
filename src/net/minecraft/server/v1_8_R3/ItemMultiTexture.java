package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;

public class ItemMultiTexture extends ItemBlock {

    protected final Block b;
    protected final Function<ItemStack, String> c;

    public ItemMultiTexture(Block block, Block block1, Function<ItemStack, String> function) {
        super(block);
        this.b = block1;
        this.c = function;
        this.setMaxDurability(0);
        this.a(true);
    }

    public ItemMultiTexture(Block block, Block block1, final String[] astring) {
        this(block, block1, new Function() {
            public String a(ItemStack itemstack) {
                int i = itemstack.getData();

                if (i < 0 || i >= astring.length) {
                    i = 0;
                }

                return astring[i];
            }

            @Override
			public Object apply(Object object) {
                return this.a((ItemStack) object);
            }
        });
    }

    @Override
	public int filterData(int i) {
        return i;
    }

    @Override
	public String e_(ItemStack itemstack) {
        return super.getName() + "." + this.c.apply(itemstack);
    }
}
