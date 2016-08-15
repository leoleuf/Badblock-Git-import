package fr.badblock.api.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import fr.badblock.api.utils.bukkit.ConfigUtils;
import fr.badblock.api.utils.bukkit.title.Title;

public abstract class Team {
	private List<UUID> players;
	private Location spawn;
	protected TeamType type;

	public TeamType getType(){
		return type;
	}
	public Location getSpawn(){
		return spawn;
	}
	public List<UUID> getPlayers(){
		return players;
	}

	public Team(){}
	public abstract void loose();
	public abstract int getScore();

	public void init(ConfigurationSection section, String deb, TeamType type, boolean spawn){
		if(players == null)
			this.players = new ArrayList<UUID>();
		this.type = type;
		if(spawn){
			this.spawn = ConfigUtils.loadLocation(section, deb + ".spawn");
			initTeam(section, deb);
		}
	}

	public abstract void initTeam(ConfigurationSection section, String deb);

	public void update(){
		for(int i=0;i<players.size();i++){
			BPlayer player = BPlayersManager.getInstance().getPlayer(players.get(i));
			if(player == null || player.getTeam() == null){
				players.remove(i);
				i--;
			}
		}
	}
	public void sendMessage(String... messages){																
		for(UUID uniqueId : players){
			BPlayer player = BPlayersManager.getInstance().getPlayer(uniqueId);
			if(player != null)
				player.sendMessage(messages);
		}
	}
	public void enter(BPlayer player){
		if(player.isSpectator()){
			player.sendMessage("%red%Vous êtes spéctateur, vous ne pouvez pas entrer dans une équipe !");
		} else if(player.getTeam() == type){
			player.sendMessage("%red%Vous êtes déjà dans l'équipe " + type.getFrench().toLowerCase() + " !");
		} else {
			if(isFull()){
				player.sendMessage("%red%Cette équipe est pleine !");
			} else {
				if(player.getTeam() != null){
					player.getTeam().getTeam().players.remove(player.getUniqueId());
				}
				player.setTeam(type);
				new Title("%gold%Succès !", "%gold%Vous avez rejoint l'équipe " + type.getDisplayName() + " %gold%!", 40).send(player.getPlayer());

				BPlayersManager.getInstance().update("00m00s", true);
				players.add(player.getUniqueId());
			}
		}
	}
	public boolean isFull(){
		return players.size() >= TeamsManager.getInstance().getMaxPlayerInTeam();
	}
	public boolean isEmpty(){
		return players.size() == 0;
	}
	public enum TeamType {
		RED("red", "%darkred%[%red%Rouge%darkred%]%red%", "%red%", "Rouge", DyeColor.RED),
		BLUE("blue", "%dblue%[%blue%Bleu%dblue%]%blue%", "%blue%", "Bleu", DyeColor.BLUE),
		GREEN("green", "%dgreen%[%green%Vert%dgreen%]%green%", "%green%", "Vert", DyeColor.GREEN),
		YELLOW("yellow", "%dgray%[%yellow%Jaune%dgray%]%yellow%", "%yellow%", "Jaune", DyeColor.YELLOW),
		WHITE("white", "%dgray%[%white%Blanc%dgray%]%white%", "%white%", "Blanc	", DyeColor.WHITE),
		PINK("pink", "%dpurple%[%lpurple%Rose%dpurple%]%lpurple%", "%lpurple%", "Rose", DyeColor.PINK),
		ORANGE("orange", "%red%[%gold%Orange%red%]%gold%", "%gold%", "Orange", DyeColor.ORANGE),
		GRAY("gray", "%dgray%[%gray%Grise%dgray%]%gray%", "%gray%", "Grise", DyeColor.GRAY),
		CYAN("cyan", "%blue%[%aqua%Cyan%blue%]%aqua%", "%aqua%", "Cyan", DyeColor.CYAN);
		
		private String simpleName, displayName, color, french;
		private DyeColor dyeColor;

		private TeamType(String simpleName, String displayName, String color, String french, DyeColor dyeColor){
			this.simpleName = simpleName;
			this.displayName = displayName;
			this.color = color;
			this.french = french;
			this.dyeColor = dyeColor;
		}
		public DyeColor getDyeColor(){
			return dyeColor;
		}
		public boolean isEnabled(){
			return getTeam() != null;
		}
		public Team getTeam(){
			return TeamsManager.getInstance().getTeam(this);
		}
		public String getFrench(){
			return french;
		}
		public String getName(){
			return simpleName;
		}
		public String getDisplayName(){
			return displayName;
		}
		public String getColor(){
			return color;
		}
		@SuppressWarnings("deprecation")
		public static TeamType getByDyeColor(ItemStack is){
			if(is == null) return null;
			DyeColor color = DyeColor.getByData(is.getData().getData());
			for(TeamType t : values()){
				if(t.dyeColor.equals(color))
					return t;
			}

			return null;
		}
	}
}
