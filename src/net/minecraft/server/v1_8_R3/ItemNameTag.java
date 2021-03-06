package net.minecraft.server.v1_8_R3;

public class ItemNameTag extends Item {

    public ItemNameTag() {
        this.a(CreativeModeTab.i);
    }

    @Override
	public boolean a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving) {
        if (!itemstack.hasName()) {
            return false;
        } else if (entityliving instanceof EntityInsentient) {
            EntityInsentient entityinsentient = (EntityInsentient) entityliving;

            entityinsentient.setCustomName(itemstack.getName());
            entityinsentient.bX();
            --itemstack.count;
            return true;
        } else {
            return super.a(itemstack, entityhuman, entityliving);
        }
    }
}
