package fr.badblock.protocol.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.buffers.ByteOutputStream;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.utils.InvalidPacketException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data@EqualsAndHashCode(callSuper=false) 
public class SocketHandler extends Thread implements PacketSender {
	private Socket		 		   socket;

	private ByteInputStream  	   in;
	private ByteOutputStream 	   out;

	private final Queue<Packet>	   packets;

	private Protocol 			   protocolIn;
	private Protocol 			   protocolOut;
	private PacketHandler		   handler;

	private int receiveId = 0;
	private int sendId    = 0;

	private boolean debug = false;

	@Getter
	private boolean				   running;

	private Thread				   thread;

	public SocketHandler(Socket socket, Protocol protocolIn, Protocol protocolOut, PacketHandler handler, boolean debug) throws IOException {
		super("Socket Handler (" + socket.getInetAddress() + ")");
		this.socket      = socket;
		this.in          = new ByteInputStream(socket.getInputStream());
		this.out         = new ByteOutputStream(socket.getOutputStream());

		this.protocolIn  = protocolIn;
		this.protocolOut = protocolOut;
		this.handler     = handler;

		this.packets 	 = new LinkedBlockingDeque<>();
		thread = new Thread("Ladder/sendPacketsThread") {
			@Override
			public void run(){
				synchronized (thread) {
					while(running && socket.isConnected() && !socket.isClosed()){
						try {
							if(packets.size() > 200){
								System.out.println("Too many packets (" + packets.size() + ") on SocketHandler");
							}
							while (!packets.isEmpty()) {
								Iterator<Packet> iterator = packets.iterator();
								while (iterator.hasNext()) {
									Packet packet = iterator.next();
									iterator.remove();
									if(packet == null) continue;
									try {
										out.writeByte((byte) 0);
										if (debug) {
											out.writeInt(sendId);
											sendId++;
										}
										protocolOut.writePacket(out, packet);
									} catch(Throwable e){
										//System.out.println("Méchant packet (" + packet + ") :");
										e.printStackTrace();
									}
								}
							}
							Thread.sleep(5);
						} catch (Exception e) {}
					}
				}
			}
		};
	}	

	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		if (!socket.isConnected() || socket.isClosed()) stop();
		running = true;
		thread.start();
		try {
			while(in.readByte() != -1){
				if(debug){
					int id = in.readInt();

					if(receiveId != id){
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
			}
		} catch (Throwable e) {

		} finally {
			running = false;
			try {
				close();
			} catch (IOException e){}
		}
	}

	public void close() throws IOException {
		running = false;
		if(socket.isClosed()) return;

		out.writeByte((byte) -1);
		socket.close();

		socketClosed();
	}

	public void sendPacket(Packet packet) {
		if(socket.isClosed()) return;

		packets.add(packet);
	}

	protected void socketClosed(){}
}