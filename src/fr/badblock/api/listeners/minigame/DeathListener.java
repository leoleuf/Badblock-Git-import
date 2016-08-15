package fr.badblock.api.listeners.minigame;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.runnables.BRunnable;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class DeathListener implements Listener{
	private Map<UUID, Location> respawn = new HashMap<UUID, Location>();
	private MJPlugin plugin;

	public DeathListener(MJPlugin plugin){
		this.plugin = plugin;
	}
	@EventHandler
	public void onDeath(final PlayerDeathEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getEntity());
		if(player == null){
			e.setDeathMessage(null);
			return;
		}
		player.incrementDeath();
		String msg = e.getDeathMessage();
		if(msg.contains("drowned")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est noyé dans une flaque"));
		} else if(msg.contains("blew up") || msg.contains("blown up")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est fait pulvériser par une explosion"));
		} else if(msg.contains("fell out of the world")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est offert un voyage dans l'espace"));
		}else if(msg.contains("hit the ground") || msg.contains("fell from a high")
				|| msg.contains("fell off") || msg.contains("fell out") || msg.contains("fell into")
				|| msg.contains("doomed to fall") || msg.contains("shot off") || msg.contains("blown from a high place")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est fracassé le crâne par terre"));
		} else if(msg.contains("went up in") || msg.contains("burned to death") || msg.contains("was burnt to a crisp") || msg.contains("walked into a fire") || msg.contains("tried to swim in lava") || msg.contains("walked into fire")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%a brûlé"));
		} else if(msg.contains("wall")){
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est étouffé"));
		} else if(msg.contains("got finished off by") || msg.contains("was slain by")){
			String killer = msg.replace("got finished off by", "");
			killer = killer.replace("was slain by", "");
			killer = killer.replace(player.getPlayerName(), "");
			killer = killer.replace(" ", "");
			BPlayer killerRP = BPlayersManager.getInstance().getPlayer(killer);
			if(killerRP == null){
				e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%est mort"));
			} else {
				e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est fait tapé dessus par " + "%green%" + killerRP.getPlayerName() + "%gold%"));
				killerRP.incrementKill();
			}
		} else if(msg.contains("was shot by")){
			String killer = msg.replace("was shot by", "");
			killer = killer.replace(player.getPlayerName(), "");
			killer = killer.replace(" ", "");
			BPlayer killerRP = BPlayersManager.getInstance().getPlayer(killer);
			if(killerRP == null){
				e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%est mort"));
			} else {
				e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%s'est prit la flèche de " + "%green%" + killerRP.getPlayerName() + "%gold%"));
				killerRP.incrementKill();
			}
		} else {
			e.setDeathMessage(ChatUtils.colorReplace("%red%" + player.getPlayerName() + " %gold%est mort"));
		}
		e.setDeathMessage(ChatUtils.colorReplace(e.getDeathMessage() + plugin.deathMessageSuffix(e) + "%gold%."));

		if(plugin.respawnAtDeathPoint())
			respawn.put(e.getEntity().getUniqueId(), e.getEntity().getLocation());
		if(plugin.lightningOnDeath())
			e.getEntity().getLocation().getWorld().strikeLightningEffect(e.getEntity().getLocation());

		if(plugin.respawnAutomaticly()){
			new BRunnable(20L){
				private int time = 1;
				@Override
				public void run(){
					if(time == 0){
						e.getEntity().spigot().respawn();
						cancel();
					}
					time--;
				}
			}.start();
		}
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player == null) return;
		if(plugin.respawnAtDeathPoint()){
			Location l = respawn.get(e.getPlayer().getUniqueId());
			if(l != null){
				if(l.getY() < 0) l = plugin.getSpectatorSpawn();
				e.setRespawnLocation(l);
				player.setSpectator(true, e.getPlayer());
				respawn.remove(e.getPlayer().getUniqueId());
			}
		} else if(plugin.respawnAtSpectatorSpawn() || player.isSpectator()){
			e.setRespawnLocation(plugin.getSpectatorSpawn());
		} else if(plugin.respawnAtTeamSpawn()) {
			if(player.getTeam() != null && player.getTeam().getTeam() != null)
				e.setRespawnLocation(player.getTeam().getTeam().getSpawn());
		}
	}
}
