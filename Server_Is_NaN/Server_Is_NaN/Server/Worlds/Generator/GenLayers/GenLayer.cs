namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public abstract class GenLayer
    {
        public static GenLayer CreateLayer(long seed)
        {
            GenLayer lay = new GenLayerIsland(null, 101L);

            lay = new GenLayerZoomFuzzy(lay, 38127L);
            lay = new GenLayerZoomFuzzy(lay, 38127L);

            lay = new GenLayerAddIsland(lay, 3919L);
            lay = new GenLayerZoom(lay, 38127L);

            lay = new GenLayerAddIsland(lay, 39319L);
            lay = new GenLayerAddIsland(lay, 399L);
            lay = new GenLayerAddIsland(lay, 63119L);
            lay = new GenLayerRemoveTooMuchOcean(lay, 3821L);

            lay = new GenLayerSmooth(lay, 3982L);

            lay = new GenLayerBiomeGroup(lay, 381L);
            lay = new GenLayerZoom(lay, 38127L);

            GenLayer alter = new GenLayerCleaner(lay, 9272L);
            alter = new GenLayerZoom(alter, 812L);
            alter = new GenLayerZoom(alter, 898L);

            lay = new GenLayerBiome(lay, 38138L);
            lay = new GenLayerDeepOcean(lay, 382L);
            lay = new GenLayerZoom(lay, 38131L);
            //lay = new GenLayerZoom(lay, 38132L);

            lay = new GenLayerZoom(lay, 38131L);
            lay = new GenLayerAddIsland(lay, 9833L);

            lay = new GenLayerSmooth(lay, 3881L);
            lay = new GenLayerSmooth(lay, 3983L);
            

            for (int i = 0; i < 5; i++)
            {
                lay = new GenLayerZoom(lay, i * 37L);

                if (i == 0)
                {
                    lay = new GenLayerAddIsland(lay, i * 3913L);
                    lay = new GenLayerBiomeHills(lay, alter, 38133L);
                }
                if (i == 1)
                {
                    lay = new GenLayerBiomeEdge(lay, 28138L);
                }
            }

            lay = new GenLayerSmooth(lay, 3981L);

            lay.InitWorldSeed(seed);
            lay.InitGenInfo(WorldGenInfo.Earth());

            return lay;
        }

        private static readonly long mult = 6364136223846793005L;
        private static readonly long add = 1442695040888963407L;

        private readonly long worldSeed;
        private long worldGenSeed, chunkSeed;

        protected WorldGenInfo genInfo { get; private set; }
        protected GenLayer parent { get; }

        public GenLayer(GenLayer parent, long seed)
        {
            this.parent = parent;
            this.worldSeed = seed;
        }

        public void InitWorldSeed(long seed)
        {
            if (parent != null)
                parent.InitWorldSeed(seed);

            this.worldGenSeed = seed;

            for (int i = 0; i < 3; i++)
            {
                this.worldGenSeed *= worldGenSeed * mult + add;
                this.worldGenSeed += worldSeed;
            }
        }

        public void InitGenInfo(WorldGenInfo genInfo)
        {
            if (parent != null)
                parent.InitGenInfo(genInfo);

            this.genInfo = genInfo;
        }

        public void InitLocalSeed(long s1, long s2)
        {
            this.chunkSeed = this.worldGenSeed;

            for (int i = 0; i < 2; i++)
            {
                this.chunkSeed *= chunkSeed * mult + add;
                this.chunkSeed += s1;
                this.chunkSeed *= chunkSeed * mult + add;
                this.chunkSeed += s2;
            }
        }

        public int NextInt(int max)
        {
            long result = ((this.chunkSeed >> 24) % (long)max);

            if (result < 0)
                result += max;

            /* Update seed */
            this.chunkSeed *= this.chunkSeed * mult * add;
            this.chunkSeed += this.worldGenSeed;

            return (int)result;
        }

        protected int SelectRandom(params int[] vals)
        {
            return vals[this.NextInt(vals.Length)];
        }

        protected bool IsBiomeOcean(int id)
        {
            return (id == genInfo.ocean || id == genInfo.deeepOcean) && id >= 0;
        }

        public abstract int[] GetInts(int x, int y, int width, int height);
    }
}
