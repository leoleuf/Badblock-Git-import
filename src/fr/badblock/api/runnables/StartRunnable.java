package fr.badblock.api.runnables;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;

import fr.badblock.api.BPlugin;
import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.game.Team;
import fr.badblock.api.game.TeamsManager;
import fr.badblock.api.utils.bukkit.ChatUtils;
import fr.badblock.api.utils.bukkit.SoundUtils;
import fr.badblock.api.utils.bukkit.title.Title;

public abstract class StartRunnable extends BRunnable{
	public StartRunnable(int time) {
		super(20L);
		this.time = time;
	}

	@Override
	public void run() {
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			player.getPlayer().setLevel(time);
		}
		if(time == 60){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%1 minute %yellow%!");
			new Title("%green%60", true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time == 30){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%30 secondes %yellow%!");
			new Title("%dgreen%30", true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time == 15){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%15 secondes %yellow%!");
			new Title("%aqua%15", true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time == 10){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%10 secondes %yellow%!");
			new Title("%dblue%10", true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time <= 5 && time > 1){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%" + time + " secondes %yellow%!");
			new Title("%red%" + time, true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time == 1){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie dans %red%1 seconde %yellow%!");
			new Title("%dred%1", true).broadcast();
			SoundUtils.broadcastSound(Sound.NOTE_PLING);
		} else if(time == 0){
			ChatUtils.broadcast("%dred%#%red%" + BPlugin.getInstance().getGameName() + " : %yellow%Démarrage de la partie ! Prenez garde !");
			new Title("%dred%GO !", true).broadcast();
			SoundUtils.broadcastSound(Sound.ORB_PICKUP);
			cancel();
			if(TeamsManager.enabled()) completeTeams();
			preparePlayers();
			begin();
		}
		time--;
	}
	public void preparePlayers(){
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			player.prepare();
		}
	}
	public void completeTeams(){
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(player.getTeam() == null){
				for(Team team : TeamsManager.getInstance().getTeams()){
					if(!team.isFull()){
						if(player.getPlayer() != null)
							team.enter(player);
						break;
					}
				}
			}
		}
		
		if(TeamsManager.getInstance().getTeams().size() == 2){
			Team empty = null;
			Team other = null;
			for(Team team : TeamsManager.getInstance().getTeams())
				if(team.isEmpty())
					empty = team;
				else other = team;
			
			if(empty != null && other != null && other.getPlayers().size() > 1)
				for(int i=0;i<other.getPlayers().size() / 2;i++){
					BPlayer player = BPlayersManager.getInstance().getPlayer(other.getPlayers().get(i));
					empty.enter(player);
				}
		}
	}
	public void teleportPlayersToTeamSpawn(){
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			if(player.getTeam() != null && player.getTeam().getTeam() != null){
				player.getPlayer().teleport(player.getTeam().getTeam().getSpawn());
			}
		}
	}
	public void teleportToSpawns(List<Location> spawns){
		int i = 0;
		if(spawns.isEmpty()) return;
		for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
			player.getPlayer().teleport(spawns.get(i));
			
			i++; if(i == spawns.size()) i = 0;
		}
	}
	public abstract void begin();
}
