package fr.badblock.ladder.plugins.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.badblock.ladder.api.events.EventHandler;
import fr.badblock.ladder.api.events.Listener;
import fr.badblock.ladder.api.events.all.MatchmakingJoinEvent;
import fr.badblock.ladder.api.events.all.MatchmakingServerEvent;
import fr.badblock.ladder.api.events.all.PlayerJoinEvent;
import fr.badblock.ladder.api.plugins.Plugin;
import fr.badblock.ladder.plugins.matchmaking.MatchServer;
import fr.badblock.ladder.plugins.utils.commands.CommandAdminChat;
import fr.badblock.ladder.plugins.utils.commands.CommandChatStaff;
import fr.badblock.ladder.plugins.utils.commands.CommandPic;
import fr.badblock.ladder.plugins.utils.commands.CommandPlugins;
import fr.badblock.ladder.plugins.utils.commands.CommandVersion;
import lombok.Getter;

public class LadderUtils extends Plugin implements Listener {
	@Getter
	private int					  picBest,
								  picBestToday;
	
	private boolean				  picSaved;
	
	private List<MatchServer> 	  matchServers;
	
	@Override
	public void onEnable(){
		getLadder().getPluginsManager().registerCommand(this, new CommandPlugins());
		getLadder().getPluginsManager().registerCommand(this, new CommandVersion());
		getLadder().getPluginsManager().registerCommand(this, new CommandPic(this));
		getLadder().getPluginsManager().registerCommand(this, new CommandChatStaff());
		getLadder().getPluginsManager().registerCommand(this, new CommandAdminChat());

		picBest 	 = getConfig().getInt("pic.best");
		picBestToday = getConfig().getInt("pic." + getDate());
		matchServers = new ArrayList<>();
		
		/**if(getConfig().getSection("matchgames") == null){
			getConfig().set("matchgames.example", 10);
		}
		
		if(getConfig().getSection("matchgames") != null && !getConfig().getSection("matchgames").getKeys().isEmpty()){
			for(String key : getConfig().getSection("matchgames").getKeys()){
				MatchServer server = new MatchServer(key, getConfig().getInt("matchgames." + key));
				matchServers.add(server);
				
				server.start();
			}
		}*/
		
		getLadder().getPluginsManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerJoin(MatchmakingJoinEvent e){
		for(MatchServer server : matchServers)
			server.onPlayerJoinMatchmaking(e);
	}
	
	@EventHandler
	public void onServerKeepalive(MatchmakingServerEvent e){
		for(MatchServer server : matchServers)
			server.onServerKeepalive(e);
	}
	
	@Override
	public void onDisable(){
		for(MatchServer server : matchServers)
			server.end();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		int players = getLadder().getOnlinePlayers().size();
		
		if(picBestToday - 100 > players && !picSaved){
			picSaved = true;
			saveConfig();
		}
		
		if(picBestToday < players){
			picSaved 	 = false;
			picBestToday = players;
			
			if(picBest < players){
				picBest = players;
				getConfig().set("pic.best", players);
			}
			
			getConfig().set("pic." + getDate(), players);
		}
	}
	
	private String getDate(){
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = dateFormat.format(date);

		return dateString;
	}
}
