package net.minecraft.server.v1_8_R3;

public class SlotResult extends Slot {

    private final InventoryCrafting a;
    private final EntityHuman b;
    private int c;

    public SlotResult(EntityHuman entityhuman, InventoryCrafting inventorycrafting, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.b = entityhuman;
        this.a = inventorycrafting;
    }

    @Override
	public boolean isAllowed(ItemStack itemstack) {
        return false;
    }

    @Override
	public ItemStack a(int i) {
        if (this.hasItem()) {
            this.c += Math.min(i, this.getItem().count);
        }

        return super.a(i);
    }

    @Override
	protected void a(ItemStack itemstack, int i) {
        this.c += i;
        this.c(itemstack);
    }

    @Override
	protected void c(ItemStack itemstack) {
        if (this.c > 0) {
            itemstack.a(this.b.world, this.b, this.c);
        }

        this.c = 0;
        if (itemstack.getItem() == Item.getItemOf(Blocks.CRAFTING_TABLE)) {
            this.b.b(AchievementList.h);
        }

        if (itemstack.getItem() instanceof ItemPickaxe) {
            this.b.b(AchievementList.i);
        }

        if (itemstack.getItem() == Item.getItemOf(Blocks.FURNACE)) {
            this.b.b(AchievementList.j);
        }

        if (itemstack.getItem() instanceof ItemHoe) {
            this.b.b(AchievementList.l);
        }

        if (itemstack.getItem() == Items.BREAD) {
            this.b.b(AchievementList.m);
        }

        if (itemstack.getItem() == Items.CAKE) {
            this.b.b(AchievementList.n);
        }

        if (itemstack.getItem() instanceof ItemPickaxe && ((ItemPickaxe) itemstack.getItem()).g() != Item.EnumToolMaterial.WOOD) {
            this.b.b(AchievementList.o);
        }

        if (itemstack.getItem() instanceof ItemSword) {
            this.b.b(AchievementList.r);
        }

        if (itemstack.getItem() == Item.getItemOf(Blocks.ENCHANTING_TABLE)) {
            this.b.b(AchievementList.E);
        }

        if (itemstack.getItem() == Item.getItemOf(Blocks.BOOKSHELF)) {
            this.b.b(AchievementList.G);
        }

        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getData() == 1) {
            this.b.b(AchievementList.M);
        }

    }

    @Override
	public void a(EntityHuman entityhuman, ItemStack itemstack) {
        this.c(itemstack);
        ItemStack[] aitemstack = CraftingManager.getInstance().b(this.a, entityhuman.world);

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack1 = this.a.getItem(i);
            ItemStack itemstack2 = aitemstack[i];

            if (itemstack1 != null) {
                this.a.splitStack(i, 1);
            }

            if (itemstack2 != null) {
                if (this.a.getItem(i) == null) {
                    this.a.setItem(i, itemstack2);
                } else if (!this.b.inventory.pickup(itemstack2)) {
                    this.b.drop(itemstack2, false);
                }
            }
        }

    }
}
