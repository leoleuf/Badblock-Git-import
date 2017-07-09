package fr.badblock.bungeecord.plugins.others.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Queues;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.tasks.SlackMessage;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;

/**
 * A TPS CLASS FOR GETTING THE LOST TICKS
 * @author Aurelian
 */
public class TPS implements Runnable
{
	long time;
	long sec;
	int nb;
	public static double tps		  = 0.0D;
	String o = "";
	public static Queue<Double> queue = Queues.newLinkedBlockingDeque();
	
	public TPS()
	{
		//this.sec = System.currentTimeMillis() + 20_000L;
		this.time = System.currentTimeMillis() + 1_000L;
		this.sec = System.currentTimeMillis();
		BungeeCord.getInstance().getScheduler().schedule(BadBlockBungeeOthers.getInstance(), this, 0L, 50, TimeUnit.MILLISECONDS);
		new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {
				while(true) {
					if (System.currentTimeMillis() > time) {
						time = System.currentTimeMillis() + 1_000L;
						double p = nb / 1_000D * 20D;
						nb = 0;
						double d = 20.0D - p;
						queue.add(d);
						tps = d;
						if (tps <= 15) {
							new SlackMessage("BungeeLag : " + ProxyServer.getInstance().getConfig().getListeners().iterator().next().getHost().getHostString() + " | " + String.format("%.2f", tps) + " TPS", "Monitoring - BungeeCord", "http://icon-icons.com/icons2/822/PNG/512/alert_icon-icons.com_66469.png", false).run();
						}
					}
				}
			}
		}.start();
	}
	
	@Override
	public void run()
	{
		double v = System.currentTimeMillis() - sec;
		if (v > 50) nb += v - 50;
		sec = System.currentTimeMillis();
	}
	
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * Récupérer le nombre de millisecondes perdus côté serveur
	 * (facteur lag)
	 * @return
	 */
	public static double getLostMilliseconds() {
		return (20D - TPS.tps) * 50D;
	}
	
	public static double getLostMilliseconds(double tps) {
		return (20D - tps) * 50D;
	}
	
}
