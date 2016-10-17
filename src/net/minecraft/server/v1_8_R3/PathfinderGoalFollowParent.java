package net.minecraft.server.v1_8_R3;

import java.util.Iterator;
import java.util.List;

public class PathfinderGoalFollowParent extends PathfinderGoal {

    EntityAnimal a;
    EntityAnimal b;
    double c;
    private int d;

    public PathfinderGoalFollowParent(EntityAnimal entityanimal, double d0) {
        this.a = entityanimal;
        this.c = d0;
    }

    @Override
	public boolean a() {
        if (this.a.getAge() >= 0) {
            return false;
        } else {
            List list = this.a.world.a(this.a.getClass(), this.a.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
            EntityAnimal entityanimal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityAnimal entityanimal1 = (EntityAnimal) iterator.next();

                if (entityanimal1.getAge() >= 0) {
                    double d1 = this.a.h(entityanimal1);

                    if (d1 <= d0) {
                        d0 = d1;
                        entityanimal = entityanimal1;
                    }
                }
            }

            if (entityanimal == null) {
                return false;
            } else if (d0 < 9.0D) {
                return false;
            } else {
                this.b = entityanimal;
                return true;
            }
        }
    }

    @Override
	public boolean b() {
        if (this.a.getAge() >= 0) {
            return false;
        } else if (!this.b.isAlive()) {
            return false;
        } else {
            double d0 = this.a.h(this.b);

            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }

    @Override
	public void c() {
        this.d = 0;
    }

    @Override
	public void d() {
        this.b = null;
    }

    @Override
	public void e() {
        if (--this.d <= 0) {
            this.d = 10;
            this.a.getNavigation().a(this.b, this.c);
        }
    }
}
