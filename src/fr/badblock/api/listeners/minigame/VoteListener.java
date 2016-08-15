package fr.badblock.api.listeners.minigame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.badblock.api.BPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.Team.TeamType;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class VoteListener implements Listener{
	private static final String TITLE = ChatUtils.colorReplace("&4Vote pour une map :");
	private static Inventory inventory = null;

	private static Map<String, Integer> votes = new HashMap<String, Integer>();
	private static Map<String, Score> score = new HashMap<String, Score>();
	private static Map<TeamType, Team> teams = new HashMap<TeamType, Team>();

	private static Scoreboard board;
	private static Objective objective;

	public static void showScoreboard(BPlayer player){
		if(player.getTeam() != null){
			Team t = teams.get(player.getTeam());
			if(t != null && !t.hasEntry(player.getPlayer().getName())){
				t.addEntry(player.getPlayer().getName());
			}
		}
		player.getPlayer().setScoreboard(board);
	}
	
	public static void generateInventory(List<String> maps){
		int lines = maps.size() / 9 + (maps.size() % 9 == 0 ? 0 : 1);

		if(lines == 0) lines++;
		else if(lines > 6) lines = 6;

		inventory = Bukkit.createInventory(null, 9 * lines, TITLE);

		for(int i=0;i<maps.size() && i<9*6;i++){
			ItemStack is = new ItemStack(Material.PAPER, 1);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatUtils.colorReplace("%gold%" + maps.get(i)));
			im.setLore(Arrays.asList(new String[]{"", ChatUtils.colorReplace("%red%Vote pour cette map en cliquant")}));
			is.setItemMeta(im);

			inventory.setItem(i, is);
		}

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();

		objective = board.registerNewObjective("votes", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatUtils.colorReplace("%dred%[%green%" + BPlugin.getInstance().getGameName() + "%dred%] Vote"));

		for(String map : maps){
			Score sc = objective.getScore(ChatUtils.colorReplace("%yellow%" + map)); sc.setScore(0);
			score.put(map, sc);
			votes.put(map,  0);
		}
		if(TeamsManager.enabled())
			for(TeamType type : TeamType.values()){
				if(type.getTeam() != null){
					Team team = board.registerNewTeam(type.getName());
					team.setPrefix(ChatUtils.colorReplace(type.getColor()));

					teams.put(type, team);
				}
			}
		for(Player player : Bukkit.getOnlinePlayers()){
			player.setScoreboard(board);
		}

	}
	public static void updateScoreboard(){
		for(String key : score.keySet()){
			int sc = votes.get(key);
			score.get(key).setScore(sc);
		}
	}
	public static void openInventory(Player p){
		p.openInventory(inventory);
	}
	public static void vote(Player p, String map){
		BPlayer player = BPlayersManager.getInstance().getPlayer(p);
		if(player == null) return;

		if(player.getVote() != null){
			int nbr = votes.get(player.getVote());
			nbr--; votes.put(player.getVote(), nbr);
		}
		if(map != null){
			int nbr = votes.get(map); nbr++;
			votes.put(map, nbr);
		}
		player.setVote(map);

		updateScoreboard();
	}
	
	public static void disconnect(Player p){
		BPlayer player = BPlayersManager.getInstance().getPlayer(p);
		if(player == null) return;

		if(player.getVote() != null){
			int nbr = votes.get(player.getVote());
			nbr--; votes.put(player.getVote(), nbr);
		}
		player.setVote(null);

		updateScoreboard();
	}
	
	public static int getVoteNumber(String key){
		return votes.get(key);
	}
	public static String getWinner(){
		String winner = null;
		int vote = 0;

		for(String key : votes.keySet()){
			int v = votes.get(key);
			if(v > vote || winner == null){
				winner = key;
				vote = v;
			}
		}
		return winner;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!e.getInventory().getTitle().equals(TITLE) || !(e.getWhoClicked() instanceof Player))
			return;
		if(e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;
		Player p = (Player) e.getWhoClicked();
		e.setCancelled(true);
		p.closeInventory();

		vote(p, ChatUtils.colorDelete(e.getCurrentItem().getItemMeta().getDisplayName()));
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e){
		disconnect(e.getPlayer());
	}
}