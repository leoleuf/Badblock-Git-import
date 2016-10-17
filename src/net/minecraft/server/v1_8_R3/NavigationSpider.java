package net.minecraft.server.v1_8_R3;

public class NavigationSpider extends Navigation {

    private BlockPosition f;

    public NavigationSpider(EntityInsentient entityinsentient, World world) {
        super(entityinsentient, world);
    }

    @Override
	public PathEntity a(BlockPosition blockposition) {
        this.f = blockposition;
        return super.a(blockposition);
    }

    @Override
	public PathEntity a(Entity entity) {
        this.f = new BlockPosition(entity);
        return super.a(entity);
    }

    @Override
	public boolean a(Entity entity, double d0) {
        PathEntity pathentity = this.a(entity);

        if (pathentity != null) {
            return this.a(pathentity, d0);
        } else {
            this.f = new BlockPosition(entity);
            this.e = d0;
            return true;
        }
    }

    @Override
	public void k() {
        if (!this.m()) {
            super.k();
        } else {
            if (this.f != null) {
                double d0 = this.b.width * this.b.width;

                if (this.b.c(this.f) >= d0 && (this.b.locY <= this.f.getY() || this.b.c(new BlockPosition(this.f.getX(), MathHelper.floor(this.b.locY), this.f.getZ())) >= d0)) {
                    this.b.getControllerMove().a(this.f.getX(), this.f.getY(), this.f.getZ(), this.e);
                } else {
                    this.f = null;
                }
            }

        }
    }
}
