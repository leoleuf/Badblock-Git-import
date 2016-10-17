package net.minecraft.server.v1_8_R3;

import java.util.List;

import com.google.common.collect.Lists;

public class EntitySenses {

    EntityInsentient a;
    List<Entity> b = Lists.newArrayList();
    List<Entity> c = Lists.newArrayList();

    public EntitySenses(EntityInsentient entityinsentient) {
        this.a = entityinsentient;
    }

    public void a() {
        this.b.clear();
        this.c.clear();
    }

    public boolean a(Entity entity) {
        if (this.b.contains(entity)) {
            return true;
        } else if (this.c.contains(entity)) {
            return false;
        } else {
            this.a.world.methodProfiler.a("canSee");
            boolean flag = this.a.hasLineOfSight(entity);

            this.a.world.methodProfiler.b();
            if (flag) {
                this.b.add(entity);
            } else {
                this.c.add(entity);
            }

            return flag;
        }
    }
}
