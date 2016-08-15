package fr.badblock.protocol.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketLadderStop;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false) 
public abstract class SocketHost extends Thread {
	private static final int 	BACKLOG = 50;

	private boolean 	  								running;

	private final InetAddress   					    address;
	private final int								    port;
	private final ServerSocket  					    server;

	private final Map<InetSocketAddress, PacketSender>  handlers;

	public SocketHost(InetAddress address, int port) throws IOException {
		super("TCP Listener");

		if(address == null)
			server    = new ServerSocket(port);
		else server   = new ServerSocket(port, BACKLOG, address);

		this.address  = server.getInetAddress();
		this.port     = port;

		this.handlers = new HashMap<>();

		this.running  = false;

		start();
	}

	@Override
	public void run(){
		running = true;
		while(running){
			try {
				final Socket socket = server.accept();
				
				new Thread(){
					@SuppressWarnings("resource") @Override
					public void run(){
						try {
							ByteInputStream input = new ByteInputStream(socket.getInputStream());
							String ip = input.readUTF();
							int port = input.readInt();
							InetSocketAddress servAddress  = new InetSocketAddress(ip, port);
							InetSocketAddress sendAddress  = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
							
							Protocol		  protocolIn   = getInputProtocol(sendAddress, servAddress);
							Protocol		  protocolOut  = getOutputProtocol(sendAddress, servAddress);
							PacketHandler	  handler      = createHandler(sendAddress, servAddress);

							if(protocolIn == null || protocolOut == null || handler == null){
								socket.close();
							} else {
								SocketHandler sockHandler  = new SocketHandler(socket, protocolIn, protocolOut, handler, false);
								handlers.put(servAddress, sockHandler);

								sockHandler.start();
							}
						} catch(Throwable e){
						}
					}
				}.start();
			} catch (Exception unused){
				unused.printStackTrace();
			}
		}
	}

	public void end(){
		running = false;

		for(PacketSender sender : handlers.values()){
			sender.sendPacket(new PacketLadderStop());
		}
		
		try {
			if(!server.isClosed())
				server.close();
		} catch (IOException e){}
	}

	public void sendPacket(InetSocketAddress address, Packet packet) {
		PacketSender socketHandler = handlers.get(address);
		if(socketHandler != null)
			socketHandler.sendPacket(packet);
	}

	public abstract Protocol      getOutputProtocol(InetSocketAddress trueAddress, InetSocketAddress serverAddress);
	public abstract Protocol      getInputProtocol(InetSocketAddress trueAddress, InetSocketAddress serverAddress);
	public abstract PacketHandler createHandler(InetSocketAddress trueAddress, InetSocketAddress serverAddress);
}
