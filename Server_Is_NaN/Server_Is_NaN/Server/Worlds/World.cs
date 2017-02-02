﻿using System.Collections.Generic;
using System;

using Newtonsoft.Json;

using Server_Is_NaN.Server.World.Entities;
using Server_Is_NaN.Server.World.Blocks;

namespace Server_Is_NaN.Server.World
{
    [JsonConverter(typeof(WorldSerializer))]
    public class SWorld
    {
        public static readonly int WORLD_HEIGHT = 256;

        public Dimension Dimension { get; private set; }
        public string Name { get; private set; } = "todo"; //FIXME
        
        private List<Entity> entities = new List<Entity>();
        private readonly IChunkProvider provider = null;
        
        public Chunk GetChunk(int x, int z)
        {
            return provider.GetChunk(x, z);
        }

        public Chunk GetChunkAt(int x, int z)
        {
            return provider.GetChunk(x >> 4, z >> 4);
        }

        public Block GetBlockAt(int x, int y, int z)
        {
            if (y < 0 || y >= WORLD_HEIGHT)
            {
                return Block.AIR;
            }
            
            return GetChunkAt(x, z).GetBlockAt(x % 16, y, z % 16);
        }

        public void AddEntity(Entity e)
        {
            entities.Add(e); //FIXME
        }

        public void tick()
        {
            // Tick entities
            for (int i = 0; i < entities.Count; i++)
            {
                Entity ent = entities[i];

                ent.tick();

                if (ent.Removed)
                {
                    //FIXME Envoyer aux joueurs la suppression

                    Console.WriteLine("Une de moins, une!");
                    entities.RemoveAt(i);
                    i--;
                }
            }
        }
        /*
         * TODO:
         *  => SetBlockAt (and update)
         */
    }

    public class WorldSerializer : JsonConverter
    {
        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            SWorld world = value as SWorld;

            serializer.Serialize(writer, world.Name);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            return Server.Instance.GetWorld(reader.ReadAsString());
        }

        public override bool CanConvert(Type objectType)
        {
            return typeof(SWorld).IsAssignableFrom(objectType);
        }
    }
}
