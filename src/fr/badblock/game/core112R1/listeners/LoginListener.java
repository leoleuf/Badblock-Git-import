package fr.badblock.game.core112R1.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.gameserver.threading.GameServerKeeperAliveTask;
import fr.badblock.game.core112R1.listeners.packets.SkullExploitListener;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.game.core112R1.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.VanishTeleportListener;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.PlayerGameInitEvent;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.events.api.SpectatorJoinEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.threading.TaskManager;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

/**
 * Listener servant � remplac� la classe CraftPlayer par GameBadblockPlayer et � demander � Ladder les informations joueur.
 * @author LeLanN
 */
public class LoginListener extends BadListener {

	public static List<String> l = new ArrayList<>();

	@EventHandler(priority=EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent e) {
		if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
			if (e.getResult().equals(Result.KICK_FULL) || BukkitUtils.getPlayers().size() >= Bukkit.getMaxPlayers()) {
				if (!VanishTeleportListener.time.containsKey(e.getPlayer().getName().toLowerCase()) || VanishTeleportListener.time.get(e.getPlayer().getName().toLowerCase()) < System.currentTimeMillis()) 
					e.disallow(Result.KICK_FULL, "§cCe serveur est plein.");
				else e.disallow(Result.ALLOWED, "");
			}
		}
		if (!GameAPI.isJoinable() && !GamePlugin.getInstance().getGameServer().getPlayers().containsKey(e.getPlayer().getName().toLowerCase())) {
			if (!VanishTeleportListener.time.containsKey(e.getPlayer().getName().toLowerCase()) || VanishTeleportListener.time.get(e.getPlayer().getName().toLowerCase()) < System.currentTimeMillis()) {
				GameAPI.getAPI().getGameServer().keepAlive();
				e.disallow(Result.KICK_FULL, "§cCette partie est en cours.");
			}
		}
		if(GameAPI.getAPI().getWhitelistStatus() && !GameAPI.getAPI().isWhitelisted(e.getPlayer().getName())){
			e.setResult(Result.KICK_WHITELIST); return;
		}

