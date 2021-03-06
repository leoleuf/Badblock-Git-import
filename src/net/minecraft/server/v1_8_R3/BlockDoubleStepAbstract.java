package net.minecraft.server.v1_8_R3;

import java.util.Random;

public abstract class BlockDoubleStepAbstract extends BlockStepAbstract {

    public static final BlockStateBoolean SEAMLESS = BlockStateBoolean.of("seamless");
    public static final BlockStateEnum<BlockDoubleStepAbstract.EnumStoneSlabVariant> VARIANT = BlockStateEnum.of("variant", BlockDoubleStepAbstract.EnumStoneSlabVariant.class);

    public BlockDoubleStepAbstract() {
        super(Material.STONE);
        IBlockData iblockdata = this.blockStateList.getBlockData();

        if (this.l()) {
            iblockdata = iblockdata.set(BlockDoubleStepAbstract.SEAMLESS, Boolean.valueOf(false));
        } else {
            iblockdata = iblockdata.set(BlockStepAbstract.HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
        }

        this.j(iblockdata.set(BlockDoubleStepAbstract.VARIANT, BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE));
        this.a(CreativeModeTab.b);
    }

    @Override
	public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return Item.getItemOf(Blocks.STONE_SLAB);
    }

    @Override
	public String b(int i) {
        return super.a() + "." + BlockDoubleStepAbstract.EnumStoneSlabVariant.a(i).d();
    }

    @Override
	public IBlockState<?> n() {
        return BlockDoubleStepAbstract.VARIANT;
    }

    @Override
	public Object a(ItemStack itemstack) {
        return BlockDoubleStepAbstract.EnumStoneSlabVariant.a(itemstack.getData() & 7);
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        IBlockData iblockdata = this.getBlockData().set(BlockDoubleStepAbstract.VARIANT, BlockDoubleStepAbstract.EnumStoneSlabVariant.a(i & 7));

        if (this.l()) {
            iblockdata = iblockdata.set(BlockDoubleStepAbstract.SEAMLESS, Boolean.valueOf((i & 8) != 0));
        } else {
            iblockdata = iblockdata.set(BlockStepAbstract.HALF, (i & 8) == 0 ? BlockStepAbstract.EnumSlabHalf.BOTTOM : BlockStepAbstract.EnumSlabHalf.TOP);
        }

        return iblockdata;
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockDoubleStepAbstract.VARIANT).a();

        if (this.l()) {
            if (iblockdata.get(BlockDoubleStepAbstract.SEAMLESS).booleanValue()) {
                i |= 8;
            }
        } else if (iblockdata.get(BlockStepAbstract.HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    @Override
	protected BlockStateList getStateList() {
        return this.l() ? new BlockStateList(this, new IBlockState[] { BlockDoubleStepAbstract.SEAMLESS, BlockDoubleStepAbstract.VARIANT}) : new BlockStateList(this, new IBlockState[] { BlockStepAbstract.HALF, BlockDoubleStepAbstract.VARIANT});
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockDoubleStepAbstract.VARIANT).a();
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockDoubleStepAbstract.VARIANT).c();
    }

    public static enum EnumStoneSlabVariant implements INamable {

        STONE(0, MaterialMapColor.m, "stone"), SAND(1, MaterialMapColor.d, "sandstone", "sand"), WOOD(2, MaterialMapColor.o, "wood_old", "wood"), COBBLESTONE(3, MaterialMapColor.m, "cobblestone", "cobble"), BRICK(4, MaterialMapColor.D, "brick"), SMOOTHBRICK(5, MaterialMapColor.m, "stone_brick", "smoothStoneBrick"), NETHERBRICK(6, MaterialMapColor.K, "nether_brick", "netherBrick"), QUARTZ(7, MaterialMapColor.p, "quartz");

        private static final BlockDoubleStepAbstract.EnumStoneSlabVariant[] i = new BlockDoubleStepAbstract.EnumStoneSlabVariant[values().length];
        private final int j;
        private final MaterialMapColor k;
        private final String l;
        private final String m;

        private EnumStoneSlabVariant(int i, MaterialMapColor materialmapcolor, String s) {
            this(i, materialmapcolor, s, s);
        }

        private EnumStoneSlabVariant(int i, MaterialMapColor materialmapcolor, String s, String s1) {
            this.j = i;
            this.k = materialmapcolor;
            this.l = s;
            this.m = s1;
        }

        public int a() {
            return this.j;
        }

        public MaterialMapColor c() {
            return this.k;
        }

        @Override
		public String toString() {
            return this.l;
        }

        public static BlockDoubleStepAbstract.EnumStoneSlabVariant a(int i) {
            if (i < 0 || i >= BlockDoubleStepAbstract.EnumStoneSlabVariant.i.length) {
                i = 0;
            }

            return BlockDoubleStepAbstract.EnumStoneSlabVariant.i[i];
        }

        @Override
		public String getName() {
            return this.l;
        }

        public String d() {
            return this.m;
        }

        static {
            BlockDoubleStepAbstract.EnumStoneSlabVariant[] ablockdoublestepabstract_enumstoneslabvariant = values();
            int i = ablockdoublestepabstract_enumstoneslabvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockDoubleStepAbstract.EnumStoneSlabVariant blockdoublestepabstract_enumstoneslabvariant = ablockdoublestepabstract_enumstoneslabvariant[j];

                BlockDoubleStepAbstract.EnumStoneSlabVariant.i[blockdoublestepabstract_enumstoneslabvariant.a()] = blockdoublestepabstract_enumstoneslabvariant;
            }

        }
    }
}
