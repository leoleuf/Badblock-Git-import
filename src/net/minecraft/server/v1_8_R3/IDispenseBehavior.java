package net.minecraft.server.v1_8_R3;

public interface IDispenseBehavior {

    IDispenseBehavior NONE = new IDispenseBehavior() {
        public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
            return itemstack;
        }
    };

    ItemStack a(ISourceBlock isourceblock, ItemStack itemstack);
}
