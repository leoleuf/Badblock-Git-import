package fr.badblock.protocol.socket;

import java.io.IOException;
import java.net.Socket;
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

	public SocketHandler(Socket socket, Protocol protocolIn, Protocol protocolOut, PacketHandler handler, boolean debug) throws IOException {
		super("Socket Handler (" + socket.getInetAddress() + ")");
		this.socket      = socket;
		this.in          = new ByteInputStream(socket.getInputStream());
		this.out         = new ByteOutputStream(socket.getOutputStream());

		this.protocolIn  = protocolIn;
		this.protocolOut = protocolOut;
		this.handler     = handler;

		this.packets 	 = new LinkedBlockingDeque<>();
	}	

	@Override
	public void run(){
		sendPacketsThread();

		running = true;

		try {
			while(in.readByte() != -1){
				if(debug){
					int id = in.readInt();

					if(receiveId != id){
						System.out.println("packet lost ? :0" + " waited = " + receiveId +  ", received = " + id);
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
		
		synchronized(this) {
			packets.add(packet);
		}
	}

	private void sendPacketsThread(){
		new Thread(){
			@Override
			public void run(){
				while(running){
					try {
						if(packets.size() > 200){
							System.out.println("Too many packets (" + packets.size() + ") on SocketHandler");
						}

						for(int i = 0;i<(packets.size() + 1) * 4;i++){
							Packet packet = packets.poll();
							
							if(packet == null) {
								Thread.sleep(2L);
							} else {
								try {
									out.writeByte((byte) 0);

									if(debug){
										out.writeInt(sendId);
										sendId++;
									}

									protocolOut.writePacket(out, packet);
								} catch(Throwable e){
									System.out.println("Méchant packet (" + packet + ") :");
									e.printStackTrace();
								}
							}
						}

						Thread.sleep(3L);
					} catch (Exception e) {}
				}
			}
		}.start();
	}

	protected void socketClosed(){}
}
