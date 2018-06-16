package fr.badblock.bungeecord.plugins.others.utils;

import java.util.concurrent.TimeUnit;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.tasks.SlackMessage;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;

/**
 * A TPS CLASS FOR GETTING THE LOST TICKS
 * 
 * @author Aurelian
 */
public class LagTask implements Runnable {

	private long time;

	public LagTask() {
		BungeeCord.getInstance().getScheduler().schedule(BadBlockBungeeOthers.getInstance(), this, 0L, 1000,
				TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		long difference = time - this.time;
		if (difference >= 2000) {
			double d = (difference - 1000);
			d /= 1000;
			new SlackMessage(
					"BungeeLag-Task: "
							+ ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost()
									.getHostString()
							+ " | " + String.format("%.2f", TPS.tps) + " TPS | Durée du lag: "
							+ String.format("%.2f", d) + " secondes",
					"Monitoring - BungeeCord",
					"http://icon-icons.com/icons2/822/PNG/512/alert_icon-icons.com_66469.png", false).run();
		}
		this.time = time;
	}

}
