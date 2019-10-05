package fr.badblock.tech.protocol.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import fr.badblock.tech.protocol.PacketHandler;
import fr.badblock.tech.protocol.Protocol;
import fr.badblock.tech.protocol.buffers.ByteInputStream;
import fr.badblock.tech.protocol.buffers.ByteOutputStream;
import fr.badblock.tech.protocol.packets.Packet;
import fr.badblock.tech.protocol.utils.InvalidPacketException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SocketHandler extends Thread implements PacketSender {
    private Socket socket;

    private ByteInputStream in;
    private ByteOutputStream out;

    private final Queue<Packet> packets;

    private Protocol protocolIn;
    private Protocol protocolOut;
    private PacketHandler handler;

    private int receiveId = 0;
    private int sendId = 0;

    private boolean debug = false;

    private boolean running;

    private final Thread thread;

    SocketHandler(Socket socket, Protocol protocolIn, Protocol protocolOut, PacketHandler handler, boolean debug) throws IOException {
        super("Socket Handler (" + socket.getInetAddress() + ")");
        this.socket = socket;
        this.in = new ByteInputStream(socket.getInputStream());
        this.out = new ByteOutputStream(socket.getOutputStream());

        this.protocolIn = protocolIn;
        this.protocolOut = protocolOut;
        this.handler = handler;

        this.packets = new LinkedBlockingDeque<>();
        thread = new Thread("Ladder/sendPacketsThread") {
            @Override
            public void run() {
                synchronized (thread) {
                    while (running && socket.isConnected() && !socket.isClosed()) {
                        try {
                            if (packets.size() > 200) {
                                System.out.println("Too many packets (" + packets.size() + ") on SocketHandler");
                            }
                            while (!packets.isEmpty()) {
                                System.out.println("SOCKET > SENDPACKET-C");
                                Iterator<Packet> iterator = packets.iterator();
                                while (iterator.hasNext()) {
                                    Packet packet = iterator.next();
                                    iterator.remove();
                                    System.out.println("SOCKET > SENDPACKET-D");
                                    if (packet == null) continue;
                                    try {
                                        System.out.println("SOCKET > SENDPACKET-E");
                                        out.writeByte((byte) 0);
                                        if (debug) {
                                            out.writeInt(sendId);
                                            sendId++;
                                        }
                                        out.flush();
                                        protocolOut.writePacket(out, packet);
                                    } catch (Throwable e) {
                                        //System.out.println("M�chant packet (" + packet + ") :");
                                        e.printStackTrace();
                                        out.close();
                                        out = new ByteOutputStream(socket.getOutputStream());
                                    }
                                }
                            }
                            thread.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        System.out.println("SOCKET > RECEIVEEEEEEEE-A " + (!socket.isConnected()) + " / " + socket.isClosed());
        if (!socket.isConnected() || socket.isClosed()) stop();
        System.out.println("SOCKET > RECEIVEEEEEEEE-B");
        if (!running) thread.start();
        running = true;
        try {
            System.out.println("SOCKET > RECEIVEEEEEEEE-C");
            while (in.readByte() != -1) {
                System.out.println("SOCKET > RECEIVEEEEEEEE-D");
                if (debug) {
                    int id = in.readInt();

                    if (receiveId != id) {
                        //System.out.println("packet lost ? :0" + " waited = " + receiveId +  ", received = " + id);
                        receiveId = id;
                    }

                    receiveId++;
                }
                try {
                    protocolIn.readPacket(in, handler);
                } catch (Throwable e) {
                    new InvalidPacketException(socket, e).printStackTrace();
                }
                Thread.sleep(50);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            running = false;
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        running = false;
        if (socket.isClosed()) return;

        out.writeByte((byte) -1);
        socket.close();

        socketClosed();
    }

    public void sendPacket(Packet packet) {
        System.out.println("SOCKET > SENDPACKET-A " + packet.toString());
        if (socket.isClosed()) return;
        System.out.println("SOCKET > SENDPACKET-B  " + packet.toString());

        packets.add(packet);
        synchronized (thread) {
            thread.notify();
        }
    }

    private void socketClosed() {
    }
}