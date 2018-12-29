package fr.badblock.game.core112R1.gameserver.threading;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.run.RunType;

public class ServerForceKillListener extends RabbitListener {
	public ServerForceKillListener() {
		super(GamePlugin.getInstance().getRabbitSpeaker().getRabbitService(), "forcekill", RabbitListenerType.SUBSCRIBER, false);
	}

	@Override
	public void onPacketReceiving(String body) {
		if (body == null)
			return;
		
		if(body.equalsIgnoreCase(Bukkit.getServerName()) && GameAPI.getAPI().getRunType() == RunType.DEV)
		{
			GameServerKeeperAliveTask.sendDevSignal(false, 0);
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			System.out.println("Kill process...");
			
			try {
				new ProcessBuilder("kill", "-9", pid).start().waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
