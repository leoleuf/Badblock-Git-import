package net.minecraft.server.v1_8_R3;

public class EntityMinecartChest extends EntityMinecartContainer {

    public EntityMinecartChest(World world) {
        super(world);
    }

    public EntityMinecartChest(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    @Override
	public void a(DamageSource damagesource) {
        super.a(damagesource);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.a(Item.getItemOf(Blocks.CHEST), 1, 0.0F);
        }

    }

    @Override
	public int getSize() {
        return 27;
    }

    @Override
	public EntityMinecartAbstract.EnumMinecartType s() {
        return EntityMinecartAbstract.EnumMinecartType.CHEST;
    }

    @Override
	public IBlockData u() {
        return Blocks.CHEST.getBlockData().set(BlockChest.FACING, EnumDirection.NORTH);
    }

    @Override
	public int w() {
        return 8;
    }

    @Override
	public String getContainerName() {
        return "minecraft:chest";
    }

    @Override
	public Container createContainer(PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerChest(playerinventory, this, entityhuman);
    }
}
