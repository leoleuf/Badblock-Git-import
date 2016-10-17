package net.minecraft.server.v1_8_R3;

public class BlockPrismarine extends Block {

    public static final BlockStateEnum<BlockPrismarine.EnumPrismarineVariant> VARIANT = BlockStateEnum.of("variant", BlockPrismarine.EnumPrismarineVariant.class);
    public static final int b = BlockPrismarine.EnumPrismarineVariant.ROUGH.a();
    public static final int N = BlockPrismarine.EnumPrismarineVariant.BRICKS.a();
    public static final int O = BlockPrismarine.EnumPrismarineVariant.DARK.a();

    public BlockPrismarine() {
        super(Material.STONE);
        this.j(this.blockStateList.getBlockData().set(BlockPrismarine.VARIANT, BlockPrismarine.EnumPrismarineVariant.ROUGH));
        this.a(CreativeModeTab.b);
    }

    @Override
	public String getName() {
        return LocaleI18n.get(this.a() + "." + BlockPrismarine.EnumPrismarineVariant.ROUGH.c() + ".name");
    }

    @Override
	public MaterialMapColor g(IBlockData iblockdata) {
        return iblockdata.get(BlockPrismarine.VARIANT) == BlockPrismarine.EnumPrismarineVariant.ROUGH ? MaterialMapColor.y : MaterialMapColor.G;
    }

    @Override
	public int getDropData(IBlockData iblockdata) {
        return iblockdata.get(BlockPrismarine.VARIANT).a();
    }

    @Override
	public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockPrismarine.VARIANT).a();
    }

    @Override
	protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockPrismarine.VARIANT});
    }

    @Override
	public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockPrismarine.VARIANT, BlockPrismarine.EnumPrismarineVariant.a(i));
    }

    public static enum EnumPrismarineVariant implements INamable {

        ROUGH(0, "prismarine", "rough"), BRICKS(1, "prismarine_bricks", "bricks"), DARK(2, "dark_prismarine", "dark");

        private static final BlockPrismarine.EnumPrismarineVariant[] d = new BlockPrismarine.EnumPrismarineVariant[values().length];
        private final int e;
        private final String f;
        private final String g;

        private EnumPrismarineVariant(int i, String s, String s1) {
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

        public static BlockPrismarine.EnumPrismarineVariant a(int i) {
            if (i < 0 || i >= BlockPrismarine.EnumPrismarineVariant.d.length) {
                i = 0;
            }

            return BlockPrismarine.EnumPrismarineVariant.d[i];
        }

        @Override
		public String getName() {
            return this.f;
        }

        public String c() {
            return this.g;
        }

        static {
            BlockPrismarine.EnumPrismarineVariant[] ablockprismarine_enumprismarinevariant = values();
            int i = ablockprismarine_enumprismarinevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockPrismarine.EnumPrismarineVariant blockprismarine_enumprismarinevariant = ablockprismarine_enumprismarinevariant[j];

                BlockPrismarine.EnumPrismarineVariant.d[blockprismarine_enumprismarinevariant.a()] = blockprismarine_enumprismarinevariant;
            }

        }
    }
}
