package fr.badblock.game.core112R1.players;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.listeners.CustomProjectileListener;
import fr.badblock.game.core112R1.players.data.GamePlayerData;
import fr.badblock.game.core112R1.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core112R1.players.utils.PlayerLoginWorkers;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.result.Result;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.players.bossbars.BossBarColor;
import fr.badblock.gameapi.players.bossbars.BossBarStyle;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.utils.selections.Vector3f;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.PermissionManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PlayerChunkMap;
import net.minecraft.server.v1_12_R1.WorldServer;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.boss.ViaBossBar;

public class GameBadblockPlayer extends CraftPlayer implements BadblockPlayer {
	public static final Type collectionType = new TypeToken<List<String>>() {}.getType();
	public static final Type collectType = new TypeToken<List<Long>>() {}.getType();
	@Getter@Setter
	private CustomObjective 			 customObjective 	  = null;
	@Getter
	private GamePlayerData 				 playerData 		  = null;
	@Getter
	public PermissiblePlayer 			 permissions 		  = null;

	@Getter
	private Map<Class<?>, InGameData> 	 inGameData  		  = null;

	private GameMode 					 gamemodeBefJail 	  = null;

	private BukkitRunnable 				 spectatorRunnable 	  = null;
	@Getter
	private BadblockMode 				 badblockMode 		  = BadblockMode.PLAYER;
	@Setter@Getter
	private boolean 					 dataFetch			  = false;
	@Getter@Setter
	private Environment 				 customEnvironment 	  = null;
	@Getter@Setter
	private BadblockTeam				 team				  = null;
	@Setter
	private boolean						 adminMode			  = false;
	@Getter@Setter
	private boolean						 onlineMode			  = false;
	@Getter@Setter
	JsonObject					 object				  = null;

	@Getter@Setter
	private Vector3f					 firstVector, secondVector;
	@Getter
	private long 						lastFakeEntityUsedTime;
	@Getter
	private boolean						visible;
	@Getter
	private Predicate<BadblockPlayer> 	visiblePredicate	= p -> true;
	@Getter
	private Predicate<BadblockPlayer> 	invisiblePredicate	= p -> false;
	@Getter@Setter
	private String						realName;
	@Getter@Setter
	private List<UUID>					playersWithHim;
	@Getter@Setter
	public  List<Long>					leaves		   = new ArrayList<>();
	@Getter@Setter
	public boolean 						ghostConnect;
	@Getter@Setter
	private boolean						resultDone;
	@Getter@Setter
	private int							shopPoints;
	@Getter@Setter
	private long						joinTime;
	@Getter@Setter
	private double						moveDist;
	@Getter@Setter
	private long						vlAfk;
	@Getter@Setter
	private String						customRank;
	@Getter@Setter
	private String						customColor;
	@Getter@Setter
	private boolean						hasJoined;

	@Getter@Setter
	private RankedPlayer				ranked;
	@Getter@Setter
	private int							monthRank = -1;
	@Getter@Setter
	private int							totalRank = -1;
	@Getter@Setter
	private int							totalPoints = -1;

	public GameBadblockPlayer(CraftServer server, EntityPlayer entity, GameOfflinePlayer offlinePlayer) {
		super(server, entity);
		setJoinTime(System.currentTimeMillis());
		this.inGameData  = Maps.newConcurrentMap();

		this.playerData  = offlinePlayer == null ? new GamePlayerData() : offlinePlayer.getPlayerData(); // On initialise pour ne pas provoquer de NullPointerException, mais sera recr�� � la r�c�ptions des donn�es
		this.playerData.setGameBadblockPlayer(this);

		this.permissions = PermissionManager.getInstance().createPlayer(getName(), offlinePlayer == null ? new JsonObject() : offlinePlayer.getObject());

		if(offlinePlayer != null) {
			object = offlinePlayer.getObject();
			team 	   = offlinePlayer.getTeam();
			inGameData = offlinePlayer.getInGameData();
			return;
		}else object = new JsonObject();
		// Load async
		if (!GameAPI.getServerName().startsWith("login"))
		{
			PlayerLoginWorkers.workAsync(this);
		}
	}

