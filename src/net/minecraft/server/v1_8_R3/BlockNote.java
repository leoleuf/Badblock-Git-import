package net.minecraft.server.v1_8_R3;

import java.util.List;

import com.google.common.collect.Lists;

public class BlockNote extends BlockContainer {

    private static final List<String> a = Lists.newArrayList(new String[] { "harp", "bd", "snare", "hat", "bassattack"});

    public BlockNote() {
        super(Material.WOOD);
        this.a(CreativeModeTab.d);
    }

    @Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
        boolean flag = world.isBlockIndirectlyPowered(blockposition);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityNote) {
            TileEntityNote tileentitynote = (TileEntityNote) tileentity;

            if (tileentitynote.f != flag) {
                if (flag) {
                    tileentitynote.play(world, blockposition);
                }

                tileentitynote.f = flag;
            }
        }

    }

    @Override
	public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumDirection enumdirection, float f, float f1, float f2) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityNote) {
                TileEntityNote tileentitynote = (TileEntityNote) tileentity;

                tileentitynote.b();
                tileentitynote.play(world, blockposition);
                entityhuman.b(StatisticList.S);
            }

            return true;
        }
    }

    @Override
	public void attack(World world, BlockPosition blockposition, EntityHuman entityhuman) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityNote) {
                ((TileEntityNote) tileentity).play(world, blockposition);
                entityhuman.b(StatisticList.R);
            }

        }
    }

    @Override
	public TileEntity a(World world, int i) {
        return new TileEntityNote();
    }

    private String b(int i) {
        if (i < 0 || i >= BlockNote.a.size()) {
            i = 0;
        }

        return BlockNote.a.get(i);
    }

    @Override
	public boolean a(World world, BlockPosition blockposition, IBlockData iblockdata, int i, int j) {
        float f = (float) Math.pow(2.0D, (j - 12) / 12.0D);

        world.makeSound(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D, "note." + this.b(i), 3.0F, f);
        world.addParticle(EnumParticle.NOTE, blockposition.getX() + 0.5D, blockposition.getY() + 1.2D, blockposition.getZ() + 0.5D, j / 24.0D, 0.0D, 0.0D, new int[0]);
        return true;
    }

    @Override
	public int b() {
        return 3;
    }
}
