using System.Collections.Generic;

using Server_Is_NaN.Utils;

using Server_Is_NaN.Server.Worlds.Entities;
using Server_Is_NaN.Server.World;

namespace Server_Is_NaN.Server.Worlds.Providers
{
    public class PlayerChunkMap
    {
        const int viewDistance = 5; //TODO configurable

        private SWorld world;

        private List<Player> managedPlayers = new List<Player>();
        private Dictionary<SimpleCoord, PlayerChunk> chunks = new Dictionary<SimpleCoord, PlayerChunk>();
        
        public PlayerChunkMap(SWorld world)
        {
            this.world = world;
        }

        private PlayerChunk GetPlayerChunk(int x, int z)
        {
            SimpleCoord coords = new SimpleCoord(x, z);

            if (!chunks.ContainsKey(coords))
                chunks.Add(coords, new PlayerChunk(coords));  

            return chunks[coords];
        }

        private void ShowChunk(Player player, int x, int z)
        {
            GetPlayerChunk(x, z).AddPlayer(player);
        }

        public void MovePlayer(Player player)
        {
            MovePlayer(player, false);
        }

        public void MovePlayer(Player player, bool force)
        {
            double dx = player.Position.x - player.lastUpdatedX;
            double dz = player.Position.z - player.lastUpdatedZ;

            if (dx * dx + dz * dz < 64.0d && !force) // if distance of last updated and current player positions is minor than an half chunk
                return;

            int chunkx = (int) player.Position.x >> 4;
            int chunkz = (int) player.Position.z >> 4;

            SimpleCoord chunkC = new SimpleCoord(chunkx, chunkz);
            List<PlayerChunk> toLoad = new List<PlayerChunk>();

            for (int x = chunkx - viewDistance; x <= chunkx + viewDistance; x++)
            {
                for (int z = chunkz - viewDistance; z <= chunkz + viewDistance; z++)
                {
                    toLoad.Add(GetPlayerChunk(x, z));    
                }
            }

            for (int i = 0; i < player.chunks.Count; i++)
            {
                SimpleCoord coords = player.chunks[i];
                // If the player already view the chunk, remove it from list of load.
                if (toLoad.RemoveAll(p => p.coords == coords) == 0)
                {
                    // Remove the chunk if the player don't is too far
                    player.chunks.RemoveAt(i);
                    GetPlayerChunk(coords.x, coords.z).RemovePlayer(player);
                    i--;
                }
            }

            toLoad.Sort((v1, v2) => CompareChunk(v1, v2, chunkC)); // Sort chunks (the nearest is the first)

            foreach (PlayerChunk chunk in toLoad) // show chunk in the right order
                chunk.AddPlayer(player);

            player.lastUpdatedX = player.Position.x;
            player.lastUpdatedZ = player.Position.z;
        }

        public void Update()
        {
            foreach (Player p in managedPlayers)
                MovePlayer(p);
        }

        public void AddPlayer(Player p)
        {
            managedPlayers.Add(p);
            MovePlayer(p, true);
        }

        public void RemovePlayer(Player p)
        {
            foreach (SimpleCoord c in p.chunks)
                GetPlayerChunk(c.x, c.z).RemovePlayer(p);

            p.chunks.Clear();
            managedPlayers.Remove(p);
        }

        private int CompareChunk(PlayerChunk v1, PlayerChunk v2, SimpleCoord point)
        {
            int d1 = v1.coords.DistanceSquared(point);
            int d2 = v2.coords.DistanceSquared(point);

            return d2 - d1;
        }

        class PlayerChunk
        {
            public SimpleCoord coords { get; }
            private List<Player> players = new List<Player>();
            private bool loaded = true;

            public PlayerChunk(SimpleCoord coords)
            {
                this.coords = coords;
                //TODO load
            }

            public void AddPlayer(Player player)
            {
                if (HasPlayer(player))
                    return;

                players.Add(player);
                
                if (loaded)
                    player.ShowChunk(coords);
            }

            public void RemovePlayer(Player player)
            {
                if (!HasPlayer(player))
                    return;

                players.Remove(player);

                if (!loaded && players.Count == 0)
                {
                    //TODO cancel load
                }
            }

            public bool HasPlayer(Player player)
            {
                return players.Contains(player);
            }

            private void LoadCallback()
            {
                loaded = true;

                foreach (Player player in players)
                    player.ShowChunk(coords);
            }
        }
    }
}
