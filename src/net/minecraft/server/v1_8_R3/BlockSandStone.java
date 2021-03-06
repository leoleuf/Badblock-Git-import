package net.minecraft.server.v1_8_R3;

public class BlockSandStone extends Block {

    public static final BlockStateEnum<BlockSandStone.EnumSandstoneVariant> TYPE = BlockStateEnum.of("type", BlockSandStone.EnumSandstoneVariant.class);

    public BlockSandStone() {
        super(Material.STONE);
        this.j(this.blockStateList.getBlockData().set(BlockSandStone.TYPE, BlockSandStone.EnumSandstoneVariant.DEFAULT));
        this.a(CreativeModeTab.b);
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockSandStone.TYPE).a();
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return MaterialMapColor.d;
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockSandStone.TYPE, BlockSandStone.EnumSandstoneVariant.a(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockSandStone.TYPE).a();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockSandStone.TYPE});
    }

    public static enum EnumSandstoneVariant implements INamable {

        DEFAULT(0, "sandstone", "default"), CHISELED(1, "chiseled_sandstone", "chiseled"), SMOOTH(2, "smooth_sandstone", "smooth");

        private static final BlockSandStone.EnumSandstoneVariant[] d = new BlockSandStone.EnumSandstoneVariant[values().length];
        private final int e;
        private final String f;
        private final String g;

        private EnumSandstoneVariant(int i, String s, String s1) {
            this.e = i;
            this.f = s;
            this.g = s1;
        }

        public int a() {
            return this.e;
        }

        @Override
		public String toString() {
            return this.f;
        }

        public static BlockSandStone.EnumSandstoneVariant a(int i) {
            if (i < 0 || i >= BlockSandStone.EnumSandstoneVariant.d.length) {
                i = 0;
            }

            return BlockSandStone.EnumSandstoneVariant.d[i];
        }

        @Override
		public String getName() {
            return this.f;
        }

        public String c() {
            return this.g;
        }

        static {
            BlockSandStone.EnumSandstoneVariant[] ablocksandstone_enumsandstonevariant = values();
            int i = ablocksandstone_enumsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockSandStone.EnumSandstoneVariant blocksandstone_enumsandstonevariant = ablocksandstone_enumsandstonevariant[j];

                BlockSandStone.EnumSandstoneVariant.d[blocksandstone_enumsandstonevariant.a()] = blocksandstone_enumsandstonevariant;
            }

        }
    }
}
