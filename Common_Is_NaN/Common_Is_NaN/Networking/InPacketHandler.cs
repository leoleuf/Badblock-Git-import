namespace Server_Is_NaN.Networking
{
    public abstract class InPacketHandler
    {
        public abstract void HandleDisconnect(In.Disconnect packet);

        public abstract void HandleHandshake(In.Handshake packet);

        public abstract void HandleKeepAlive(In.KeepAlive packet);
    }
}
