using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class PingAnswer : PacketOutJson
    {
        public int PlayerCount;
        public int Slots;
        /* Server version */
        public string Version;

        public PingAnswer(int PlayerCount, int Slots, string Version)
        {
            this.PlayerCount = PlayerCount;
            this.Slots = Slots;
            this.Version = Version;
        }

        public PingAnswer() { }

        public override void Handle(OutPacketHandler handler)
        {

        }
    }
}
