package fr.badblock.api.listeners.minigame;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.runnables.BRunnable;

public class PingListener implements Listener{
	private static int timeWithoutPlayer = 0, timeVip = 10;
	private static long vipLastUpdate;
	private static MJPlugin plugin;

	public PingListener(final MJPlugin plugin){
		PingListener.plugin = plugin;
		new BRunnable(40L){
			@Override
			public void run(){
				if(Bukkit.getOnlinePlayers().size() == 0 && plugin.getStatus() == GameStatus.PLAYING)
					Bukkit.shutdown();
			}
		}.start();
	}
	
	public static void kick(){
		Bukkit.getScheduler().runTaskLater(MJPlugin.getInstance(), new Runnable() {
			@Override
			public void run() {
				for(final Player p : Bukkit.getOnlinePlayers()){
					plugin.kick(p);
				}
			}
		}, 20 * 10);
		Bukkit.getScheduler().runTaskLater(MJPlugin.getInstance(), new Runnable() {
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}, 20 * 20);
	}
	@EventHandler
	public void onPing(ServerListPingEvent e){
		e.setMaxPlayers(plugin.maxPlayer());
		if(BPlayersManager.getInstance().getPlayers().size() == 0){
			timeWithoutPlayer++;
		} else timeWithoutPlayer = 0;
		if(timeWithoutPlayer == 500_000) Bukkit.shutdown();
		if(BPlayersManager.getInstance().getPlayers().size() >= plugin.maxPlayer()){
			e.setMotd("OFF");
		} else {
			if(plugin.getStatus() == GameStatus.PLAYING){
				e.setMotd("OFF");
			} else {
				if(timeVip > 0){
					if(new Date().getTime() - vipLastUpdate >= 1000){
						vipLastUpdate = new Date().getTime();
						timeVip--;
					}
					if(timeVip == 0){
						e.setMotd("on");
					} else e.setMotd("on true " + timeVip);
				} else {
					e.setMotd("on");
				}
			}
		}
	}

}
