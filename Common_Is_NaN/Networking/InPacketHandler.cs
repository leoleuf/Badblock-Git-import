namespace Server_Is_NaN.Networking
{
    public abstract class InPacketHandler
    {
        public abstract void handleHandshake(In.Handshake packet);

        public abstract void handleKeepAlive(In.KeepAlive packet);
    }
}
