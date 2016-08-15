package fr.badblock.api.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.listeners.minigame.VoteListener;
import fr.badblock.api.utils.bukkit.ChatUtils;

public abstract class BPlayerBoard {
	protected Scoreboard board;
	protected Objective objective;

	protected BPlayer player;

	public BPlayerBoard(BPlayer player){
		this.player = player;

		board 	  = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("playerBoard", "dummy");

		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		for(int i=1;i<=15;i++){
			board.registerNewTeam(Identifiers.getLineIdentifier(i)).addEntry(Identifiers.getLineIdentifier(i));
		}

		update();
		show();
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

	public abstract void setTime(String time);
	public abstract void update();

	public void setLine(int line, String text){
		if(line < 1 || line > 15) return;

		text = ChatUtils.colorReplace(text);
		String identifier = Identifiers.getLineIdentifier(line);
		Team team = board.getTeam(identifier);

		String prefix = text.substring(0, text.length() <= 16 ? text.length() : 16);
		String suffix = "";


		if(prefix.endsWith("§")){
			prefix.substring(0, prefix.length() - 1);

			suffix += "§";
		} else {
			String lastColor = lastColor(prefix);
			suffix = lastColor == null ? "" : lastColor.toString();
		}

		suffix += (text.length() > 16 ? text.substring(16) : "");

		if(suffix.length() > 16){
			suffix = suffix.substring(0,  16);
		}

		team.setPrefix(prefix);
		team.setSuffix(suffix);

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

	private static String lastColor(String str){
		str = ChatUtils.colorReplace(str);
		ChatColor last = null;
		ChatColor encod = null;

		for(int i=0;i<str.length();i++){
			if(str.charAt(i) == '§'){
				ChatColor color = ChatColor.getByChar(str.charAt(i + 1));

				last = color;

				if(color.isColor() && str.length() > i + 2){
					if(str.charAt(i + 2) == '§'){
						encod = ChatColor.getByChar(str.charAt(i + 3));

						i += 2;
					} else encod = null;
				} else encod = null;

				i++;

				if(color != null){
					last = color;
				}
			}
		}

		return last == null ? "" : (last + "" + (encod == null ? "" : encod.toString()));
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
