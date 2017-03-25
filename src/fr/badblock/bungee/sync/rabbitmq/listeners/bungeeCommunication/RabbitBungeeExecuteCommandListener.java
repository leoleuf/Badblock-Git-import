package fr.badblock.bungee.sync.rabbitmq.listeners.bungeeCommunication;

import fr.badblock.bungee.BadBungee;
import fr.badblock.commons.technologies.rabbitmq.RabbitListener;
import fr.badblock.commons.technologies.rabbitmq.RabbitListenerType;
import net.md_5.bungee.api.ProxyServer;

public class RabbitBungeeExecuteCommandListener extends RabbitListener {

	public static boolean done = false;
	
	public RabbitBungeeExecuteCommandListener() {
		super(BadBungee.getInstance().getRabbitService(), "bungee.worker.executeCommand", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		ProxyServer proxy = BadBungee.getInstance().getProxy();
		System.out.println("[BadBungee] Command dispatch: " + body);
		proxy.getPluginManager().dispatchCommand(proxy.getConsole(), body);
	}
}
