package fr.badblock.api.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.listeners.minigame.VoteListener;
import fr.badblock.api.utils.bukkit.ChatUtils;

public abstract class BScoreboard {
	protected Scoreboard board;
	protected Objective objective;

	protected BPlayer player;
	
	public BScoreboard(BPlayer player){
		this.player = player;
		
		board 	  = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("spectator", "dummy");
		
		for(int i=1;i<=15;i++){
			board.registerNewTeam(Identifiers.getLineIdentifier(i));
		}

		update();
	}
	
	public void show(){
		MJPlugin plugin = MJPlugin.getInstance();
		if(plugin.enableVote() && (plugin.getStatus() == GameStatus.WAITING_PLAYERS || plugin.getStatus() == GameStatus.STARTING)){
			VoteListener.showScoreboard(player);
		} else {
			if(player.getPlayer() != null)
				player.getPlayer().setScoreboard(board);
		}
	}
	
	public abstract void update();
	
	public void setLine(int line, String text){
		if(line < 1 || line > 15) return;
		
		String identifier = Identifiers.getLineIdentifier(line);
		
		Team team = board.getTeam(identifier);
		
		team.setPrefix(text.substring(0, text.length() <= 16 ? text.length() : 16));
		team.setPrefix(text.length() > 16 ? text.substring(16) : "");

		objective.getScore(identifier).setScore(line);
	}
	
	public void removeLine(int line){
		if(line < 1 || line > 15) return;

		board.resetScores(Identifiers.getLineIdentifier(line));
	}
	
	public void reset(){
		for(int i=1;i<=15;i++){
			removeLine(i);
		}
	}
	
	public void setDisplayName(String displayName){
		objective.setDisplayName(ChatUtils.colorReplace(displayName));
	}
	
	enum Identifiers {
		line1('0', 1),
		line2('1', 2),
		line3('2', 3),
		line4('3', 4),
		line5('4', 5),
		line6('5', 6),
		line7('6', 7),
		line8('7', 8),
		line9('8', 9),
		line10('9', 10),
		line11('a', 11),
		line12('b', 12),
		line13('c', 13),
		line14('d', 14),
		line15('e', 15);

		private static final Map<Integer, Character>	lookup	= new HashMap<Integer, Character>();

		static {
			for (Identifiers s : Identifiers.values())
				lookup.put(s.getLine(), s.getChar());
		}

		private Character								id;
		private Integer									line;

		Identifiers(Character id, int line) {
			this.id = id;
			this.line = line;
		}

		public static String getLineIdentifier(int line) {
			return ChatColor.getByChar(lookup.get(line).charValue()).toString() + ChatColor.RESET.toString();
		}

		public Character getChar() {
			return id;
		}

		public int getLine() {
			return line;
		}
	}
}
