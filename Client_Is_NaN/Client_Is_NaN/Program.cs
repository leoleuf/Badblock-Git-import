using System;
using System.Net;
using System.Net.Sockets;
using Server_Is_NaN.Networking.Sockets;
using Server_Is_NaN.Networking;

namespace Client_Is_NaN
{
    public class Program
    {
        static void Main(string[] args)
        {
            string ip = "127.0.0.1";

            IPAddress address = null;
            Console.WriteLine(IPAddress.TryParse(ip, out address));

            IPEndPoint endPoint = new IPEndPoint(address, 24070);


            Socket s = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            Console.WriteLine(endPoint);

            s.Connect(endPoint);

            new SocketHandler(s, new MyHandler(), PacketsOut.OUT, PacketsIn.IN).SendPacket(new Server_Is_NaN.Networking.In.Handshake(1, "alpha_1", "LeLanN"));
            // null : handler
        }
    }
}
