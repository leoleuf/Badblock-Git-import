package fr.badblock.api.listeners.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.runnables.BRunnable;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class AntiSpawnKillListener implements Listener{
	private List<UUID> invincible = new ArrayList<UUID>();
	
	@EventHandler
	public void onRespawn(final PlayerRespawnEvent e){
		invincible.add(e.getPlayer().getUniqueId());
		new BRunnable(20L){
			@Override
			public void run(){
				time++;
				if(time == 10){
					ChatUtils.sendMessagePlayer(e.getPlayer(), "%red%Vous n'�tes plus prot�g� !");
					invincible.remove(e.getPlayer().getUniqueId());
				}
			}
		}.start();
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onFight(EntityDamageByEntityEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getEntity());
		BPlayer damager = BPlayersManager.getInstance().getPlayer(e.getDamager());
		
		if(player == null || damager == null) return;
		if(invincible.contains(player.getUniqueId())){
			damager.sendMessage("%red%Ce joueur est prot�g� par l'Anti-SpawnKill !");
			e.setCancelled(true);
		} else if(invincible.contains(damager.getUniqueId())){
			damager.sendMessage("%red%Vous �tes prot�g� par l'Anti-SpawnKill ! Vous ne pouvez pas taper.");
			e.setCancelled(true);
		}
	}
}
