package net.minecraft.server.v1_8_R3;

public class BlockSmoothBrick extends Block {

    public static final BlockStateEnum<BlockSmoothBrick.EnumStonebrickType> VARIANT = BlockStateEnum.of("variant", BlockSmoothBrick.EnumStonebrickType.class);
    public static final int b = BlockSmoothBrick.EnumStonebrickType.DEFAULT.a();
    public static final int N = BlockSmoothBrick.EnumStonebrickType.MOSSY.a();
    public static final int O = BlockSmoothBrick.EnumStonebrickType.CRACKED.a();
    public static final int P = BlockSmoothBrick.EnumStonebrickType.CHISELED.a();

    public BlockSmoothBrick() {
        super(Material.STONE);
        this.j(this.blockStateList.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.DEFAULT));
        this.a(CreativeModeTab.b);
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockSmoothBrick.VARIANT).a();
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.a(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockSmoothBrick.VARIANT).a();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockSmoothBrick.VARIANT});
    }

    public static enum EnumStonebrickType implements INamable {

        DEFAULT(0, "stonebrick", "default"), MOSSY(1, "mossy_stonebrick", "mossy"), CRACKED(2, "cracked_stonebrick", "cracked"), CHISELED(3, "chiseled_stonebrick", "chiseled");

        private static final BlockSmoothBrick.EnumStonebrickType[] e = new BlockSmoothBrick.EnumStonebrickType[values().length];
        private final int f;
        private final String g;
        private final String h;

        private EnumStonebrickType(int i, String s, String s1) {
            this.f = i;
            this.g = s;
            this.h = s1;
        }

        public int a() {
            return this.f;
        }

        @Override
		public String toString() {
            return this.g;
        }

        public static BlockSmoothBrick.EnumStonebrickType a(int i) {
            if (i < 0 || i >= BlockSmoothBrick.EnumStonebrickType.e.length) {
                i = 0;
            }

            return BlockSmoothBrick.EnumStonebrickType.e[i];
        }

        @Override
		public String getName() {
            return this.g;
        }

        public String c() {
            return this.h;
        }

        static {
            BlockSmoothBrick.EnumStonebrickType[] ablocksmoothbrick_enumstonebricktype = values();
            int i = ablocksmoothbrick_enumstonebricktype.length;

            for (int j = 0; j < i; ++j) {
                BlockSmoothBrick.EnumStonebrickType blocksmoothbrick_enumstonebricktype = ablocksmoothbrick_enumstonebricktype[j];

                BlockSmoothBrick.EnumStonebrickType.e[blocksmoothbrick_enumstonebricktype.a()] = blocksmoothbrick_enumstonebricktype;
            }

        }
    }
}
