package fr.badblock.ladder.api.entities;

import java.util.List;
import java.util.UUID;

import fr.badblock.ladder.api.chat.ChatMessage;

public interface Player extends CommandSender, Connection, OfflinePlayer {
	public UUID 	   getUniqueId();
	
	public void 	   connect(Bukkit server);
	public void 	   disconnect(String... reason);
	public void		   send(ChatMessage message);
	public void		   sendTab(String... tab);
	
	public Bukkit      getBukkitServer();
	public BungeeCord  getBungeeServer();
	
	public void		   sendToBungee(String... dataPart);
	public void		   sendToBukkit(String... dataPart);
	
	public void		   canJoinHimself(boolean can);
	public boolean     canJoinHimself();
	
	public List<UUID>  getPlayersWithHim();
	public void		   setPlayersWithHim(List<UUID> players);
	
	public String	   getRequestedGame();
	public void		   setRequestedGame(String game);
}