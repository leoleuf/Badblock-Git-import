using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using Server_Is_NaN.Networking;
using Server_Is_NaN.Networking.Sockets;

namespace Server_Is_NaN.Server
{
    public class SocketListener
    {
        public static readonly int default_port = 24070;

        private ManualResetEvent allDone = new ManualResetEvent(false);
        private Socket listener;
        private bool running = false;
        
        public void StartListening(string ip, int port)
        {
            new Thread(() => _StartListening(ip, port)).Start();
        }

        private void _StartListening(string ip, int port)
        {
            Console.WriteLine(ip);
            IPAddress address = null;

            if (!IPAddress.TryParse("127.0.0.1", out address))
            {
                address = Dns.GetHostEntry(Dns.GetHostName()).AddressList[0];
            }

            IPEndPoint endPoint = new IPEndPoint(address, port);
            listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            //listener.SetSocketOption(SocketOptionLevel.IPv6, SocketOptionName.IPv6Only, 0);

            try
            {
                listener.Bind(endPoint);
                listener.Listen(100);

                running = true;

                while (running)
                {
                    allDone.Reset();
                    Console.WriteLine("waiting...");
                    listener.BeginAccept(new AsyncCallback(AcceptCallback), listener);
                    allDone.WaitOne();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private void AcceptCallback(IAsyncResult ar)
        {
            allDone.Set();

            PlayerConnection pc = new PlayerConnection();

            SocketHandler sh = new SocketHandler(listener.EndAccept(ar), pc, PacketsIn.IN, PacketsOut.OUT);
            pc.Socket = sh;
        }

        public void StopListening()
        {
            running = false;

            listener.Close();
            allDone.Set();
            allDone.Close();
        }
    }
}
