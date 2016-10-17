package net.minecraft.server.v1_8_R3;

public class ItemSaddle extends Item {

    public ItemSaddle() {
        this.maxStackSize = 1;
        this.a(CreativeModeTab.e);
    }

    @Override
	public boolean a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving) {
        if (entityliving instanceof EntityPig) {
            EntityPig entitypig = (EntityPig) entityliving;

            if (!entitypig.hasSaddle() && !entitypig.isBaby()) {
                entitypig.setSaddle(true);
                entitypig.world.makeSound(entitypig, "mob.horse.leather", 0.5F, 1.0F);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
	public boolean a(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
        this.a(itemstack, (EntityHuman) null, entityliving);
        return true;
    }
}
