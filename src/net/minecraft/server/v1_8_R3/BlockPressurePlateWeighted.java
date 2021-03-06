package net.minecraft.server.v1_8_R3;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

import fr.badblock.minecraftserver.BadblockConfig;

public class BlockPressurePlateWeighted extends BlockPressurePlateAbstract {

    public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
    private final int weight;

    protected BlockPressurePlateWeighted(Material material, int i) {
        this(material, i, material.r());
    }

    protected BlockPressurePlateWeighted(Material material, int i, MaterialMapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.j(this.blockStateList.getBlockData().set(BlockPressurePlateWeighted.POWER, Integer.valueOf(0)));
        this.weight = i;
    }

    @Override
	protected int f(World world, BlockPosition blockposition) {
    	if(!BadblockConfig.config.redstone.usePressurePlate)
    		return 0;
    	
        // CraftBukkit start
        //int i = Math.min(world.a(Entity.class, this.a(blockposition)).size(), this.b);
        int i = 0;
        java.util.Iterator iterator = world.a(Entity.class, this.getBoundingBox(blockposition)).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            org.bukkit.event.Cancellable cancellable;

            if (entity instanceof EntityHuman) {
                cancellable = org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            // We only want to block turning the plate on if all events are cancelled
            if (!cancellable.isCancelled()) {
                i++;
            }
        }

        i = Math.min(i, this.weight);
        // CraftBukkit end

        if (i > 0) {
            float f = (float) Math.min(this.weight, i) / (float) this.weight;

            return MathHelper.f(f * 15.0F);
        } else {
            return 0;
        }
    }

    @Override
	protected int e(IBlockData iblockdata) {
    	if(!BadblockConfig.config.redstone.usePressurePlate)
    		return 0;
    	
        return iblockdata.get(BlockPressurePlateWeighted.POWER).intValue();
    }

    @Override
	protected IBlockData a(IBlockData iblockdata, int i) {
        return iblockdata.set(BlockPressurePlateWeighted.POWER, Integer.valueOf(i));
    }

    @Override
	public int a(World world) {
        return 10;
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockPressurePlateWeighted.POWER, Integer.valueOf(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockPressurePlateWeighted.POWER).intValue();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockPressurePlateWeighted.POWER});
    }
}
