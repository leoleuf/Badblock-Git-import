package net.minecraft.server.v1_8_R3;

import fr.badblock.minecraftserver.BadblockConfig;

public class BlockFenceGate extends BlockDirectional {

    public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    public static final BlockStateBoolean IN_WALL = BlockStateBoolean.of("in_wall");

    public BlockFenceGate(BlockWood.EnumLogVariant blockwood_enumlogvariant) {
        super(Material.WOOD, blockwood_enumlogvariant.c());
        this.j(this.blockStateList.getBlockData().set(BlockFenceGate.OPEN, Boolean.valueOf(false)).set(BlockFenceGate.POWERED, Boolean.valueOf(false)).set(BlockFenceGate.IN_WALL, Boolean.valueOf(false)));
        this.a(CreativeModeTab.d);
    }

    @Override
	public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection.EnumAxis enumdirection_enumaxis = iblockdata.get(BlockDirectional.FACING).k();

        if (enumdirection_enumaxis == EnumDirection.EnumAxis.Z && (iblockaccess.getType(blockposition.west()).getBlock() == Blocks.COBBLESTONE_WALL || iblockaccess.getType(blockposition.east()).getBlock() == Blocks.COBBLESTONE_WALL) || enumdirection_enumaxis == EnumDirection.EnumAxis.X && (iblockaccess.getType(blockposition.north()).getBlock() == Blocks.COBBLESTONE_WALL || iblockaccess.getType(blockposition.south()).getBlock() == Blocks.COBBLESTONE_WALL)) {
            iblockdata = iblockdata.set(BlockFenceGate.IN_WALL, Boolean.valueOf(true));
        }

        return iblockdata;
    }

    @Override
	public boolean canPlace(World world, BlockPosition blockposition) {
        return world.getType(blockposition.down()).getBlock().getMaterial().isBuildable() ? super.canPlace(world, blockposition) : false;
    }

    @Override
	public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (iblockdata.get(BlockFenceGate.OPEN).booleanValue()) {
            return null;
        } else {
            EnumDirection.EnumAxis enumdirection_enumaxis = iblockdata.get(BlockDirectional.FACING).k();

            return enumdirection_enumaxis == EnumDirection.EnumAxis.Z ? new AxisAlignedBB(blockposition.getX(), blockposition.getY(), blockposition.getZ() + 0.375F, blockposition.getX() + 1, blockposition.getY() + 1.5F, blockposition.getZ() + 0.625F) : new AxisAlignedBB(blockposition.getX() + 0.375F, blockposition.getY(), blockposition.getZ(), blockposition.getX() + 0.625F, blockposition.getY() + 1.5F, blockposition.getZ() + 1);
        }
    }

    @Override
	public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection.EnumAxis enumdirection_enumaxis = iblockaccess.getType(blockposition).get(BlockDirectional.FACING).k();

        if (enumdirection_enumaxis == EnumDirection.EnumAxis.Z) {
            this.a(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        } else {
            this.a(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }

    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockaccess.getType(blockposition).get(BlockFenceGate.OPEN).booleanValue();
    }

    @Override
	public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        return this.getBlockData().set(BlockDirectional.FACING, entityliving.getDirection()).set(BlockFenceGate.OPEN, Boolean.valueOf(false)).set(BlockFenceGate.POWERED, Boolean.valueOf(false)).set(BlockFenceGate.IN_WALL, Boolean.valueOf(false));
    }

    @Override
	public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (iblockdata.get(BlockFenceGate.OPEN).booleanValue()) {
            iblockdata = iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(false));
            world.setTypeAndData(blockposition, iblockdata, 2);
        } else {
            EnumDirection enumdirection1 = EnumDirection.fromAngle(entityhuman.yaw);

            if (iblockdata.get(BlockDirectional.FACING) == enumdirection1.opposite()) {
                iblockdata = iblockdata.set(BlockDirectional.FACING, enumdirection1);
            }

            iblockdata = iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(true));
            world.setTypeAndData(blockposition, iblockdata, 2);
        }

        world.a(entityhuman, iblockdata.get(BlockFenceGate.OPEN).booleanValue() ? 1003 : 1006, blockposition, 0);
        return true;
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
    	if(!BadblockConfig.config.redstone.useDoors)
    		return;
    	
        if (!world.isClientSide) {
            boolean flag = world.isBlockIndirectlyPowered(blockposition);

            if (flag || block.isPowerSource()) {
                if (flag && !iblockdata.get(BlockFenceGate.OPEN).booleanValue() && !iblockdata.get(BlockFenceGate.POWERED).booleanValue()) {
                    world.setTypeAndData(blockposition, iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(true)).set(BlockFenceGate.POWERED, Boolean.valueOf(true)), 2);
                    world.a((EntityHuman) null, 1003, blockposition, 0);
                } else if (!flag && iblockdata.get(BlockFenceGate.OPEN).booleanValue() && iblockdata.get(BlockFenceGate.POWERED).booleanValue()) {
                    world.setTypeAndData(blockposition, iblockdata.set(BlockFenceGate.OPEN, Boolean.valueOf(false)).set(BlockFenceGate.POWERED, Boolean.valueOf(false)), 2);
                    world.a((EntityHuman) null, 1006, blockposition, 0);
                } else if (flag != iblockdata.get(BlockFenceGate.POWERED).booleanValue()) {
                    world.setTypeAndData(blockposition, iblockdata.set(BlockFenceGate.POWERED, Boolean.valueOf(flag)), 2);
                }
            }

        }
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockDirectional.FACING, EnumDirection.fromType2(i)).set(BlockFenceGate.OPEN, Boolean.valueOf((i & 4) != 0)).set(BlockFenceGate.POWERED, Boolean.valueOf((i & 8) != 0));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockDirectional.FACING).b();

        if (iblockdata.get(BlockFenceGate.POWERED).booleanValue()) {
            i |= 8;
        }

        if (iblockdata.get(BlockFenceGate.OPEN).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockDirectional.FACING, BlockFenceGate.OPEN, BlockFenceGate.POWERED, BlockFenceGate.IN_WALL});
    }
}
