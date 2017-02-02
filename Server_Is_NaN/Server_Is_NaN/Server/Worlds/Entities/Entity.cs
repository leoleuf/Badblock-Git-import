using Newtonsoft.Json.Linq;
using Server_Is_NaN.Server.Worlds;

namespace Server_Is_NaN.Server.World.Entities
{
    public abstract class Entity
    {
        private static int entityId = 0;

        public int Id { get; private set; }
        public bool Removed { get; protected set; }
        public Location Position { get; set; } //FIXME update (ticking ?)

        public Entity()
        {
            this.Id = entityId++;
        }

        public abstract void tick();

        public abstract JObject ToJson();
    }
}
