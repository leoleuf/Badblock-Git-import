package fr.badblock.api.listeners.minigame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.TeamsManager;

public class FightListener implements Listener{
	public FightListener(MJPlugin plugin){}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		testFight(e);
	}
	
	public static void testFight(EntityDamageByEntityEvent e){
		BPlayer badblockDamaged = BPlayersManager.getInstance().getPlayer(e.getEntity())
				, badblockDamager = BPlayersManager.getInstance().getPlayer(e.getDamager());
		
		if(badblockDamager != null && badblockDamager.isSpectator()){
			e.setCancelled(true); return;
		} else if(badblockDamaged != null && badblockDamaged.isSpectator()){
			e.setCancelled(true); return;
		}
		
		if(badblockDamager == null || badblockDamaged == null) return;
		if(badblockDamager.equals(badblockDamaged)) return;
		
		if(!MJPlugin.getInstance().canFight() && badblockDamaged != null && badblockDamager != null)
			e.setCancelled(true);
		else if(badblockDamaged == null || badblockDamager == null);
		else if(badblockDamaged.isSpectator() || badblockDamager.isSpectator())
			e.setCancelled(true);
		else if(TeamsManager.enabled() && badblockDamager.getTeam().equals(badblockDamaged.getTeam())){
			badblockDamager.sendMessage("%red%On ne tape pas ses amis, oh !");
			e.setCancelled(true);
			e.setDamage(0d);
		}
	}
}
