using System;
using Server_Is_NaN.Networking;
using Server_Is_NaN.Networking.Out;

namespace Client_Is_NaN
{
    public class MyHandler : OutPacketHandler
    {
        public override void HandleDisconnect(Server_Is_NaN.Networking.Out.Disconnect packet)
        {
            Console.WriteLine(packet.Message);
        }

        public override void HandlePingAnswer(Server_Is_NaN.Networking.Out.PingAnswer packet)
        {
            Console.WriteLine(packet.Slots);
        }

        public override void HandleKeepAlive(Server_Is_NaN.Networking.Out.KeepAlive packet)
        {
            Console.WriteLine("oooh");
            /**
             * FIXME
             * - Réponse
             * - Déconnexion si aucun reçu
             */

            new Server_Is_NaN.Networking.In.Handshake();
        }

        public override void HandleLoginSuccess(Server_Is_NaN.Networking.Out.LoginSuccess packet)
        {
            Console.WriteLine("ouiwi");
        }

        public override void HandleChangeDimension(Server_Is_NaN.Networking.Out.ChangeDimension packet)
        {

        }

        public override void HandleSendChunks(SendChunks packet)
        {
            Console.WriteLine("A chunky!");
        }
    }
}
