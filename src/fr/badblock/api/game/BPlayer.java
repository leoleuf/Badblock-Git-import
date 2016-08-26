package fr.badblock.api.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.api.BPlugin;
import fr.badblock.api.MJPlugin;
import fr.badblock.api.commands.BadblockCommand;
import fr.badblock.api.commands.HealCommand;
import fr.badblock.api.game.Team.TeamType;
import fr.badblock.api.kit.Kit;
import fr.badblock.api.listeners.minigame.SpectatorListener;
import fr.badblock.api.scoreboard.BPlayerBoard;
import fr.badblock.api.scoreboard.BSpectatorBoard;
import fr.badblock.api.utils.bukkit.ChatUtils;
import fr.badblock.api.utils.bukkit.PlayerUtils;
import fr.badblock.api.utils.bukkit.title.Title;
import fr.badblock.bukkit.speak.Callback;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter public class BPlayer {
	@Getter protected JsonObject modified;
	
	protected UUID playerId;
	protected String playerName;
	protected TeamType team;
	protected BPlayerBoard board;

	protected boolean hasChanged = false;

	protected String vote;

	protected int money, level, xp, kills = 0;
	protected boolean mustCreateAccount = false, mustCreateStatsAccount = false, hasEnded = false;

	protected List<String> kits;
	protected Kit usedKit = null;

	protected JsonObject classementSet = null;

	protected boolean isAdmin, isVip, isSpectator;

	public boolean hasKit(Kit kit){
		if(kit.isVIP() && isVip()) return true;
		else if(kit.getPrice() == 0) return true;
		return kits.contains(kit.getBDDName());
	}

	public void win(){
		if(hasEnded) return;
		incrementWins();
		int xp = (int) (getXPReward() * MJPlugin.getInstance().getBoostXP()); giveXP(xp) ;
		int gold = (int) (getCoinsReward() * MJPlugin.getInstance().getBoostXP()); addCoins(gold);

		sendMessage("%gold%Vous avez gagné %aqua%" + gold + " BadCoins %gold%et %aqua%" + xp + " XP %gold%!");
		new BadblockCommand().run(getPlayer(), new String[]{});
		hasEnded = true;

		save();
	}
	
	public void loose(){
		loose(getPlayer());
	}
	
	public void loose(Player p){
		if(hasEnded) return;
		incrementLooses();
		int xp = (int) (getXPReward() * MJPlugin.getInstance().getBoostXP());
		giveXP(xp);
		sendMessage("%gold%Vous avez gagné %aqua%0 BadCoins %gold%et %aqua%" + xp + " XP %gold%!");
		new BadblockCommand().run(p, new String[]{});
		hasEnded = true;

		save();
	}
	protected boolean save = false;

	public int getXPReward(){
		int reward = (5 + kills) * (isVip ? 2 : 1);
		return reward > 30 ? 30 : reward;
	}
	public int getCoinsReward(){
		int reward = (3 + kills) * (isVip ? 2 : 1);
		return reward > 20 ? 20 : reward;
	}
	
	public BPlayer(Player player, boolean spectator) {
		this.playerId = player.getUniqueId();
		this.playerName = player.getName();
		this.isAdmin = player.hasPermission("minigames.admin") || player.isOp();

		this.isVip = player.hasPermission("minigames.vip");
		setSpectator(spectator);

		BPlugin plugin = BPlugin.getInstance();
		if(!plugin.getGameName().equalsIgnoreCase("lobby"))
			clear();

		money = 50;
		level = 1;
		xp = 0;
		kits = new ArrayList<String>();

		classementSet = new JsonObject();
		
		MJPlugin.getSpeaker().getPlayerData(new Callback<JsonObject>(){
			@Override
			public void call(JsonObject data, Throwable error) {
				if(error == null){
					modified = new JsonObject();
					
					if(data.has("game")){
						JsonObject game = data.get("game").getAsJsonObject();

						if(game.has("badcoins"))
							 money = game.get("badcoins").getAsInt();
						else money = 50;
						
						if(game.has("level"))
							 level = game.get("level").getAsInt();
						else level = 1;
						
						if(game.has("xp"))
							 xp = game.get("xp").getAsInt();
						else xp = 0;

						kits    = new ArrayList<>();
						if(game.has("kits")){
							JsonArray array = game.get("kits").getAsJsonArray();
							for(int i=0;i<array.size();i++)
								kits.add(array.get(i).getAsString());
						}

						if(!MJPlugin.getInstance().getGameName().equalsIgnoreCase("lobby")){
							String name = BPlugin.getInstance().getGameName().toLowerCase();
							
							if(game.has("stats") && game.get("stats").getAsJsonObject().has(name))
								classementSet  = game.get("stats").getAsJsonObject().get(name).getAsJsonObject();
							else classementSet = new JsonObject();
						}
					}
					
					if(data.has("commands") && data.get("commands").getAsJsonArray().size() > 0){
						JsonArray cmd = data.get("commands").getAsJsonArray();
						
						for(int i=0;i<cmd.size();i++){
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.get(i).getAsString());
						}

						modified.add("commands", new JsonArray());
						
						MJPlugin.getSpeaker().updatePlayerData(player, modified);
					}
					
					modified = new JsonObject();
				}

				update("00m00s", true);
			}
		}, player);
		
		setScoreboard();
	}
	public void setScoreboard(){
		if(BPlugin.getInstance() instanceof MJPlugin){
			board = ((MJPlugin)BPlugin.getInstance()).getPlayerBoard(this);
		}

		if(board != null)
			board.show();
	}
	public void hasChanged(boolean hasChanged){
		this.hasChanged = hasChanged;
	}
	public void update(String time, boolean playerCountHasChanged){
		board.setTime(time);
		
		if(hasChanged || playerCountHasChanged){
			hasChanged = false;
			
			board.update();
			board.show();
		}
		
		if(playerName.length() < 15 && team != null)
			getPlayer().setPlayerListName(ChatUtils.colorReplace(team.getColor() + playerName));
	}

	/** Coins system **/

	public void addCoins(int nbr) {
		money += nbr;
	}
	
	public int getCoins(){
		return money;
	}
	
	public void setCoins(int money){
		this.money = money;
	}
	
	public void removeCoins(int nbr) {
		money -= nbr;
	}

	public void save() {
		if(save) return;
		save = true;
		
		if(BPlugin.getInstance().getGameName().equalsIgnoreCase("lobby"))
			return;
		if(money < 0) money = 0;
		
		String pluginLower = BPlugin.getInstance().getGameName().toLowerCase();

		if(modified != null) {
			modified.add("game", new JsonObject());
			JsonObject game 	= modified.get("game").getAsJsonObject();
			game.add("stats", new JsonObject());
			JsonObject stats    = game.get("stats").getAsJsonObject();
			JsonArray  kitsJson = new JsonArray();

			for(String kit : kits)
				kitsJson.add(new JsonPrimitive(kit));
			
			game.addProperty("badcoins", money);
			game.addProperty("level", level);
			game.addProperty("xp", xp);
			game.add("kits", kitsJson);
			stats.add(pluginLower, classementSet);
			
			MJPlugin.getSpeaker().updatePlayerData(getPlayerName(), modified);
		}
	}
	
	public void prepare(){
		HealCommand.heal(getPlayer());
		getPlayer().closeInventory();

		if(usedKit != null)
			usedKit.give(this);
		else clear();
	}

	public void clear(){
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
	}
	/** Statistiques et classement **/

	// Base increments
	public void incrementKill() {
		incrementStat("kills");
		kills++;
	}
	public void incrementDeath() {
		incrementStat("deaths");
	}
	public void incrementWins() {
		incrementStat("wins");	
	}
	public void incrementLooses() {
		incrementStat("looses");
	}

	public void incrementStat(String name){
		incrementStat(name, 1);
		hasChanged = true;
	}
	public int getXP(){
		return xp;
	}
	public void giveXP(int xp){
		this.xp += xp;
		while(this.xp > getNeededXP()){
			this.xp -= getNeededXP();
			this.level++;
		}
	}
	public int getNeededXP(){
		return (int)(Math.pow(1.2d, level + 1) * 100);
	}
	
	public void incrementStat(String name, int adder) {
		classementSet.addProperty(name, getStatValue(name) + adder);
	}
	
	public int getStatValue(String name){
		return classementSet.has(name) ? classementSet.get(name).getAsInt() : 0;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(playerId);
	}

	public void sendMessage(String... messages) {
		Player p = Bukkit.getPlayer(playerId);
		if (PlayerUtils.isValid(p))
			ChatUtils.sendMessagePlayer(p, messages);
	}
	public void sendTitle(Title title){
		Player p = Bukkit.getPlayer(playerId);
		if (PlayerUtils.isValid(p))
			title.send(p);
	}
	public UUID getUniqueId() {
		return playerId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

	public boolean isSpectator() {
		return isSpectator;
	}

	public void setSpectator(boolean isSpectator) {
		setSpectator(isSpectator, getPlayer());
	}
	
	public void setSpectator(boolean isSpectator, Player p) {
		this.isSpectator = isSpectator;
		this.team = null;

		if(isSpectator()){
			SpectatorListener.prepareSpectator(p);
			for(BPlayer player : BPlayersManager.getInstance().getPlayers()){
				if(player.getPlayer() == null || p == null) continue;
				if(player.isSpectator()){
					player.getPlayer().showPlayer(p);
					p.showPlayer(player.getPlayer());
				} else {
					player.getPlayer().hidePlayer(p);
					p.showPlayer(player.getPlayer());
				}
			}
			BSpectatorBoard.getInstance().addSpectator(p);
		}
	}
	
	@Override
	public String toString(){
		new Throwable().printStackTrace();
		return "";
	}
}