		Reflector 			  reflector 	= new Reflector(ReflectionUtils.getHandle(e.getPlayer()));
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());
		GameBadblockPlayer    player        = null;
		try {
			player = new GameBadblockPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) reflector.getReflected(), (GameOfflinePlayer) offlinePlayer);
			reflector.setFieldValue("bukkitEntity", player);
		} catch (Exception exception) {
			System.out.println("Impossible de modifier la classe du joueur : ");
			exception.printStackTrace();
		}
		/*
		final BadblockPlayer bbPlayer = player;
		try {
			TaskManager.runTaskAsync(new Runnable()
			{
				@Override
				public void run() {
					Property property = MojangAPI.getSkinPropertyObject(e.getPlayer().getName());
					TaskManager.runTask(new Runnable()
					{
						@Override
						public void run() {
							SkinFactory.applySkin(e.getPlayer(), property);
							Bukkit.getPluginManager().callEvent(new PlayerDataChangedEvent(bbPlayer));
						}
					});
				}
			});
			//String[] props = MojangAPI.getSkinProperty(e.getPlayer().getName());
			//player.setTextureProperty(props[0], props[1]);
		} catch (Exception exception) {
			System.out.println("Impossible de mettre le skin au joueur : ");
			exception.printStackTrace();
		}*/
	}

	@EventHandler
	public void onDataReceived(PlayerLoadedEvent e)
	{
		if(GameAPI.getAPI().getRunType() != RunType.DEV || GameServerKeeperAliveTask.isOpenToStaff())
			return;

		if(!e.getPlayer().hasPermission("devserver")) {
			e.getPlayer().kickPlayer("Serveur dév non ouvert au staff !");
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		if (player.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
			GameBadblockPlayer p = (GameBadblockPlayer) player;
			if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && !p.isGhostConnect()) {
				Inventory inventory = event.getInventory();
				if (inventory != null && inventory.getName() != null && inventory.getSize() < 9 * 2) {
					manageRunningJoin(player);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void whenLoaded(PlayerLoadedEvent event) {
		GameBadblockPlayer player = (GameBadblockPlayer) event.getPlayer();
		
		for (Player pla : Bukkit.getOnlinePlayers())
		{
			if (pla.getName().equalsIgnoreCase(player.getName()))
			{
				continue;
			}
			
			GameBadblockPlayer otherPlayer = (GameBadblockPlayer) pla;
			if (!otherPlayer.isGhostConnect())
			{
				continue;
			}
			
			player.hidePlayer(pla);
			pla.showPlayer(player);
		}
		
		if (player.isGhostConnect() || (VanishTeleportListener.time.containsKey(player.getName().toLowerCase()) && VanishTeleportListener.time.get(player.getName().toLowerCase()) > System.currentTimeMillis())) {
			player.setVisible(false, pl -> !pl.hasPermission("gameapi.ghost"));
			for (Player plo : Bukkit.getOnlinePlayers())
			{
				if (plo.getName().equalsIgnoreCase(player.getName()))
				{
					continue;
				}
				plo.hidePlayer(player);
				player.showPlayer(plo);
			}
		}
	}

	private HashMap<Player, Integer> lastBookTick = new HashMap<>();

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GameBadblockPlayer p = (GameBadblockPlayer) e.getPlayer();
		
		p.setHasJoined(true);
		
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());
		
		if (VanishTeleportListener.time.containsKey(p.getName().toLowerCase()) && VanishTeleportListener.time.get(p.getName().toLowerCase()) > System.currentTimeMillis()) {
			p.setGhostConnect(true);
			e.setJoinMessage("");
			VanishTeleportListener.manage(p, VanishTeleportListener.splitters.get(p.getName().toLowerCase()));
		}else if(offlinePlayer != null){
			p.changePlayerDimension(offlinePlayer.getFalseDimension());
			p.showCustomObjective(offlinePlayer.getCustomObjective());

			GamePlugin.getInstance().getGameServer().getPlayers().remove(offlinePlayer.getName().toLowerCase());
			GamePlugin.getInstance().getGameServer().getSavedPlayers().remove(p.getPlayerData());

		} else if(GameAPI.getAPI().getGameServer().getGameState() == GameState.RUNNING){
			p.setVisible(false, player -> !player.getBadblockMode().equals(BadblockMode.SPECTATOR));
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
			p.sendTranslatedMessage("game.closetheinventorytoplaywiththedefaultkit");
			if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && BukkitUtils.getPlayers().size() <= Bukkit.getMaxPlayers() && !p.isGhostConnect()) {
				TaskManager.runTaskLater(new Runnable() {
					@Override
					public void run() {
						JoinItems joinItems = GameAPI.getAPI().getJoinItems();
						if (joinItems.getKits().isEmpty()) {
							// Manage
							manageRunningJoin(p);
							return;
						}
						CustomInventory inventory = GameAPI.getAPI().createCustomInventory(joinItems.getKits().size() / 9, GameAPI.i18n().get(p.getPlayerData().getLocale(), "joinitems.kit.inventoryName")[0]);

						int slot = 0;

						for(PlayerKit kit : joinItems.getKits()){
							if(kit != null){
								inventory.addClickableItem(slot, kit.getKitItem(p));
							}
							slot++;
						}

						inventory.openInventory(p);
						TaskManager.runTaskLater(new Runnable() {
							@Override
							public void run() {
								if (!p.isOnline()) return;
								if (p.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
									p.closeInventory();
								}
							}
						}, 20 * 15);
					}
				}, 1);
			}
		} else if(GameAPI.getAPI().getGameServer().getGameState() == GameState.FINISHED){
			p.setVisible(false, player -> !player.getBadblockMode().equals(BadblockMode.SPECTATOR));
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
		}
		
		reSendPlayerJoined(p);
		
	}

	public void reSendPlayerJoined(BadblockPlayer player) {
		if (GameAPI.getAPI().getRunType().equals(RunType.LOBBY))
		{
			return;
		}
		// Test de renvois du packet
		final String playerName = player.getName();
		TaskManager.runTaskLater(new Runnable() {
			@Override
			public void run() {
				Player p = Bukkit.getPlayer(playerName);
				if (p == null) return;
				EntityPlayer[] e = new EntityPlayer[]{((CraftPlayer) p).getHandle()};
				PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, e);
				for (BadblockPlayer online : BukkitUtils.getAllPlayers()) {
					if (online.getUniqueId().equals(p.getUniqueId())) continue;
					EntityPlayer ep = ((GameBadblockPlayer) online).getHandle();
					if (ep.playerConnection != null && !ep.playerConnection.isDisconnected()) ep.playerConnection.sendPacket(packet);
				}
			}
		}, 20);
	}

	public static void manageRunningJoin(BadblockPlayer player) {
		GameBadblockPlayer p = (GameBadblockPlayer) player;
		if (p.isGhostConnect()) return;
		GameMessages.joinMessage(GameAPI.getGameName(), player.getName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).broadcast();
		player.setBadblockMode(BadblockMode.PLAYER);
		if (!GameAPI.getAPI().getTeams().isEmpty()) {
			BadblockTeam betterTeam = null;
			for (BadblockTeam team : GameAPI.getAPI().getTeams()) {
				if (team.isDead()) continue;
				if (team.playersCurrentlyOnline() < team.getMaxPlayers() && team.playersCurrentlyOnline() > 0) {
					if (betterTeam == null || (betterTeam != null && betterTeam.playersCurrentlyOnline() > team.playersCurrentlyOnline()))
						betterTeam = team;
				}
			}
			if (betterTeam == null) player.kickPlayer("Unable to push you in a team.");
			else betterTeam.joinTeam(player, JoinReason.REBALANCING);
		}
		Bukkit.getPluginManager().callEvent(new PlayerGameInitEvent(player));
		player.setVisible(true);
	}

	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
	public void onInteract(PlayerInteractEvent e)
	{
		if (SkullExploitListener.isExploit(e.getItem()))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
		}
	}

	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
	public void onJoine(PlayerJoinEvent e)
	{
		for (ItemStack item : e.getPlayer().getInventory().getContents()) {
			if (SkullExploitListener.isExploit(item)) {
				System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onItemDrop(PlayerDropItemEvent e)
	{
		if (SkullExploitListener.isExploit(e.getItemDrop().getItemStack()))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onItemDrop(InventoryClickEvent e)
	{
		if ((e.getWhoClicked().getType() == EntityType.PLAYER) && (SkullExploitListener.isExploit(e.getCurrentItem())))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getWhoClicked().getName());
		}
	}

	@EventHandler
	public void onLoad(ChunkLoadEvent e)
	{
		SkullExploitListener.cleanChunk(e.getChunk());
	}

	@EventHandler
	public void onLoad2(WorldLoadEvent e)
	{
		for (Chunk c : e.getWorld().getLoadedChunks()) {
			SkullExploitListener.cleanChunk(c);
		}
	}

}
