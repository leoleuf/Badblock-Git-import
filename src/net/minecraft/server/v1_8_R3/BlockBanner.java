package net.minecraft.server.v1_8_R3;

import java.util.Random;

import com.google.common.base.Predicate;

public class BlockBanner extends BlockContainer {

    public static final BlockStateDirection FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
    public static final BlockStateInteger ROTATION = BlockStateInteger.of("rotation", 0, 15);

    protected BlockBanner() {
        super(Material.WOOD);
        float f = 0.25F;
        float f1 = 1.0F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    @Override
	public String getName() {
        return LocaleI18n.get("item.banner.white.name");
    }

    @Override
	public AxisAlignedBB a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return null;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return true;
    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public boolean g() {
        return true;
    }

    @Override
	public TileEntity a(World world, int i) {
        return new TileEntityBanner();
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Items.BANNER;
    }

    @Override
	public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityBanner) {
            ItemStack itemstack = new ItemStack(Items.BANNER, 1, ((TileEntityBanner) tileentity).b());
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            tileentity.b(nbttagcompound);
            nbttagcompound.remove("x");
            nbttagcompound.remove("y");
            nbttagcompound.remove("z");
            nbttagcompound.remove("id");
            itemstack.a("BlockEntityTag", nbttagcompound);
            a(world, blockposition, itemstack);
        } else {
            super.dropNaturally(world, blockposition, iblockdata, f, i);
        }

    }

    @Override
	public boolean canPlace(World world, BlockPosition blockposition) {
        return !this.e(world, blockposition) && super.canPlace(world, blockposition);
    }

    @Override
	public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, TileEntity tileentity) {
        if (tileentity instanceof TileEntityBanner) {
            TileEntityBanner tileentitybanner = (TileEntityBanner) tileentity;
            ItemStack itemstack = new ItemStack(Items.BANNER, 1, ((TileEntityBanner) tileentity).b());
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            TileEntityBanner.a(nbttagcompound, tileentitybanner.b(), tileentitybanner.d());
            itemstack.a("BlockEntityTag", nbttagcompound);
            a(world, blockposition, itemstack);
        } else {
            super.a(world, entityhuman, blockposition, iblockdata, (TileEntity) null);
        }

    }

    static class SyntheticClass_1 {

        static final int[] a = new int[EnumDirection.values().length];

        static {
            try {
                BlockBanner.SyntheticClass_1.a[EnumDirection.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                BlockBanner.SyntheticClass_1.a[EnumDirection.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                BlockBanner.SyntheticClass_1.a[EnumDirection.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                BlockBanner.SyntheticClass_1.a[EnumDirection.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

        }
    }

    public static class BlockStandingBanner extends BlockBanner {

        public BlockStandingBanner() {
            this.j(this.blockStateList.getBlockData().set(BlockBanner.ROTATION, Integer.valueOf(0)));
        }

        @Override
		public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
            if (!world.getType(blockposition.down()).getBlock().getMaterial().isBuildable()) {
                this.b(world, blockposition, iblockdata, 0);
                world.setAir(blockposition);
            }

            super.doPhysics(world, blockposition, iblockdata, block);
        }

        @Override
		public IBlockData fromLegacyData(int i) {
            return this.getBlockData().set(BlockBanner.ROTATION, Integer.valueOf(i));
        }

        @Override
		public int toLegacyData(IBlockData iblockdata) {
            return iblockdata.get(BlockBanner.ROTATION).intValue();
        }

        @Override
		protected BlockStateList getStateList() {
            return new BlockStateList(this, new IBlockState[] { BlockBanner.ROTATION});
        }
    }

    public static class BlockWallBanner extends BlockBanner {

        public BlockWallBanner() {
            this.j(this.blockStateList.getBlockData().set(BlockBanner.FACING, EnumDirection.NORTH));
        }

        @Override
		public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
            EnumDirection enumdirection = iblockaccess.getType(blockposition).get(BlockBanner.FACING);
            float f = 0.0F;
            float f1 = 0.78125F;
            float f2 = 0.0F;
            float f3 = 1.0F;
            float f4 = 0.125F;

            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            switch (BlockBanner.SyntheticClass_1.a[enumdirection.ordinal()]) {
            case 1:
            default:
                this.a(f2, f, 1.0F - f4, f3, f1, 1.0F);
                break;

            case 2:
                this.a(f2, f, 0.0F, f3, f1, f4);
                break;

            case 3:
                this.a(1.0F - f4, f, f2, 1.0F, f1, f3);
                break;

            case 4:
                this.a(0.0F, f, f2, f4, f1, f3);
            }

        }

        @Override
		public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
            EnumDirection enumdirection = iblockdata.get(BlockBanner.FACING);

            if (!world.getType(blockposition.shift(enumdirection.opposite())).getBlock().getMaterial().isBuildable()) {
                this.b(world, blockposition, iblockdata, 0);
                world.setAir(blockposition);
            }

            super.doPhysics(world, blockposition, iblockdata, block);
        }

        @Override
		public IBlockData fromLegacyData(int i) {
            EnumDirection enumdirection = EnumDirection.fromType1(i);

            if (enumdirection.k() == EnumDirection.EnumAxis.Y) {
                enumdirection = EnumDirection.NORTH;
            }

            return this.getBlockData().set(BlockBanner.FACING, enumdirection);
        }

        @Override
		public int toLegacyData(IBlockData iblockdata) {
            return iblockdata.get(BlockBanner.FACING).a();
        }

        @Override
		protected BlockStateList getStateList() {
            return new BlockStateList(this, new IBlockState[] { BlockBanner.FACING});
        }
    }
}
