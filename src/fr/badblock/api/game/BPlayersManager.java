	package fr.badblock.api.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.scoreboard.BSpectatorBoard;

public class BPlayersManager {
	private static BPlayersManager instance;
	private int lastCount = 0;

	public static BPlayersManager getInstance() {
		if(instance == null) init();

		return instance;
	}
	public static void init() {
		instance = new BPlayersManager();
	}

	private Map<UUID, BPlayer> players;


	public BPlayersManager(){
		players = new HashMap<UUID, BPlayer>();
	}
	public BPlayer disconnect(Player p) {
		BPlayer player = getPlayer(p);
		if(player != null){
			player.save();
			if(player.getTeam() != null){
				player.getTeam().getTeam().getPlayers().remove(player.getUniqueId());
				player.setTeam(null);
			}
			players.remove(p.getUniqueId());
		}

		return player;
	}

	public BPlayer connect(Player p, boolean spectator) {
		BPlayer badblockPlayer = MJPlugin.getInstance().createPlayer(p, spectator);
		
		if(!badblockPlayer.isSpectator())
			badblockPlayer.setScoreboard();
		
		players.put(p.getUniqueId(), badblockPlayer);

		if(!spectator){
			update("00m00s", true);
		}

		return badblockPlayer;
	}

	public BPlayer getPlayer(UUID uniqueId) {
		return players.get(uniqueId);
	}
	public BPlayer getPlayer(String userName) {
		Player p = Bukkit.getPlayer(userName);
		if(p != null)
			return players.get(p.getUniqueId());
		else return null;
	}
	public BPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}

	public BPlayer getPlayer(Entity entity) {
		if (entity instanceof Player)
			return getPlayer((Player) entity);
		else if (entity instanceof Projectile)
			return getPlayer((Entity) ((Projectile) entity).getShooter());
		else
			return null;
	}
	
	public Map<UUID, BPlayer> getMap(){
		return players;
	}
	
	public Collection<BPlayer> getPlayers(){
		return players.values();
	}
	public int countPlayers(){
		int count = 0;
		for(BPlayer p : players.values()){
			if(!p.isSpectator()){
				count++;
			}
		}

		return count;
	}
	public int countSpectators(){
		return players.size() - countPlayers();
	}
	public void update(String time){
		int count = countPlayers();
		boolean hasChanged = false;
		if(lastCount != count){
			hasChanged = true;
			lastCount = count;
		}
		update(time, hasChanged);
	}
	public void update(String time, boolean hasChanged){
		if(hasChanged) BSpectatorBoard.getInstance().generate();
		BSpectatorBoard.getInstance().update(time);

		for(BPlayer player : getPlayers()){
			if(!player.isSpectator())
				player.update(time, hasChanged);
		}
	}
	public BPlayer getWinner(){
		int count = 0;
		BPlayer player = null;
		for(BPlayer p : players.values()){
			if(!p.isSpectator()){
				player = p;
				count++;
			}
		}

		if(count == 1) return player;
		else return null;
	}

}
