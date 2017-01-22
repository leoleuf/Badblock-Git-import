namespace Server_Is_NaN.Server.World
{
    public interface IChunkProvider
    {
        bool IsChunkLoaded(int x, int z);

        Chunk GetChunk(int x, int z);

        void UnloadChunk(Chunk chunk);

        void UnloadChunks();
    }
}
