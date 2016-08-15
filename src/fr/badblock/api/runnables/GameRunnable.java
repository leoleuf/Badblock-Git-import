package fr.badblock.api.runnables;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.Team;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.listeners.minigame.PingListener;
import fr.badblock.api.scoreboard.BSpectatorBoard;
import fr.badblock.api.utils.bukkit.title.Title;
import fr.badblock.api.utils.maths.TimeUtils;
import fr.badblock.api.utils.maths.TimeUtils.TimeType;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;

public abstract class GameRunnable extends BRunnable{
	private int timeModifier;
	
	protected Title winTitle = new Title("%green%Félicitations !", "%green%Vous avez gagné !", 80);
	protected Title looseTitle = new Title("%red%Dommage !", "%red%Retentez votre chance !", 80);

	public GameRunnable(int time, int timeModifier){
		super(20L);
		
		this.time = time;
		this.timeModifier = timeModifier;
	}

	public void kick(){
		PingListener.kick();
	}
	
	public void win(Team team){
		cancel();
		
		MJPlugin.getInstance().setStatus(GameStatus.ENDING);
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(player.getTeam() == team.getType()){
				player.win();
				player.sendTitle(winTitle);
			} else {
				player.sendTitle(looseTitle);
				player.loose();
			}
		}
		kick();
	}
	public void win(BPlayer winner){
		cancel();
		MJPlugin.getInstance().setStatus(GameStatus.ENDING);
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(player.getUniqueId().equals(winner.getUniqueId())){
				player.win();
				player.sendTitle(winTitle);
			} else {
				player.sendTitle(looseTitle);
				player.loose();
			}
		}
		kick();
	}
	@Override
	public void run(){
		time += timeModifier;
		BPlayersManager.getInstance().update(TimeUtils.parseTime(time, TimeType.SECOND));
		BSpectatorBoard.getInstance().update(TimeUtils.parseTime(time, TimeType.SECOND));
		
		if(TeamsManager.getInstance() != null){
			TeamsManager.getInstance().update();
			Team winner = TeamsManager.getInstance().getWinners();
			if(winner != null){
				cancel();
				win(winner);
				return;
			}
		} else {
			BPlayer winner = BPlayersManager.getInstance().getWinner();
			if(winner != null){
				cancel();
				win(winner);
				return;
			}
		}
		if(timeModifier < 0 && time == 0 && TeamsManager.enabled()){
			int max = 0;
			Team win = null;
			for(Team team : TeamsManager.getInstance().getTeams()){
				if(team.getScore() >= max){
					win = team;
					max = team.getScore();
				}
			}
			cancel();
			win(win);
		}
		update();
	}
	
	public abstract void update();
	
	@Override
	public void start(){
		SignsRunnable.getInstance().sendStatus(ServerStatus.RUNNING);
		super.start();
	}
}
