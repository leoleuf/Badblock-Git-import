package net.minecraft.server.v1_8_R3;

public class BlockRedSandstone extends Block {

    public static final BlockStateEnum<BlockRedSandstone.EnumRedSandstoneVariant> TYPE = BlockStateEnum.of("type", BlockRedSandstone.EnumRedSandstoneVariant.class);

    public BlockRedSandstone() {
        super(Material.STONE, BlockSand.EnumSandVariant.RED_SAND.c());
        this.j(this.blockStateList.getBlockData().set(BlockRedSandstone.TYPE, BlockRedSandstone.EnumRedSandstoneVariant.DEFAULT));
        this.a(CreativeModeTab.b);
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockRedSandstone.TYPE).a();
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockRedSandstone.TYPE, BlockRedSandstone.EnumRedSandstoneVariant.a(i));
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockRedSandstone.TYPE).a();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockRedSandstone.TYPE});
    }

    public static enum EnumRedSandstoneVariant implements INamable {

        DEFAULT(0, "red_sandstone", "default"), CHISELED(1, "chiseled_red_sandstone", "chiseled"), SMOOTH(2, "smooth_red_sandstone", "smooth");

        private static final BlockRedSandstone.EnumRedSandstoneVariant[] d = new BlockRedSandstone.EnumRedSandstoneVariant[values().length];
        private final int e;
        private final String f;
        private final String g;

        private EnumRedSandstoneVariant(int i, String s, String s1) {
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

        public static BlockRedSandstone.EnumRedSandstoneVariant a(int i) {
            if (i < 0 || i >= BlockRedSandstone.EnumRedSandstoneVariant.d.length) {
                i = 0;
            }

            return BlockRedSandstone.EnumRedSandstoneVariant.d[i];
        }

        @Override
		public String getName() {
            return this.f;
        }

        public String c() {
            return this.g;
        }

        static {
            BlockRedSandstone.EnumRedSandstoneVariant[] ablockredsandstone_enumredsandstonevariant = values();
            int i = ablockredsandstone_enumredsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockRedSandstone.EnumRedSandstoneVariant blockredsandstone_enumredsandstonevariant = ablockredsandstone_enumredsandstonevariant[j];

                BlockRedSandstone.EnumRedSandstoneVariant.d[blockredsandstone_enumredsandstonevariant.a()] = blockredsandstone_enumredsandstonevariant;
            }

        }
    }
}
