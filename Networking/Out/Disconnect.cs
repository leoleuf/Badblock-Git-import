using static Server_Is_NaN.Networking.PacketOut;

namespace Server_Is_NaN.Networking.Out
{
    public class Disconnect : PacketOutJson
    {
        public string Message;

        public Disconnect(string Message)
        {
            this.Message = Message;
        }

        public Disconnect() : this("") { }

        public override void Handle(OutPacketHandler handler)
        {
            handler.HandleDisconnect(this);
        }
    }
}
