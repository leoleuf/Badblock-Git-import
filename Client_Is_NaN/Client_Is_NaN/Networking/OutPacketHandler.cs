namespace Server_Is_NaN.Networking
{
    public abstract class OutPacketHandler
    {
        public abstract void HandleDisconnect(Out.Disconnect packet);

        public abstract void HandleKeepAlive(Out.KeepAlive packet);

        public abstract void HandleLoginSuccess(Out.LoginSuccess packet);

        public abstract void HandlePingAnswer(Out.PingAnswer packet);

        public abstract void HandleChangeDimension(Out.ChangeDimension packet);

        public abstract void HandleSendChunks(Out.SendChunks packet);
    }
}
