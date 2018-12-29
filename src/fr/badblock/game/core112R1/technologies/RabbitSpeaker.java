package fr.badblock.game.core112R1.technologies;

import java.io.IOException;

import org.bukkit.Bukkit;

import fr.badblock.api.common.tech.rabbitmq.RabbitConnector;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.technologies.RabbitAPIListener;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
public class RabbitSpeaker implements fr.badblock.gameapi.technologies.RabbitSpeaker {

	@Getter
	@Setter
	private static RabbitConnector rabbitConnector = RabbitConnector.getInstance();

	private RabbitService rabbitService;

	public RabbitSpeaker(RabbitSettings rabbitSettings) throws IOException {
		RabbitService service = getRabbitConnector().registerService(new RabbitService("default", rabbitSettings));
		GameAPI.getAPI().setRabbitService(service);
		setRabbitService(service);
	}

	@Override
	public void sendAsyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(ttl, content);
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, queueName, debug, RabbitPacketEncoder.UTF8,
				RabbitPacketType.MESSAGE_BROKER);
		
		this.getRabbitService().sendPacket(rabbitPacket);
	}

	@Override
	public void sendAsyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(ttl, content);
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, queueName, debug, RabbitPacketEncoder.UTF8,
				RabbitPacketType.PUBLISHER);
		
		this.getRabbitService().sendPacket(rabbitPacket);
	}

	@Override
	public void sendSyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(ttl, content);
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, queueName, debug, RabbitPacketEncoder.UTF8,
				RabbitPacketType.MESSAGE_BROKER);
		
		try {
			this.getRabbitService().sendSyncPacket(rabbitPacket);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SYNC Packet sent to queue " + queueName + ". Be aware of possible performance burns");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendSyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(ttl, content);
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, queueName, debug, RabbitPacketEncoder.UTF8,
				RabbitPacketType.PUBLISHER);
		
		try {
			this.getRabbitService().sendSyncPacket(rabbitPacket);
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SYNC Packet sent to queue " + queueName + ". Be aware of possible performance burns");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void listen(RabbitAPIListener rabbitListener) {
		new RabbitListener(this.getRabbitService(), rabbitListener.getQueue(), convert(rabbitListener.getType()), rabbitListener.isDebug()) {
			@Override
			public void onPacketReceiving(String body) {
				rabbitListener.onPacketReceiving(body);
			}
		};
	}

	private static RabbitListenerType convert(fr.badblock.gameapi.technologies.RabbitListenerType vl)
	{
		for (RabbitListenerType rb : RabbitListenerType.values())
		{
			if (rb.name().equalsIgnoreCase(vl.name()))
			{
				return rb;
			}
		}
		
		return null;
	}
	
}
