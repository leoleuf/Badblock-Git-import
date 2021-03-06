package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public interface IMonster extends IAnimal {

    Predicate<Entity> d = new Predicate() {
        public boolean a(Entity entity) {
            return entity instanceof IMonster;
        }

        @Override
		public boolean apply(Object object) {
            return this.a((Entity) object);
        }
    };
    Predicate<Entity> e = new Predicate() {
        public boolean a(Entity entity) {
            return entity instanceof IMonster && !entity.isInvisible();
        }

        @Override
		public boolean apply(Object object) {
            return this.a((Entity) object);
        }
    };
}
