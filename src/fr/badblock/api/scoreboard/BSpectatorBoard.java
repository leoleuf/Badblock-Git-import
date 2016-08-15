package fr.badblock.api.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.badblock.api.BPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.Team.TeamType;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;

public class BSpectatorBoard {
	private static BSpectatorBoard instance;
	public static BSpectatorBoard getInstance(){
		return instance;
	}

	private Scoreboard board;
	private Objective objective;
	private Team team;

	private List<Score> informations = new ArrayList<Score>();
	private List<UUID> players = new ArrayList<UUID>();
	private String blank = " ";

	public BSpectatorBoard(){
		if(instance == null) instance = this;
		generate();
	}

	protected BPlayer player;

	public void show(){
		player.getPlayer().setScoreboard(board);
	}
	//	public void addStat(String stat, String displayName){
	//		statsGetter.put(stat, displayName);
	//		Score score = objective.getScore(ChatUtils.colorReplace(displayName + ": " + (int)player.getStatValue(stat)));
	//		score.setScore(stats.size() * -1 - 2);
	//
	//		stats.put(stat, score);
	//	}
	public void add(String information){
		information = ChatUtils.colorReplace(information);
		if(information.isEmpty()){
			blank += " ";
			addScore(blank);
		} else if(information.length() > 16){
			String prefix = information.substring(0, information.length() - 16);
			String other = information.substring(information.length() - 16, information.length());

			Team team = board.registerNewTeam(prefix);
			team.addEntry(other);
			team.setPrefix(prefix);

			addScore(other);
		} else {
			addScore(information);
		}
	}
	private void addScore(String information){
		Score score = objective.getScore(information);
		score.setScore(informations.size() * -1 - 1);
		informations.add(score);
	}
	public void setTime(String time){
		objective.setDisplayName(ChatUtils.colorReplace("%dred%[%red%" + BPlugin.getInstance().getGameName() + "%dred%] %green%" + time));
	}
	public void generate(){
		blank = " ";
		informations.clear();
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("spectator", "dummy");
		objective.setDisplayName(ChatUtils.colorReplace("%dred%[%red%" + BPlugin.getInstance().getGameName() + "%dred%] %green%00m00s"));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		//		space = objective.getScore(" ");
		//		space.setScore(1);
		if(TeamsManager.enabled())
			for(TeamType type : TeamType.values()){
				if(type.getTeam() != null){
					//					int score = type.getTeam().getScore();
					//					Score teamScore = objective.getScore(ChatUtils.colorReplace(type.getColor() + type.getFrench()));
					//					teamScore.setScore(score);
					//					
					//					teamsScore.put(type, teamScore);
					Team team = board.registerNewTeam(type.getName());
					team.setCanSeeFriendlyInvisibles(false);
					team.setPrefix(ChatUtils.colorReplace(type.getColor()));
					for(UUID player : type.getTeam().getPlayers()){
						BPlayer p = BPlayersManager.getInstance().getPlayer(player);
						if(p != null && p.getPlayer() != null)
							team.addEntry(p.getPlayer().getName());
					}
				}
			}
		add("");
		add("%red%Joueurs : %green%" + BPlayersManager.getInstance().countPlayers());
		add("%red%Spectateurs : %green%" + BPlayersManager.getInstance().countSpectators());
		add("");
		add("%dred%/%red%badblock");

		team = board.registerNewTeam("spects");
		team.setCanSeeFriendlyInvisibles(true);
		team.setAllowFriendlyFire(false);

		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(player != null && player.getPlayer() != null && player.isSpectator()){
				player.getPlayer().setScoreboard(board);
				team.addEntry(player.getPlayer().getName());
			}
		}
	}
	public void update(String time){
		objective.setDisplayName(ChatUtils.colorReplace("%dred%[%red%" + BPlugin.getInstance().getGameName() + "%dred%]%green% " + time));
		//		int max = 0;
		//		if(TeamsManager.enabled())
		//			for(TeamType type : teamsScore.keySet()){
		//				if(type.getTeam() == null){
		//					board.resetScores(ChatUtils.colorReplace(type.getColor() + type.getFrench()));
		//				} else {
		//					int score = type.getTeam().getScore();
		//					teamsScore.get(type).setScore(score);
		//					if(score > max)
		//						max = score;
		//				}
		//			}
		//		space.setScore(max + 1);
	}
	public void addSpectator(BPlayer player){
		addSpectator(player.getPlayer());
	}
	public void addSpectator(Player player){
		team.addEntry(player.getName());
		players.add(player.getUniqueId());
		player.setScoreboard(board);

		generate();
	}
}
