package fr.badblock.game.core18R3.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.ingamedata.GameOfflinePlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.SpectatorJoinEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.EntityPlayer;

/**
 * Listener servant � remplac� la classe CraftPlayer par GameBadblockPlayer et � demander � Ladder les informations joueur.
 * @author LeLanN
 */
public class LoginListener extends BadListener {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e){
		if(GameAPI.getAPI().getWhitelistStatus() && !GameAPI.getAPI().isWhitelisted(e.getPlayer().getName())){
			e.setResult(Result.KICK_WHITELIST); return;
		}

		Reflector 			  reflector 	= new Reflector(ReflectionUtils.getHandle(e.getPlayer()));
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());
		GameBadblockPlayer    player;

		try {
			player = new GameBadblockPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) reflector.getReflected(), (GameOfflinePlayer) offlinePlayer);
			reflector.setFieldValue("bukkitEntity", player);
		} catch (Exception exception) {
			System.out.println("Impossible de modifier la classe du joueur : ");
			exception.printStackTrace();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GameBadblockPlayer p = (GameBadblockPlayer) e.getPlayer();

		p.loadInjector();
		p.setHasJoined(true);

		if(GamePlugin.EMPTY_VERSION) return;

		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());

		if(offlinePlayer != null){
			p.changePlayerDimension(offlinePlayer.getFalseDimension());
			p.showCustomObjective(offlinePlayer.getCustomObjective());

			GamePlugin.getInstance().getGameServer().getPlayers().remove(offlinePlayer.getName().toLowerCase());
			GamePlugin.getInstance().getGameServer().getSavedPlayers().remove(offlinePlayer.getName().toLowerCase());

		} else if(GameAPI.getAPI().getGameServer().getGameState() != GameState.WAITING){
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
		}

		new BukkitRunnable(){
			@Override
			public void run(){

				for(Player player : Bukkit.getOnlinePlayers()){
					GameBadblockPlayer bp = (GameBadblockPlayer) player;
					/*if(bp.isDisguised()){
						bp.getDisguiseEntity().show(p);
					} else */
					if(!bp.isVisible() && bp.getVisiblePredicate().test(p)){
						p.hidePlayer(bp);
					}
					/*if(bp.inGameData(CommandInGameData.class).vanish && !p.hasPermission(GamePermission.BMODERATOR)){
						p.hidePlayer(bp);
					}*/
				}
				if(GameAPI.getAPI().getGameServer().getGameState() == GameState.WAITING){
					if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
						// Booster
						List<String> players = new ArrayList<String>();
						double xp = 0;
						double badcoins = 0;
						PlayerBooster playerBoosterZ = null;
						for (PlayerBooster playerBoosterr : p.getPlayerData().getBoosters())
							if (playerBoosterr.isEnabled() && !playerBoosterr.isExpired()) playerBoosterZ = playerBoosterr;
						boolean hasBoosterEnabled = playerBoosterZ != null;
						for (Player player : Bukkit.getOnlinePlayers()) {
							BadblockPlayer bbPlayer = (BadblockPlayer) player;
							PlayerBooster playerBooster = null;
							for (PlayerBooster playerBoosterr : bbPlayer.getPlayerData().getBoosters())
								if (playerBoosterr.isEnabled() && !playerBoosterr.isExpired()) playerBooster = playerBoosterr;
							if (playerBooster != null) {
								xp += playerBooster.getBooster().getXpMultiplier();
								badcoins += playerBooster.getBooster().getCoinsMultiplier();
							}
						}
						if (xp == 0) xp = 1;
						if (badcoins == 0) badcoins = 1;
						String o = "[";
						Iterator<String> iterator = players.iterator();
						while (iterator.hasNext()) {
							String oo = iterator.next();
							o += oo + (iterator.hasNext() ? ", " : "");
						}
						o += "]";
						if (xp > 1 || badcoins > 1) {
							final double xpp = xp;
							final double badcoinss = badcoins;
							final String oo = o;
							if (hasBoosterEnabled) {
								Bukkit.getOnlinePlayers().forEach(po -> {
									BadblockPlayer pob = (BadblockPlayer) po;
									pob.sendTranslatedMessage("booster.load", Double.toString(xpp), Double.toString(badcoinss), p.getName(), oo);
									pob.playSound(Sound.LEVEL_UP);
								});
							}else{
								p.sendTranslatedMessage("booster.resume", Double.toString(xpp), Double.toString(badcoinss), p.getName(), oo);
								p.playSound(Sound.LEVEL_UP);
							}
						}
					}
				}

			}
		}.runTaskLater(GameAPI.getAPI(), 10L);
	}
}
