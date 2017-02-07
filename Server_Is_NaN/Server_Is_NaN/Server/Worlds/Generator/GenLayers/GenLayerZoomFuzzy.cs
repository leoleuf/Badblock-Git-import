namespace Server_Is_NaN.Server.Worlds.Generator.GenLayers
{
    public class GenLayerZoomFuzzy : GenLayerZoom
    {
        public GenLayerZoomFuzzy(GenLayer parent, long worldSeed) : base(parent, worldSeed)
        {
        }

        protected override int SelectModeOrRandom(int i1, int i2, int i3, int i4)
        {
            return SelectRandom(i1, i2, i3, i4);
        }
    }
}
