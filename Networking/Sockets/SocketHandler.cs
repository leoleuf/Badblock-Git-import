using System;
using System.Collections.Concurrent;
using System.Net.Sockets;
using System.Threading;

namespace Server_Is_NaN.Networking.Sockets
{
    public class SocketHandler
    {
        private ConcurrentQueue<Packet> pendingPackets = new ConcurrentQueue<Packet>();

        private Socket socket;
        private ByteNetworkStream stream;
        private PacketGroup from, to;
        private object handler;
        private bool running;

        private bool closed = false;

        private ManualResetEvent allDone = new ManualResetEvent(false);

        public SocketHandler(Socket socket, object handler, PacketGroup from, PacketGroup to)
        {
            this.socket = socket;
            this.stream = new ByteNetworkStream(socket);
            this.from = from;
            this.to = to;
            this.handler = handler;

            new Thread(Listen).Start();
            new Thread(SendThread).Start();
        }

        private void Listen()
        {
            this.running = true;

            try
            {
                while (running)
                {
                    int id = stream.ReadInt();
                    bool compressed = stream.ReadBoolean();

                    if (compressed) { } //FIXME

                    from.ReadPacket(id, handler, stream);
                }
            }
            catch (Exception e) {
                Console.WriteLine(e);
            }


            running = false;

            if (socket.Connected)
                socket.Close();
        }

        private void SendThread()
        {
            try
            {
                while (running)
                {
                    Packet packet = null;

                    if (pendingPackets.Count == 0)
                        if (closed)
                            break;
                        else allDone.WaitOne();
                    else if (pendingPackets.TryDequeue(out packet))
                    {
                        to.WritePacket(stream, false, packet);
                    }
                }
            }
            catch (Exception)
            {
                //FIXME
            }

            running = false;

            if(socket.Connected)
                socket.Close();
        }

        public void SendPacket(Packet packet)
        {
            if (closed)
                return;

            pendingPackets.Enqueue(packet);
            allDone.Set();
        }

        public void Close()
        {
            closed = true;
        }
    }
}
