package fr.badblock.api.listeners.minigame;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.commands.HealCommand;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;
import fr.badblock.api.utils.bukkit.title.Title;

public class ConnectionListener implements Listener {
	private MJPlugin plugin;

	public ConnectionListener(MJPlugin plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		VoteListener.disconnect(e.getPlayer());
		
		BPlayersManager.getInstance().disconnect(e.getPlayer());
		e.setQuitMessage(null);
		
		if(Bukkit.getOnlinePlayers().size() == 0 && MJPlugin.getInstance().getStatus() != GameStatus.WAITING_PLAYERS){
			Bukkit.shutdown();
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e){
		if(e.getResult() == Result.KICK_FULL && e.getPlayer().hasPermission("game.staff")){
			e.setResult(Result.ALLOWED);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		HealCommand.heal(e.getPlayer());
		boolean spectator = plugin.mustBeSpectator();
		while(e.getPlayer().getGameMode() != GameMode.SURVIVAL)
			e.getPlayer().setGameMode(GameMode.SURVIVAL);

		new Title(plugin.getWelcomeTitle(), plugin.getWelcomeSubTitle(), 80, 10, 10).send(e.getPlayer());

		BPlayer player = BPlayersManager.getInstance().connect(e.getPlayer(), spectator);
		if(spectator){
			e.setJoinMessage(ChatUtils.colorReplace("%dred%#%red%" + plugin.getGameName() + " : %yellow%" + e.getPlayer().getName() + " nous a rejoint en tant que spectateur !"));
			e.getPlayer().teleport(plugin.getSpectatorSpawn());
			player.setSpectator(true, e.getPlayer());
		} else {
			e.setJoinMessage(ChatUtils.colorReplace("%dred%#%red%" + plugin.getGameName() + " : %yellow%" + e.getPlayer().getName() + " nous a rejoint ! (" + BPlayersManager.getInstance().countPlayers() + "/" + plugin.maxPlayer() + ")"));
			e.getPlayer().teleport(plugin.getSpawn());
			if(TeamsManager.enabled())
				e.getPlayer().getInventory().addItem(ChooseTeamListener.getItem());
			if(plugin.enableKits()){
				int pos = TeamsManager.enabled() ? ((!plugin.enableVote()) ? 8 : 4) : 0;
				e.getPlayer().getInventory().setItem(pos, KitsInventoryListener.getItem());
			}
			if(plugin.enableVote())
				e.getPlayer().getInventory().setItem(8, InteractListener.getPaper());
		}
		plugin.begin();
	}
}
