package fr.badblock.api.game;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.google.common.collect.Maps;

import fr.badblock.api.BPlugin;
import fr.badblock.api.MJPlugin;
import fr.badblock.api.commands.CommandsManager;
import fr.badblock.api.commands.TeamCommand;
import fr.badblock.api.game.Team.TeamType;
import fr.badblock.api.listeners.minigame.ChooseTeamListener;

public class TeamsManager {
	private static TeamsManager instance;
	public static TeamsManager getInstance(){
		return instance;
	}
	public static boolean enabled(){
		return instance != null;
	}
	private int maxPlayerInTeam;
	private Map<TeamType, Team> teams;

	public int getMaxPlayerInTeam(){
		return maxPlayerInTeam;
	}

	public TeamsManager(ConfigurationSection config, int maxPlayerInTeam, Class<? extends Team> clazz, TeamType... enabledTeams){
		instance = this;
		BPlugin.getInstance().registerEvent(new ChooseTeamListener());

		this.maxPlayerInTeam = maxPlayerInTeam;
		this.teams 			 = Maps.newConcurrentMap();
		
		for(TeamType type : enabledTeams){
			Team t = createObjectInstance(clazz);
			t.init(config, "teams." + type.getName().toLowerCase(), type, ((MJPlugin)MJPlugin.getInstance()).initTeams());
			teams.put(type, t);
		}
		
		CommandsManager.getInstance().registerCommand("team", TeamCommand.class, "t");
	}
	@SuppressWarnings("unchecked")
	private static <T> T createObjectInstance(Class<T> c){
		if(c.isEnum() || c.isArray()){ // Could not load an Enum or an Array from an JObject
			return null;
		} else if(c.isInterface()){

		} else {
			try{
				return getConstructor(c).newInstance();
			} catch(Exception e){}
			try {
				Class<?> unsafe = Class.forName("sun.misc.Unsafe");
				Field f = unsafe.getDeclaredField("theUnsafe"); f.setAccessible(true);
				return (T) unsafe.getMethod("allocateInstance", Class.class).invoke(f.get(null), c);
			} catch (Exception e){}
		}
		return null;
	}
	private static <T> Constructor<T> getConstructor(Class<T> c){
		try {
			return c.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		} catch(SecurityException e){
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		}
	}
	
	public void update(){
		for(TeamType type : teams.keySet()){
			Team t = type.getTeam();
			t.update();
			
			if(t.isEmpty()){
				t.loose();
				teams.remove(type);
			}
		}
	}
	
	public Collection<Team> getTeams(){
		return teams.values();
	}
	public Team getWinners(){
		if(teams.size() > 1)
			return null;
		else return teams.values().toArray(new Team[0])[0];
	}
	public void removeTeam(TeamType type){
		if(teams.containsKey(type))
			teams.remove(type);
	}
	public Team getTeam(TeamType type){
		return teams.get(type);
	}
	public Team getTeam(BPlayer player){
		return getTeam(player.getTeam());
	}

	public boolean onFight(Entity damaged, Entity damager){
		BPlayer badblockDamaged = BPlayersManager.getInstance().getPlayer(damaged)
				, badblockDamager = BPlayersManager.getInstance().getPlayer(damager);
		if(badblockDamaged == null || badblockDamager == null)
			return false;
		if(badblockDamaged.isSpectator() || badblockDamager.isSpectator())
			return true;
		if(badblockDamager.getTeam() == badblockDamaged.getTeam()){
			badblockDamager.sendMessage("%red%On ne tape pas ses amis, oh !");
			return true;
		} else return false;
	}
}