	public void updateData(JsonObject object)
	{
		// Refresh Shop points
		this.refreshShopPoints();

		// Online mode
		if (object.has("onlineMode"))
		{
			this.onlineMode = object.get("onlineMode").getAsBoolean();
		}

		// Game
		if (object.has("game"))
		{
			JsonObject game = object.get("game").getAsJsonObject();
			this.object.add("game", game);
			playerData = GameAPI.getGson().fromJson(game, GamePlayerData.class);
			playerData.setData(game);
			playerData.setGameBadblockPlayer(this);
			if (object.has("onlyJoinWhileWaiting"))
			{
				playerData.onlyJoinWhileWaiting = object.get("onlyJoinWhileWaiting").getAsLong();
			}
		}

		// LeaverBuster
		if (object.has("leaves"))
		{
			this.leaves = GameAPI.getGson().fromJson(object.get("leaves").toString(), collectType);
			if (this.leaves == null) this.leaves = new ArrayList<>();
		}
		else
		{
			this.setLeaves(new ArrayList<>());
		}

		// Parties
		if (object.has("playersWithHim"))
		{
			try
			{
				List<String> playersStringWithHim = GameAPI.getGson().fromJson(object.get("playersWithHim").getAsString(), collectionType);
				if (playersWithHim == null)
				{
					playersWithHim = new ArrayList<>();
				}
				else
				{
					playersWithHim.clear();
				}
				playersStringWithHim.forEach(playerString -> playersWithHim.add(UUID.fromString(playerString)));
				if (playersStringWithHim.contains(getUniqueId().toString()))
				{
					playersWithHim.clear();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		if (object.has("permissions"))
		{
			this.object.add("permissions", object.get("permissions"));
			permissions = PermissionManager.getInstance().createPlayer(getRealName() != null ? getRealName() : getName(), object);
		}

		// Result
		if (getJoinTime() != -1)
		{
			long difference = System.currentTimeMillis() - getJoinTime();
			System.out.println("[API] Loaded player " + getName() + " in " + difference + " ms.");
			setJoinTime(-1);
		}
	}

	@Override
	public void refreshShopPoints()
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public void addShopPoints(int shopPointsToAdd)
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public void removeShopPoints(int shopPointsToRemove)
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public EntityPlayer getHandle() {
		return (EntityPlayer) entity;
	}

	private GameAPI getAPI() {
		return GameAPI.getAPI();
	}

	private I18n getI18n() {
		return getAPI().getI18n();
	}

	@Override
	public boolean isInvulnerable() {
		return getHandle().abilities.isInvulnerable;
	}

	@Override
	public void setInvulnerable(boolean invulnerable) {
		getHandle().abilities.isInvulnerable = invulnerable;
		getHandle().updateAbilities();
	}

	@Override
	public boolean canInstantlyBuild() {
		return getHandle().abilities.canInstantlyBuild;
	}

	@Override
	public void setCanInstantlyBuild(boolean instantlyBuild) {
		getHandle().abilities.canInstantlyBuild = instantlyBuild;
		getHandle().updateAbilities();
	}

	@Override
	public boolean canBuild() {
		return getHandle().abilities.mayBuild;
	}

	@Override
	public void setCanBuild(boolean canBuild) {
		getHandle().abilities.mayBuild = canBuild;
		getHandle().updateAbilities();
	}

	@Override
	public void setReducedDebugInfo(boolean reducedDebugInfo) {

	}

	@Override
	public void playRain(boolean rain) {

	}

	@Override
	public void showDemoScreen() {

	}

	@Override
	public void heal() {
		setFireTicks(0);
		setArrowsInBody((byte) 0);
		removeBadPotionEffects();
		feed();

		setHealth(getMaxHealth());
	}

	@Override
	public void feed() {
		setFoodLevel(20);
		setSaturation(10);
	}

	@Override
	public void clearInventory() {
		getInventory().clear();
		getInventory().setArmorContents(new ItemStack[getInventory().getArmorContents().length]);
	}

	@Override
	public void removePotionEffects() {
		for (PotionEffectType type : PotionEffectType.values()) {
			if (type != null)
				removePotionEffect(type);
		}
	}

	@Override
	public void removeBadPotionEffects() {
		removePotionEffect(PotionEffectType.BLINDNESS);
		removePotionEffect(PotionEffectType.CONFUSION);
		removePotionEffect(PotionEffectType.HARM);
		removePotionEffect(PotionEffectType.HUNGER);
		removePotionEffect(PotionEffectType.POISON);
		removePotionEffect(PotionEffectType.SLOW);
		removePotionEffect(PotionEffectType.SLOW_DIGGING);
		removePotionEffect(PotionEffectType.WEAKNESS);
		removePotionEffect(PotionEffectType.WITHER);
	}

	@Override
	public int getPing() {
		return getHandle().ping;
	}

	@Override
	public void playSound(Sound sound) {
		playSound(getLocation(), sound);
	}

	@Override
	public void playSound(Location location, Sound sound) {
		playSound(location, sound, 3.0f, 1.0f);
	}

	protected Locale getBadLocale() {
		return playerData.getLocale() == null ? Locale.FRENCH_FRANCE : playerData.getLocale();
	}

	@Override
	public String[] getTranslatedMessage(String key, Object... args) {
		return getI18n().get(getBadLocale(), key, args);
	}

	@Override
	public void saveOnlineMode()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("onlineMode", isOnlineMode());
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(this, jsonObject);
	}

	@Override
	public void saveGameData() {
		if (isDataFetch()) {
			GameAPI.getAPI().getLadderDatabase().updatePlayerData(this, getPlayerData().saveData());
		}
	}

	@Override
	public void postResult(Result ztoPost) {
		/*long   id	    = new SecureRandom().nextLong();
		long   party    = GamePlugin.getInstance().getGameServer().getGameId();
		String player   = getName().toLowerCase();
		UUID   playerId = getUniqueId();
		String gameType = GameAPI.getGameName();
		String server   = Bukkit.getServerName();*/
		//String result   = GameAPI.getGson().toJson(toPost);


		//	PreparedStatement statement = null;

		try {
			/*statement = GameAPI.getAPI().getSqlDatabase().preparedStatement("INSERT INTO parties(id, party, player, playerId, gametype, servername, day, result)"
					+ " VALUES(?, ?, ?, ?, ?, ?, NOW(), ?)");
			statement.setLong(1, id);
			statement.setLong(2, party);
			statement.setString(3, player);
			statement.setString(4, playerId.toString());
			statement.setString(5, gameType);
			statement.setString(6, server);
			statement.setString(7, result);

			statement.executeUpdate();*/

			if (!resultDone) {
				int percent = (int) Math.round(((double)getPlayerData().getXp() / (double)getPlayerData().getXpUntilNextLevel()) * 100);

				String line = "&a";

				for(int i=0;i<100;i++){
					if(i == percent)
						line += "&8";
					line += "|";
				}
				sendTranslatedMessage("game.result", 
						getPlayerData().getBadcoins(), getPlayerData().getLevel(), percent, getPlayerData().getXp(),
						getPlayerData().getXpUntilNextLevel(), line, "", getPlayerData().getAddedBadcoins(), 
						getPlayerData().getAddedLevels(), getPlayerData().getAddedXP(), getPlayerData().getAddedShopPoints());

				resultDone = true;
			}

			saveGameData();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			/*if(statement != null)
				try {
					statement.close();
				} catch (SQLException e){}*/
		}

	}
	
	private long bl;

	@Override
	public void sendMessage(String message)
	{
		if (message != null)
		{
			if (message.contains("§f[§b"))
			{
				super.sendMessage(message);
				return;
			}
			
			if (message.toLowerCase().contains("multiverse"))
			{
				return;
			}
			
			if (message.toLowerCase().contains("pas la permission")
					|| message.toLowerCase().contains("tu ne peux pas")
					|| message.toLowerCase().contains("required permission")
					|| message.toLowerCase().contains("tu ne peux ")
					|| message.toLowerCase().contains("not permitted")
					|| message.toLowerCase().contains("have permission")
					|| message.toLowerCase().contains("/region "))
			{
				if (bl > System.currentTimeMillis())
				{
					return;
				}
				
				bl = System.currentTimeMillis() + 1000;
				super.sendMessage("§fCommande inconnue.");
				return;
			}
		}
		super.sendMessage(message);
	}

	@Override
	public void sendTranslatedMessage(String key, Object... args) {
		System.out.println("Key GameBadblockPlayer->sendTranslatedMessage() 0 : " + key);
		sendMessage(getTranslatedMessage(key, args));
	}

	@Override
	public void sendActionBar(String message) {
		// TODO
		//super.sendAc(message);
	}

	@Override
	public void sendTranslatedActionBar(String key, Object... args) {
		sendActionBar(getTranslatedMessage(key, args)[0]);
	}

	private Map<String, ViaBossBar> bossBars    = new HashMap<>();
	private ViaBossBar	            lastBossBar = null;

	@Override
	public void addBossBar(String key, String message, float life, BossBarColor color, BossBarStyle style) {
		message = getI18n().replaceColors(message);

		if(bossBars.containsKey(key.toLowerCase())){
			changeBossBar(key, message);
			changeBossBarStyle(key, life, color, style);

			return;
		}

		ViaBossBar bar = new ViaBossBar(message, life, us.myles.ViaVersion.api.boss.BossColor.valueOf(color.name()), us.myles.ViaVersion.api.boss.BossStyle.valueOf(style.name()));
		bar.addPlayer(this);

		bossBars.put(key.toLowerCase(), bar);
		lastBossBar = bar;
	}

	@Override
	public void changeBossBar(String key, String message) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			message = getI18n().replaceColors(message);
			bar.setTitle(message);

			lastBossBar = bar;
		}
	}

