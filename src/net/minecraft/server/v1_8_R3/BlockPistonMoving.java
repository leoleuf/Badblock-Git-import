package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class BlockPistonMoving extends BlockContainer {

    public static final BlockStateDirection FACING = BlockPistonExtension.FACING;
    public static final BlockStateEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;

    public BlockPistonMoving() {
        super(Material.PISTON);
        this.j(this.blockStateList.getBlockData().set(BlockPistonMoving.FACING, EnumDirection.NORTH).set(BlockPistonMoving.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.c(-1.0F);
    }

    @Override
	public TileEntity a(World world, int i) {
        return null;
    }

    public static TileEntity a(IBlockData iblockdata, EnumDirection enumdirection, boolean flag, boolean flag1) {
        return new TileEntityPiston(iblockdata, enumdirection, flag, flag1);
    }

    @Override
	public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityPiston) {
            ((TileEntityPiston) tileentity).h();
        } else {
            super.remove(world, blockposition, iblockdata);
        }

    }

    @Override
	public boolean canPlace(World world, BlockPosition blockposition) {
        return false;
    }

    @Override
	public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return false;
    }

    @Override
	public void postBreak(World world, BlockPosition blockposition, IBlockData iblockdata) {
        BlockPosition blockposition1 = blockposition.shift(iblockdata.get(BlockPistonMoving.FACING).opposite());
        IBlockData iblockdata1 = world.getType(blockposition1);

        if (iblockdata1.getBlock() instanceof BlockPiston && iblockdata1.get(BlockPiston.EXTENDED).booleanValue()) {
            world.setAir(blockposition1);
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
	public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (!world.isClientSide && world.getTileEntity(blockposition) == null) {
            world.setAir(blockposition);
            return true;
        } else {
            return false;
        }
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return null;
    }

    @Override
	public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        if (!world.isClientSide) {
            TileEntityPiston tileentitypiston = this.e((IBlockAccess) world, blockposition);

            if (tileentitypiston != null) {
                IBlockData iblockdata1 = tileentitypiston.b();

                iblockdata1.getBlock().b(world, blockposition, iblockdata1, 0);
            }
        }
    }

    @Override
	public MovingObjectPosition a(World world, BlockPosition blockposition, Vec3D vec3d, Vec3D vec3d1) {
        return null;
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        if (!world.isClientSide) {
            world.getTileEntity(blockposition);
        }

    }

    @Override
	public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        TileEntityPiston tileentitypiston = this.e((IBlockAccess) world, blockposition);

        if (tileentitypiston == null) {
            return null;
        } else {
            float f = tileentitypiston.a(0.0F);

            if (tileentitypiston.d()) {
                f = 1.0F - f;
            }

            return this.a(world, blockposition, tileentitypiston.b(), f, tileentitypiston.e());
        }
    }

    @Override
	public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
        TileEntityPiston tileentitypiston = this.e(iblockaccess, blockposition);

        if (tileentitypiston != null) {
            IBlockData iblockdata = tileentitypiston.b();
            Block block = iblockdata.getBlock();

            if (block == this || block.getMaterial() == Material.AIR) {
                return;
            }

            float f = tileentitypiston.a(0.0F);

            if (tileentitypiston.d()) {
                f = 1.0F - f;
            }

            block.updateShape(iblockaccess, blockposition);
            if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON) {
                f = 0.0F;
            }

            EnumDirection enumdirection = tileentitypiston.e();

            this.minX = block.B() - enumdirection.getAdjacentX() * f;
            this.minY = block.D() - enumdirection.getAdjacentY() * f;
            this.minZ = block.F() - enumdirection.getAdjacentZ() * f;
            this.maxX = block.C() - enumdirection.getAdjacentX() * f;
            this.maxY = block.E() - enumdirection.getAdjacentY() * f;
            this.maxZ = block.G() - enumdirection.getAdjacentZ() * f;
        }

    }

    public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata, float f, EnumDirection enumdirection) {
        if (iblockdata.getBlock() != this && iblockdata.getBlock().getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = iblockdata.getBlock().a(world, blockposition, iblockdata);

            if (axisalignedbb == null) {
                return null;
            } else {
                double d0 = axisalignedbb.a;
                double d1 = axisalignedbb.b;
                double d2 = axisalignedbb.c;
                double d3 = axisalignedbb.d;
                double d4 = axisalignedbb.e;
                double d5 = axisalignedbb.f;

                if (enumdirection.getAdjacentX() < 0) {
                    d0 -= enumdirection.getAdjacentX() * f;
                } else {
                    d3 -= enumdirection.getAdjacentX() * f;
                }

                if (enumdirection.getAdjacentY() < 0) {
                    d1 -= enumdirection.getAdjacentY() * f;
                } else {
                    d4 -= enumdirection.getAdjacentY() * f;
                }

                if (enumdirection.getAdjacentZ() < 0) {
                    d2 -= enumdirection.getAdjacentZ() * f;
                } else {
                    d5 -= enumdirection.getAdjacentZ() * f;
                }

                return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
            }
        } else {
            return null;
        }
    }

    private TileEntityPiston e(IBlockAccess iblockaccess, BlockPosition blockposition) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        return tileentity instanceof TileEntityPiston ? (TileEntityPiston) tileentity : null;
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockPistonMoving.FACING, BlockPistonExtension.b(i)).set(BlockPistonMoving.TYPE, (i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockPistonMoving.FACING).a();

        if (iblockdata.get(BlockPistonMoving.TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 8;
        }

        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockPistonMoving.FACING, BlockPistonMoving.TYPE});
    }
}
