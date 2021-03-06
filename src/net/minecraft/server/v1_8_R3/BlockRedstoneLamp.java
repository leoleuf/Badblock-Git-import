package net.minecraft.server.v1_8_R3;

import java.util.Random;

import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

import fr.badblock.minecraftserver.BadblockConfig;

public class BlockRedstoneLamp extends Block {

    private final boolean a;

    public BlockRedstoneLamp(boolean flag) {
        super(Material.BUILDABLE_GLASS);
        this.a = flag;
        if (flag) {
            this.a(1.0F);
        }

    }

    @Override
	public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
    	if(!BadblockConfig.config.redstone.useRedstoneLamp)
    		return;
    	
        if (!world.isClientSide) {
            if (this.a && !world.isBlockIndirectlyPowered(blockposition)) {
                world.setTypeAndData(blockposition, Blocks.REDSTONE_LAMP.getBlockData(), 2);
            } else if (!this.a && world.isBlockIndirectlyPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, Blocks.LIT_REDSTONE_LAMP.getBlockData(), 2);
            }

        }
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
    	if(!BadblockConfig.config.redstone.useRedstoneLamp)
    		return;
    	
        if (!world.isClientSide) {
            if (this.a && !world.isBlockIndirectlyPowered(blockposition)) {
                world.a(blockposition, this, 4);
            } else if (!this.a && world.isBlockIndirectlyPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, Blocks.LIT_REDSTONE_LAMP.getBlockData(), 2);
            }

        }
    }

    @Override
	public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
    	if(!BadblockConfig.config.redstone.useRedstoneLamp)
    		return;
    	
        if (!world.isClientSide) {
            if (this.a && !world.isBlockIndirectlyPowered(blockposition)) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeAndData(blockposition, Blocks.REDSTONE_LAMP.getBlockData(), 2);
            }

        }
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Item.getItemOf(Blocks.REDSTONE_LAMP);
    }

    @Override
	protected ItemStack i(IBlockData iblockdata) {
        return new ItemStack(Blocks.REDSTONE_LAMP);
    }
}
