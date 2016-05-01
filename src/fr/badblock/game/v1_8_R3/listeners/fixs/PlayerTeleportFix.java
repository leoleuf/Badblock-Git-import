package fr.badblock.game.v1_8_R3.listeners.fixs;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.packets.out.play.PlayNamedEntitySpawn;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Fix des t�l�portations bugu�e Bukkit
 * @author LeLanN
 */
public class PlayerTeleportFix extends BadListener {
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		final Location from = event.getFrom().clone();
		final Location to = event.getTo().clone();

		runRefresh(player, from, to);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		final Location from = player.getLocation().clone();
		final Location to = event.getRespawnLocation().clone();

		runRefresh(player, from, to);
	}
	
	private void runRefresh(Player refresh, Location from, Location to){
		if(refresh.getGameMode() == GameMode.SPECTATOR) return;
		
		new BukkitRunnable(){
			@Override
			public void run(){
				for(Player player : Bukkit.getOnlinePlayers()){
					if(player.equals(refresh)) continue;
					refresh((BadblockPlayer) player, (BadblockPlayer) refresh);
				}
			}
		}.runTaskLater(GameAPI.getAPI(), 5L);
	}
	
	private void refresh(BadblockPlayer toShow, BadblockPlayer player){
		if(!toShow.isValid() || !player.isValid()){
			return;
		}
		
		if(!player.canSee(toShow)) return;
		
		GameAPI.getAPI().createPacket(PlayEntityDestroy.class)
						.setEntities(new int[]{ toShow.getEntityId() })
						.send(toShow);
		
		new BukkitRunnable(){
			@Override
			public void run(){
				GameAPI.getAPI().createPacket(PlayNamedEntitySpawn.class)
								.setPlayer(toShow)
								.send(player);
				
				for(int i=0; i<toShow.getInventory().getArmorContents().length;i++){
					GameAPI.getAPI().createPacket(PlayEntityEquipment.class)
									.setEntityId(toShow.getEntityId())
									.setItemStack(toShow.getInventory().getArmorContents()[i])
									.setSlot(i)
									.send(player);
				}
			}
		}.runTaskLater(GameAPI.getAPI(), 1L);
	}
}
