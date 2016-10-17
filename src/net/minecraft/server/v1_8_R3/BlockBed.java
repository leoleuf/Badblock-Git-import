package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.Random;

public class BlockBed extends BlockDirectional {

    public static final BlockStateEnum<BlockBed.EnumBedPart> PART = BlockStateEnum.of("part", BlockBed.EnumBedPart.class);
    public static final BlockStateBoolean OCCUPIED = BlockStateBoolean.of("occupied");

    public BlockBed() {
        super(Material.CLOTH);
        this.j(this.blockStateList.getBlockData().set(BlockBed.PART, BlockBed.EnumBedPart.FOOT).set(BlockBed.OCCUPIED, Boolean.valueOf(false)));
        this.l();
    }

    @Override
	public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (world.isClientSide) {
            return true;
        } else {
            if (iblockdata.get(BlockBed.PART) != BlockBed.EnumBedPart.HEAD) {
                blockposition = blockposition.shift(iblockdata.get(BlockDirectional.FACING));
                iblockdata = world.getType(blockposition);
                if (iblockdata.getBlock() != this) {
                    return true;
                }
            }

            if (world.worldProvider.e() && world.getBiome(blockposition) != BiomeBase.HELL) {
                if (iblockdata.get(BlockBed.OCCUPIED).booleanValue()) {
                    EntityHuman entityhuman1 = this.f(world, blockposition);

                    if (entityhuman1 != null) {
                        entityhuman.b((new ChatMessage("tile.bed.occupied", new Object[0])));
                        return true;
                    }

                    iblockdata = iblockdata.set(BlockBed.OCCUPIED, Boolean.valueOf(false));
                    world.setTypeAndData(blockposition, iblockdata, 4);
                }

                EntityHuman.EnumBedResult entityhuman_enumbedresult = entityhuman.a(blockposition);

                if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.OK) {
                    iblockdata = iblockdata.set(BlockBed.OCCUPIED, Boolean.valueOf(true));
                    world.setTypeAndData(blockposition, iblockdata, 4);
                    return true;
                } else {
                    if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW) {
                        entityhuman.b((new ChatMessage("tile.bed.noSleep", new Object[0])));
                    } else if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.NOT_SAFE) {
                        entityhuman.b((new ChatMessage("tile.bed.notSafe", new Object[0])));
                    }

                    return true;
                }
            } else {
                world.setAir(blockposition);
                BlockPosition blockposition1 = blockposition.shift(iblockdata.get(BlockDirectional.FACING).opposite());

                if (world.getType(blockposition1).getBlock() == this) {
                    world.setAir(blockposition1);
                }

                world.createExplosion((Entity) null, blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    private EntityHuman f(World world, BlockPosition blockposition) {
        Iterator iterator = world.players.iterator();

        EntityHuman entityhuman;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityhuman = (EntityHuman) iterator.next();
        } while (!entityhuman.isSleeping() || !entityhuman.bx.equals(blockposition));

        return entityhuman;
    }

    @Override
	public boolean d() {
        return false;
    }

    @Override
	public boolean c() {
        return false;
    }

    @Override
	public void updateShape(IBlockAccess iblockaccess, BlockPosition blockposition) {
        this.l();
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        EnumDirection enumdirection = iblockdata.get(BlockDirectional.FACING);

        if (iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.HEAD) {
            if (world.getType(blockposition.shift(enumdirection.opposite())).getBlock() != this) {
                world.setAir(blockposition);
            }
        } else if (world.getType(blockposition.shift(enumdirection)).getBlock() != this) {
            world.setAir(blockposition);
            if (!world.isClientSide) {
                this.b(world, blockposition, iblockdata, 0);
            }
        }

    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.HEAD ? null : Items.BED;
    }

    private void l() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    public static BlockPosition a(World world, BlockPosition blockposition, int i) {
        EnumDirection enumdirection = world.getType(blockposition).get(BlockDirectional.FACING);
        int j = blockposition.getX();
        int k = blockposition.getY();
        int l = blockposition.getZ();

        for (int i1 = 0; i1 <= 1; ++i1) {
            int j1 = j - enumdirection.getAdjacentX() * i1 - 1;
            int k1 = l - enumdirection.getAdjacentZ() * i1 - 1;
            int l1 = j1 + 2;
            int i2 = k1 + 2;

            for (int j2 = j1; j2 <= l1; ++j2) {
                for (int k2 = k1; k2 <= i2; ++k2) {
                    BlockPosition blockposition1 = new BlockPosition(j2, k, k2);

                    if (e(world, blockposition1)) {
                        if (i <= 0) {
                            return blockposition1;
                        }

                        --i;
                    }
                }
            }
        }

        return null;
    }

    protected static boolean e(World world, BlockPosition blockposition) {
        return World.a(world, blockposition.down()) && !world.getType(blockposition).getBlock().getMaterial().isBuildable() && !world.getType(blockposition.up()).getBlock().getMaterial().isBuildable();
    }

    @Override
	public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        if (iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.FOOT) {
            super.dropNaturally(world, blockposition, iblockdata, f, 0);
        }

    }

    @Override
	public int k() {
        return 1;
    }

    @Override
	public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        if (entityhuman.abilities.canInstantlyBuild && iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.HEAD) {
            BlockPosition blockposition1 = blockposition.shift(iblockdata.get(BlockDirectional.FACING).opposite());

            if (world.getType(blockposition1).getBlock() == this) {
                world.setAir(blockposition1);
            }
        }

    }

    @Override
	public IBlockData fromLegacyData(int i) {
        EnumDirection enumdirection = EnumDirection.fromType2(i);

        return (i & 8) > 0 ? this.getBlockData().set(BlockBed.PART, BlockBed.EnumBedPart.HEAD).set(BlockDirectional.FACING, enumdirection).set(BlockBed.OCCUPIED, Boolean.valueOf((i & 4) > 0)) : this.getBlockData().set(BlockBed.PART, BlockBed.EnumBedPart.FOOT).set(BlockDirectional.FACING, enumdirection);
    }

    @Override
	public IBlockData updateState(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        if (iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.FOOT) {
            IBlockData iblockdata1 = iblockaccess.getType(blockposition.shift(iblockdata.get(BlockDirectional.FACING)));

            if (iblockdata1.getBlock() == this) {
                iblockdata = iblockdata.set(BlockBed.OCCUPIED, iblockdata1.get(BlockBed.OCCUPIED));
            }
        }

        return iblockdata;
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockDirectional.FACING).b();

        if (iblockdata.get(BlockBed.PART) == BlockBed.EnumBedPart.HEAD) {
            i |= 8;
            if (iblockdata.get(BlockBed.OCCUPIED).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockDirectional.FACING, BlockBed.PART, BlockBed.OCCUPIED});
    }

    public static enum EnumBedPart implements INamable {

        HEAD("head"), FOOT("foot");

        private final String c;

        private EnumBedPart(String s) {
            this.c = s;
        }

        @Override
		public String toString() {
            return this.c;
        }

        @Override
		public String getName() {
            return this.c;
        }
    }
}
