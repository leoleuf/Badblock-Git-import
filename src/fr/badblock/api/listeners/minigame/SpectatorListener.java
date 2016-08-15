package fr.badblock.api.listeners.minigame;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import fr.badblock.api.BPlugin;
import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.runnables.BRunnable;
import fr.badblock.api.utils.bukkit.ChatUtils;
import fr.badblock.api.utils.bukkit.PlayerUtils;

public class SpectatorListener implements Listener {
	private static final String DOOR_ITEM_NAME = ChatUtils.colorReplace("%gold%Cliquez pour quitter la %red%partie %gold%!");
	private static final String COMPASS_ITEM_NAME = ChatUtils.colorReplace("%gold%Cliquez pour vous téléporter à un %red%joueur %gold%!");
	private static final String INVENTORY_NAME = ChatUtils.colorReplace("%dred%[%red%BadBlock%dred%] %gold%Spectateurs");
	
	@SuppressWarnings("deprecation")
	public ItemStack[] getContents(int inventorySize){
		inventorySize = (int)(BPlayersManager.getInstance().countPlayers() / 9) * 9;
		if(BPlayersManager.getInstance().countPlayers() > inventorySize) inventorySize += 9;

		ItemStack[] is = new ItemStack[inventorySize];
		int i = 0;
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(!player.isSpectator()){
				ItemStack item = new ItemStack(Material.SKULL_ITEM, i + 1, (short)0, (byte)3);
				SkullMeta meta = (SkullMeta) item.getItemMeta();
				meta.setOwner(player.getPlayerName());
				String playerName = (player.getTeam() != null ? player.getTeam().getDisplayName() + " " : "%red%") + player.getPlayerName();
				meta.setLore(Arrays.asList(new String[]{ChatUtils.colorReplace("%gold%Clique pour te téléporter à " + playerName + "%gold% !")}));
				meta.setDisplayName(ChatUtils.colorReplace("%red%" + player.getPlayerName()));
				item.setItemMeta(meta);
				
				is[i] = item;
				
				i++; if(i == 54) break;
			}
		}
		return is;
	}
	public static void prepareSpectator(Player p){
		p.setAllowFlight(true);
		p.setFlying(true);
		p.getInventory().clear();
		ItemStack door = new ItemStack(Material.WOOD_DOOR, 1);
		ItemMeta doorim = door.getItemMeta();
		doorim.setDisplayName(DOOR_ITEM_NAME);
		door.setItemMeta(doorim);
		
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta compassim = compass.getItemMeta();
		compassim.setDisplayName(COMPASS_ITEM_NAME);
		compass.setItemMeta(compassim);
		
		p.getInventory().setItem(0, compass);
		p.getInventory().setItem(8, door);
	}
	@EventHandler
	public void onClickInventory(InventoryClickEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getWhoClicked());
		if(player == null || !player.isSpectator()) return;

		if(!INVENTORY_NAME.equals(e.getInventory().getName())) {
			e.setCancelled(true);
			return;
		}

		if(e.getCurrentItem() == null) return;
		if(e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		
		BPlayer clicked = BPlayersManager.getInstance().getPlayer(ChatUtils.colorDelete(e.getCurrentItem().getItemMeta().getDisplayName()));
		if(clicked == null || !PlayerUtils.isValid(clicked.getPlayer())){
			player.sendMessage("%red%Le joueur est introuvable !");
		} else {
			player.getPlayer().teleport(clicked.getPlayer());
			player.sendMessage("%green%Téléportation vers " + clicked.getPlayerName() + " !");
		}
		e.setCancelled(true);
		e.getWhoClicked().closeInventory();
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		final BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player == null || !player.isSpectator()) return;
		if(e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) {
		} else if(e.getItem().getItemMeta().getDisplayName().equals(DOOR_ITEM_NAME)){
			((MJPlugin)MJPlugin.getInstance()).kick(e.getPlayer());
		} else if(e.getItem().getItemMeta().getDisplayName().equals(COMPASS_ITEM_NAME)){
			final ItemStack[] players = getContents(0);
			final Inventory inv = Bukkit.createInventory(null, players.length, INVENTORY_NAME);
			inv.setContents(players);
			e.getPlayer().openInventory(inv);
//			new BRunnable(40L){
//				@Override
//				public void run(){
//					if(player == null || !PlayerUtils.isValid(player.getPlayer()) || player.getPlayer().getOpenInventory() == null) {
//						cancel();
//						return;
//					}
//					inv.setContents(getContents(players.length));
//					player.getPlayer().updateInventory();
//				}
//			}.start();
		} 
		e.setCancelled(true);
	}
	@EventHandler
	public void onTarget(EntityTargetEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getTarget());
		if(player != null && player.isSpectator())
			e.setCancelled(true);
	}
	@EventHandler
	public void onBlockCanBuild(BlockCanBuildEvent e){
		if (!e.isBuildable()) {
			Location blockL = e.getBlock().getLocation();

			boolean allowed = false;
			for (BPlayer target : BPlayersManager.getInstance().getPlayers()) {
				if (target.getPlayer().getWorld().equals(e.getBlock().getWorld())){
					Location playerL = target.getPlayer().getLocation();
					if ((playerL.getX() > blockL.getBlockX() - 1) && (playerL.getX() < blockL.getBlockX() + 1) && 
							(playerL.getZ() > blockL.getBlockZ() - 1) && (playerL.getZ() < blockL.getBlockZ() + 1) && 
							(playerL.getY() > blockL.getBlockY() - 2) && (playerL.getY() < blockL.getBlockY() + 1)) {
						if (target.isSpectator()) {
							allowed = true;
						} else {
							allowed = false;
							break;
						}
					}
				}
			}
			e.setBuildable(allowed);
		}
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		Location blockL = e.getBlock().getLocation();
		for (BPlayer target : BPlayersManager.getInstance().getPlayers()) {
			if ((target.isSpectator()) && (target.getPlayer().getWorld().equals(e.getBlock().getWorld()))) {
				Location playerL = target.getPlayer().getLocation();
				if ((playerL.getX() > blockL.getBlockX() - 1) && (playerL.getX() < blockL.getBlockX() + 1) && 
						(playerL.getZ() > blockL.getBlockZ() - 1) && (playerL.getZ() < blockL.getBlockZ() + 1) && 
						(playerL.getY() > blockL.getBlockY() - 2) && (playerL.getY() < blockL.getBlockY() + 1)) {
					target.getPlayer().teleport(e.getPlayer(), PlayerTeleportEvent.TeleportCause.PLUGIN);
					target.sendMessage("%red%Ne te mets pas en travers de la route d'un joueur ! :)");
				}
			}
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getEntity());
		if(player != null && player.isSpectator()){
			e.setCancelled(true);
			e.setDamage(0d);
		}
	}
	@EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onDamage(final EntityDamageByEntityEvent e) {
		BPlayer entity = BPlayersManager.getInstance().getPlayer(e.getEntity());
		if(entity == null) return;
		if (((e.getDamager() instanceof Projectile)) && (!(e.getDamager() instanceof ThrownPotion)) && entity.isSpectator()){
			e.setCancelled(true);

			final Player spectatorInvolved = (Player)e.getEntity();
			final boolean wasFlying = spectatorInvolved.isFlying();
			final Location initialSpectatorLocation = spectatorInvolved.getLocation();

			final Vector initialProjectileVelocity = e.getDamager().getVelocity();
			final Location initialProjectileLocation = e.getDamager().getLocation();

			spectatorInvolved.setFlying(true);
			spectatorInvolved.teleport(initialSpectatorLocation.clone().add(0.0D, 6.0D, 0.0D), PlayerTeleportEvent.TeleportCause.PLUGIN);


			new BRunnable(1L){
				public void run(){
					e.getDamager().teleport(initialProjectileLocation);
					e.getDamager().setVelocity(initialProjectileVelocity);
					cancel();
				}
			}.start();

			new BRunnable(5L){
				{time = 1;}
				public void run(){
					time--;
					if(time == 0) return; else cancel();
					spectatorInvolved.teleport(new Location(initialSpectatorLocation.getWorld(), initialSpectatorLocation.getX(), initialSpectatorLocation.getY(), initialSpectatorLocation.getZ(), spectatorInvolved.getLocation().getYaw(), spectatorInvolved.getLocation().getPitch()), PlayerTeleportEvent.TeleportCause.PLUGIN);
					spectatorInvolved.setFlying(wasFlying);
				}
			}.start();
		}
	}
	@EventHandler
	public void onToggleFly(PlayerToggleFlightEvent e){
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		if(player.isSpectator() && e.getPlayer().isFlying())
			e.setCancelled(true);
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		MJPlugin plugin = ((MJPlugin)BPlugin.getInstance());
		BPlayer player = BPlayersManager.getInstance().getPlayer(e.getPlayer());
		
		Location spawn = null;
		
		if(player.getTeam() != null)
			spawn = player.getTeam().getTeam().getSpawn();
		else spawn = plugin.getSpectatorSpawn();
		
		if(e.getPlayer().getLocation().getWorld().equals(plugin.getSpectatorSpawn().getWorld()) && e.getPlayer().getLocation().distance(plugin.getSpectatorSpawn()) >= plugin.getMaxDistance() && plugin.getStatus() == GameStatus.PLAYING){
			e.getPlayer().teleport(spawn);
			ChatUtils.sendMessagePlayer(e.getPlayer(), "%red%Ne vous éloignez pas trop !");
		} else if(e.getTo().getY() < 0d){
			if(player.isSpectator()){
				e.setTo(plugin.getSpectatorSpawn());
				player.sendMessage("%red%Tu ne te suicideras point.");
			} else if(plugin.getStatus() == GameStatus.WAITING_PLAYERS || plugin.getStatus() == GameStatus.STARTING){
				e.setTo(plugin.getSpawn());
				player.sendMessage("%red%Tu ne te suicideras point.");
			}
		}
	}
}