	@Override
	public void changeBossBarStyle(String key, float life, BossBarColor color, BossBarStyle style) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			bar.setHealth(life);
			bar.setColor(BossColor.valueOf(color.name()));
			bar.setStyle(BossStyle.valueOf(style.name()));

			lastBossBar = bar;
		}
	}

	@Override
	public void removeBossBar(String key) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			bar.removePlayer(this);
			bossBars.remove(key.toLowerCase());


			if(!bossBars.isEmpty()){
				lastBossBar = bossBars.values().iterator().next();
			} else {
				lastBossBar = null;
			}
		}
	}

	@Override
	public void removeBossBars() {
		bossBars.values().forEach(bar -> bar.removePlayer(this));

		bossBars.clear();

		lastBossBar = null;
	}

	@Override
	public void sendTranslatedBossBar(String key, Object... args) {
		sendBossBar(getTranslatedMessage(key, args)[0]);
	}

	@Override
	public void sendTitle(String title, String subtitle) {
		super.sendTitle(title, subtitle);
	}

	@Override
	public void sendTranslatedTitle(String key, Object... args) {
		String[] messages = getI18n().get(getLocale(), key, args);

		String title = messages[0];
		String subtitle = "";

		if (messages.length > 1)
			subtitle = messages[1];
		sendTitle(title, subtitle);
	}

	@Override
	public void clearTitle(){
		super.resetTitle();
	}

	@Override
	public void sendTimings(long fadeIn, long stay, long fadeOut) {
	}

	@Override
	public void sendTabHeader(String header, String footer) {

	}

	@Override
	public void sendTranslatedTabHeader(TranslatableString header, TranslatableString footer) {
		sendTabHeader(StringUtils.join(header.get(this), "\\n"), StringUtils.join(footer.get(this), "\\n"));
	}

	@Override
	public void showFloatingText(String text, Location location, long lifeTime, double offset) {
		// TODO
	}

	@Override
	public void showTranslatedFloatingText(Location location, long lifeTime, double offset, String key, Object... args) {
		showFloatingText(getTranslatedMessage(key, args)[0], location, lifeTime, offset);
	}

	@Override
	public void jailPlayerAt(Location location) {
		// TODO
	}

	@Getter private Location centerJail = null;
	@Getter private double   radius 	= 0.0d;

	@Override
	public void pseudoJail(Location location, double radius) {
		this.centerJail = location;
		this.radius		= radius;
	}

	@Override
	public boolean isJailed() {
		return false;
	}

	@Override
	public boolean isPseudoJailed(){
		return centerJail != null;
	}

	@Override
	public void playChestAnimation(Block block, boolean open) {

	}

	@Override
	public void setEntityCollision(boolean collision) {
		getHandle().collides = collision;
	}

	@Override
	public void setArrowsInBody(byte amount) {
		//super.setArrowsStuck(amount);
	}

	@Override
	public void changePlayerDimension(Environment world) {

	}

	@Override
	public void reloadMap() {
		WorldServer server = (WorldServer) ReflectionUtils.getHandle(getWorld());
		try {
			PlayerChunkMap map = ((PlayerChunkMap) new Reflector(server).getFieldValue("manager"));
			map.removePlayer(getHandle());
			map.addPlayer(getHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendParticle(Location location, ParticleEffect effect) {

	}

	@Override
	public void showCustomObjective(CustomObjective objective) {
		if(objective == null) return;

		objective.showObjective(this);
	}

	@Override
	public <T extends InGameData> T inGameData(Class<T> clazz) {
		try {
			if (!inGameData.containsKey(clazz)) {
				inGameData.put(clazz, (InGameData) clazz.getConstructor().newInstance());
			}

			return clazz.cast(inGameData.get(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			GameAPI.logError("Invalid InGameData class (" + clazz + ") ! Return null.");
			return null;
		}
	}

	@Override
	public boolean hasPermission(GamePermission permission) {
		return permission.getPermission() == null ? true : hasPermission(permission.getPermission());
	}

	@Override
	public boolean hasPermission(String permission) {
		if(GameAPI.getAPI().getRunType() == RunType.DEV && permissions.hasPermission("devserver"))
			return true;
		return permission == null ? true : permissions.hasPermission(permission);
	}

	@Override
	public TranslatableString getGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.chat." + getFakeMainGroup());
	}

	@Override
	public TranslatableString getGroupSuffix() {
		if (customColor != null && !customColor.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customColor);
			return result;
		}
		return new TranslatableString("permissions.chat_suffix." + getFakeMainGroup());
	}

	@Override
	public TranslatableString getTabGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.tab." + getFakeMainGroup());
	}

	public String getFakeMainGroup()
	{
		String rankName = permissions.getParent().getName();
		if (getRealName() != null)
		{
			if (rankName.equalsIgnoreCase("gradeperso"))
			{
				return rankName;
			}
			List<String> groups = new ArrayList<>(permissions.getAlternateGroups().keySet());
			groups.add(permissions.getSuperGroup());
			rankName = "default";
			int rankLevel = 0;
			for (String group : groups)
			{
				PermissibleGroup g = PermissionManager.getInstance().getGroup(group);
				if (!g.isStaff())
				{
					if (g.getPower() > rankLevel)
					{
						rankName = g.getName();
						rankLevel = g.getPower();
					}
				}
			}
		}
		return rankName;
	}

	@Override
	public String getMainGroup() {
		return permissions.getSuperGroup();
	}

	@Override
	public Collection<String> getAlternateGroups() {
		return permissions.getAlternateGroups().keySet();
	}

	@Override
	public void sendPlayer(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(getRealName() != null ? getRealName() : getName());
		out.writeUTF(server);
		sendPluginMessage(GameAPI.getAPI(), "BungeeCord", out.toByteArray());
	}

	@Override
	public void setBadblockMode(BadblockMode newMode) {
		if (newMode != badblockMode) {
			if (newMode == BadblockMode.PLAYER) {
				if (spectatorRunnable != null) {
					spectatorRunnable.cancel();
					spectatorRunnable = null;
				}

				setGameMode(GameMode.SURVIVAL);
			} else if (spectatorRunnable == null) {
				setGameMode(GameMode.SPECTATOR);

				if(!isPseudoJailed() || newMode == BadblockMode.SPECTATOR){
					spectatorRunnable = new TooFarRunnable();
					spectatorRunnable.runTaskTimer(GameAPI.getAPI(), 0, 20L);
				}
			}

			badblockMode = newMode;
		}
	}

	class TooFarRunnable extends BukkitRunnable {
		@Override
		public void run() {
			if (!isOnline()) {
				cancel();
				return;
			}

			Player closestPlayer = null;
			double minDistance = Integer.MAX_VALUE;

			for (BadblockPlayer online : GameAPI.getAPI().getOnlinePlayers()) {
				if (!able(online))
					continue;

				double distance = getLocation().distance(online.getLocation());

				if (distance < minDistance) {
					minDistance = distance;
					closestPlayer = online;
				}
			}

			if (minDistance > 32.0D) {
				Player tp = closestPlayer;
				if (tp == null)
					tp = getRandomNonSpecPlayer();

				if (tp != null){
					teleport(tp);
					GameMessages.doNotGoTooFarWhenSpectator().send(GameBadblockPlayer.this);
				}
			}
		}

		public Player getRandomNonSpecPlayer() {
			for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()) {
				if(able(player))
					return player;
			}

			return null;
		}

		public boolean able(BadblockPlayer player){
			return !player.getUniqueId().equals(getUniqueId()) && player.getGameMode() != GameMode.SPECTATOR && player.getBadblockMode() == BadblockMode.PLAYER
					&& !player.getLocation().getWorld().equals(getLocation().getWorld());
		}
	}

	@Override
	public CuboidSelection getSelection() {
		if(firstVector != null && secondVector != null){
			return new CuboidSelection(getWorld().getName(), firstVector, secondVector);
		}

		return null;
	}

	@Override
	public boolean hasAdminMode(){
		return adminMode;
	}

	@Override
	public boolean isDisguised(){
		return false;
	}

	@Override
	public void undisguise() {

	}

	@Override
	public int countItems(Material type, byte data) {
		int result = 0;

		for(ItemStack item : getInventory().getContents()){
			if(ItemStackUtils.isValid(item) && item.getType() == type && item.getDurability() == data){
				result += item.getAmount();
			}
		}

		return result;
	}

	@Override
	public int removeItems(Material type, byte data, int amount) {

		for(int i=0;i<getInventory().getContents().length;i++){
			ItemStack item = getInventory().getContents()[i];

			if(ItemStackUtils.isValid(item) && item.getType() == type && item.getDurability() == data){

				if(amount != -1){

					int to = 0;

					if(amount < item.getAmount()){
						to = item.getAmount() - amount;
						amount = 0;
					} else amount -= item.getAmount();

					if(to <= 0)
						getInventory().setItem(i, null);
					else item.setAmount(to);
				} else getInventory().setItem(i, null);

				if(amount == 0)
					break;
			}
		}

		updateInventory();

		return amount;
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action) {
		return launchProjectile(projectile,  action, 0);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action, int range) {
		T proj = launchProjectile(projectile);
		proj.setMetadata(CustomProjectileListener.metadataKey, new ProjectileMetadata(action));

		if(range <= 0)
			return proj;

		Vector velocity  = proj.getVelocity();
		Location initLoc = proj.getLocation();

		new BukkitRunnable() {
			@Override
			public void run() {
				if(proj.isDead() || !proj.isValid() || proj.getLocation().distance(initLoc) > range){
					cancel();
					return;
				}

				proj.setVelocity(velocity);
			}
		}.runTaskTimer(GameAPI.getAPI(), 0, 1L);

		return proj;
	}

	@AllArgsConstructor
	public static class ProjectileMetadata implements MetadataValue {
		private BiConsumer<Block, Entity> value;

		@Override
		public BiConsumer<Block, Entity> value() {
			return value;
		}

		@Override
		public void invalidate() {
			value = null;
		}

		@Override
		public Plugin getOwningPlugin() {
			return GameAPI.getAPI();
		}

		@Override
		public String asString() {
			return "";
		}

		@Override
		public short asShort() {
			return 0;
		}

		@Override
		public long asLong() {
			return 0;
		}

		@Override
		public int asInt() {
			return 0;
		}

		@Override
		public float asFloat() {
			return 0;
		}

		@Override
		public double asDouble() {
			return 0;
		}

		@Override
		public byte asByte() {
			return 0;
		}

		@Override
		public boolean asBoolean() {
			return false;
		}
	}

	@Override
	public void useFakeEntity() {
		this.lastFakeEntityUsedTime = System.currentTimeMillis() + 500; // décalage de 500ms pour les double packets
	}

	@Override
	public void setVisible(boolean visible) {
		setVisible(visible, player -> true);
	}

	@Override
	public void setVisible(boolean visible, Predicate<BadblockPlayer> visiblePredicate) {
		this.visible = visible;
		if(visible){
			this.visiblePredicate = visiblePredicate;
			GameAPI.getAPI().getOnlinePlayers().stream().filter(visiblePredicate).forEach(player -> player.showPlayer(this));
		} else {
			this.invisiblePredicate = visiblePredicate;
			GameAPI.getAPI().getOnlinePlayers().stream().filter(visiblePredicate).forEach(player -> player.hidePlayer(this));
		}
	}

	@SuppressWarnings("deprecation")@Override
	public int getProtocolVersion() {
		return us.myles.ViaVersion.api.ViaVersion.getInstance().getPlayerVersion(this);
	}

	@Override
	public <T> T getPermissionValue(String key, Class<T> clazz) {
		JsonElement el = permissions.getValue(key);

		return el == null ? null : GameAPI.getGson().fromJson(permissions.getValue(key), clazz);
	}

	@Override
	public int getVipLevel() {
		Integer res = getPermissionValue("badblock.viplevel", Integer.class);
		return res == null ? 0 : (int) res;
	}

	@Override
	public boolean hasVipLevel(int level, boolean showErrorMessage) {
		boolean have = getVipLevel() >= level;

		if(!have && showErrorMessage){
			sendTranslatedMessage("game.vip.needlevel." + level);
		}

		return have;
	}

	@Override
	public boolean canOnlyJoinWhileWaiting() {
		return this.getPlayerData().onlyJoinWhileWaiting > System.currentTimeMillis();
	}

	@Override
	public void setOnlyJoinWhileWaiting(long time) {
		this.getPlayerData().onlyJoinWhileWaiting = time;
	}

	@Override
	public void setPlayerSkin(String skinUrl) {
		setPlayerSkin0(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl));
	}

	@Override
	public void setPlayerSkin(String skinUrl, String capeUrl) {
		setPlayerSkin0(String.format("{textures:{SKIN:{url:\"%s\"},CAPE:{url\"%s\"}}", skinUrl, capeUrl));
	}

	private void setPlayerSkin0(String textures)
	{
		PropertyMap map = this.getHandle().getProfile().getProperties();

		map.clear();

		byte[] encodedData = Base64.getEncoder().encode(textures.getBytes());
		map.put("textures", new Property("textures", new String(encodedData)));

		updatePlayerSkin();
	}

	@Override
	public void setTextureProperty(String value, String signature) {
		TaskManager.runTask(new Runnable() {
			@Override
			public void run() {
				PropertyMap map = getHandle().getProfile().getProperties();
				map.clear();
				map.put("textures", new Property("textures", value, signature));
				updatePlayerSkin();
			}
		});
	}

	public void updatePlayerSkin()
	{
		resetPlayerView();

		for(BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers())
		{
			if(player == this || !player.canSee(this))
				return;

			player.hidePlayer(this);
			player.showPlayer(this);
		}
	}

	public void resetPlayerView()
	{

	}

}