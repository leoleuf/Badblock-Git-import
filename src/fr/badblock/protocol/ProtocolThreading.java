package fr.badblock.protocol;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import fr.badblock.protocol.buffers.ByteInputStream;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.utils.InvalidPacketException;
import lombok.Getter;
import lombok.Setter;

public class ProtocolThreading {

	@Getter@Setter public static ProtocolThreading instance = new ProtocolThreading();

	private List<Thread> threads;
	private Queue<ProtocolPacket> packets;

	public ProtocolThreading() {
		this.threads = new ArrayList<>();
		this.packets = new LinkedList<>();
		for (int i = 0; i < 32; i++) {
			threads.add(new Thread() {
				@Override
				public void run() {
					while (true) {
						while (!packets.isEmpty()) {
							ProtocolPacket protocolPacket = packets.poll();
							try {
								consume(protocolPacket);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	private void consume(ProtocolPacket protocolPacket) throws Exception {
		int id = protocolPacket.getInput().readByte();
		final Packet packet = protocolPacket.getProtocol().getPacketById(id);
		try {
			packet.read(protocolPacket.getInput());
			packet.handle(protocolPacket.getHandler());
		} catch (Exception e) {
			new InvalidPacketException(packet, e).printStackTrace();
		}			
	}

	public void readPacket(ByteInputStream input, PacketHandler packetHandler, Protocol protocol) {
		packets.add(new ProtocolPacket(input, packetHandler, protocol));
		Optional<Thread> optionalThread = threads.stream().filter(thread -> thread != null && thread.getState() != null && thread.getState().equals(State.WAITING)).findAny();
		if (optionalThread.isPresent()) {
			Thread thread = optionalThread.get();
			synchronized (thread) {
				thread.notify();
			}
		}
	}

}
