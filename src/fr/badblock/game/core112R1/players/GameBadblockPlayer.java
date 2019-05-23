package fr.badblock.game.core112R1.players;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.api.common.minecraft.matchmaking.MatchmakingEnterRequest;
import fr.badblock.api.common.minecraft.party.Party;
import fr.badblock.api.common.minecraft.party.PartyPlayer;
import fr.badblock.api.common.minecraft.party.PartyPlayerRole;
import fr.badblock.api.common.minecraft.party.PartyPlayerState;
import fr.badblock.api.common.utils.data.Callback;
import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.Permission;
import fr.badblock.api.common.utils.permissions.Permission.PermissionResult;
import fr.badblock.api.common.utils.permissions.PermissionSet;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.api.common.utils.permissions.PermissionsManager;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.listeners.CustomProjectileListener;
import fr.badblock.game.core112R1.players.data.GamePlayerData;
import fr.badblock.game.core112R1.players.ingamedata.CommandInGameData;
import fr.badblock.game.core112R1.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core112R1.players.utils.PlayerLoginWorkers;
import fr.badblock.game.core112R1.technologies.rabbitlisteners.PlayerDataReceiver;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.result.Result;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.players.bossbars.BossBarColor;
import fr.badblock.gameapi.players.bossbars.BossBarStyle;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.utils.selections.Vector3f;
import fr.badblock.gameapi.utils.threading.TaskManager;
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
	private PermissionUser				permissions;

	@Getter
	private Map<Class<?>, InGameData> 	 inGameData  		  = null;

	private List<String>	tempPermissions = new ArrayList<>();

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
	JsonObject									 object				  = null;

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
	@Getter@Setter
	private boolean						load;

	public GameBadblockPlayer(CraftServer server, EntityPlayer entity, GameOfflinePlayer offlinePlayer) {
		super(server, entity);
		setJoinTime(System.currentTimeMillis());
		this.inGameData  = Maps.newConcurrentMap();

		this.playerData  = offlinePlayer == null ? new GamePlayerData() : offlinePlayer.getPlayerData(); // On initialise pour ne pas provoquer de NullPointerException, mais sera recr�� � la r�c�ptions des donn�es
		this.playerData.setGameBadblockPlayer(this);

		if (this.permissions == null)
		{
			this.permissions = new PermissionUser();
		}

		if(offlinePlayer != null) {
			object = offlinePlayer.getObject();
			team 	   = offlinePlayer.getTeam();
			inGameData = offlinePlayer.getInGameData();
			return;
		}else object = new JsonObject();
		
		if (!this.isLoad())
		{
			this.setRealName(entity.getName());
		}
		
		// Load async
		if (!GameAPI.getServerName().startsWith("login"))
		{
			PlayerLoginWorkers.workAsync(this);
		}
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String permission, boolean enable)
	{
		if (tempPermissions == null)
		{
			tempPermissions = new ArrayList<>();
		}

		if (enable && !tempPermissions.contains(permission))
		{
			tempPermissions.add(permission);
		}
		else if (!enable && tempPermissions.contains(permission))
		{
			tempPermissions.remove(permission);
		}

		return null;
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
			JsonObject game = object.getAsJsonObject("game");
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
			this.permissions = new PermissionUser(object.getAsJsonObject("permissions"));
			if (!permissions.getGroups().containsKey(GamePlugin.getInstance().getPermissionPlace()))
			{
				Map<String, Long> md = new HashMap<>();
				md.put("default", -1L);
				permissions.getGroups().put(GamePlugin.getInstance().getPermissionPlace(), md);
			}
			else if (permissions.getGroups().get(GamePlugin.getInstance().getPermissionPlace()).isEmpty())
			{
				Map<String, Long> md = new HashMap<>();
				md.put("default", -1L);
				permissions.getGroups().put(GamePlugin.getInstance().getPermissionPlace(), md);
			}

			Map<String, Long> newGroups = new HashMap<>();
			Map<String, Long> groups = permissions.getGroups().get(GamePlugin.getInstance().getPermissionPlace());

			boolean edit = false;
			for (Entry<String, Long> entry : groups.entrySet())
			{
				if (entry.getValue() > 0 && entry.getValue() < System.currentTimeMillis())
				{
					continue;
				}

				if (GamePlugin.getInstance().getServerConfig().getEquiv() != null)
				{
					if (!GamePlugin.getInstance().getServerConfig().getEquiv().containsKey(entry.getKey()))
					{
						newGroups.put(entry.getKey(), entry.getValue());
						continue;
					}

					edit = true;
					newGroups.put(GamePlugin.getInstance().getServerConfig().getEquiv().get(entry.getKey()), entry.getValue());
					continue;
				}
				else
				{
					newGroups.put(entry.getKey(), entry.getValue());
				}

			}

			if (edit)
			{
				permissions.getGroups().put(GamePlugin.getInstance().getPermissionPlace(), newGroups);
				this.object.add("permissions", new JsonParser().parse(permissions.getDBObject().toString()));
				Bukkit.getScheduler().runTaskLater(GamePlugin.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						saveGameData();
					}
				}, 20 * 2);
			}
		}

		// Aura
		if(!isGhostConnect())
		{
			inGameData(CommandInGameData.class).vanish = true;
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

	@Override
	public String[] getTranslatedMessage(String key, Object... args) {
		return getI18n().get(getLocale(), key, args);
	}

	@Override
	public void saveOnlineMode()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("onlineMode", isOnlineMode());
		PlayerDataReceiver.send(this);
	}

	@Override
	public void saveGameData() {
		if (isDataFetch()) {
			PlayerDataReceiver.send(this);
		}
	}

	@Override
	public void saveGameData(JsonObject jsonObject) {
		if (isDataFetch()) {
			PlayerDataReceiver.send(this, jsonObject);
		}
	}

	@Override
	public void postResult(Result ztoPost) {
	}

	@Override
	public void sendTranslatedMessage(String key, Object... args) {
		sendMessage(getTranslatedMessage(key, args));
	}

	@Override
	public void sendTranslatedActionBar(String key, Object... args) {
		sendActionBar(getTranslatedMessage(key, args)[0]);
	}

	private Map<String, ViaBossBar> bossBars    = new HashMap<>();
	private ViaBossBar	            lastBossBar = null;

	@Override
	public void addBossBar(String key, String message, float life, BossBarColor color, BossBarStyle style) {

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

	}

	@Override
	public void showTranslatedFloatingText(Location location, long lifeTime, double offset, String key, Object... args) {

	}

	@Override
	public void jailPlayerAt(Location location) {
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
	public void setEntityCollision(boolean collision) {
		super.setCollidable(collision);
	}

	@Override
	public void setArrowsInBody(byte amount) {
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
		return permission.getPermission() == null ? true : hasPermission(permission.getPermission()) || (tempPermissions != null && tempPermissions.contains(permission.getPermission()));
	}

	@Override
	public boolean hasPermission(String permission) {
		if(GameAPI.getAPI().getRunType() == RunType.DEV && permissions.hasPermission(GamePlugin.getInstance().getPermissionPlace(), "devserver"))
			return true;
		return permission == null ? true : permissions.hasPermission(GamePlugin.getInstance().getPermissionPlace(), permission) || (tempPermissions != null && tempPermissions.contains(permission));
	}

	@Override
	public boolean hasPermission(org.bukkit.permissions.Permission pm) {
		return hasPermission(pm.getName());
	}


	@Override
	public TranslatableString getGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.chat." + getMainGroup());
	}

	@Override
	public TranslatableString getGroupSuffix() {
		if (customColor != null && !customColor.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customColor);
			return result;
		}
		return new TranslatableString("permissions.chat_suffix." + getMainGroup());
	}

	@Override
	public TranslatableString getTabGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.tab." + getMainGroup());
	}

	@Override
	public String getMainGroup() {
		if (getRealName() != null && !getRealName().isEmpty() && !getRealName().equalsIgnoreCase(getName()))
		{
			return getHighestNickRank(GamePlugin.getInstance().getPermissionPlace(), false).getName();
		}
		
		return permissions.getHighestRank(GamePlugin.getInstance().getPermissionPlace(), false).getName();
	}

	private Permissible getHighestNickRank(String place, boolean onlyDisplayables)
	{
		List<String> g = getPermissions().getValidRanks(place);
		if (g == null || g.isEmpty())
		{
			return PermissionsManager.getManager().getGroup("default");
		}

		Permissible result = null;
		for (String group : g)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(group);

			if (permissible == null)
			{
				continue;
			}

			if (onlyDisplayables && !permissible.isDisplayable())
			{
				continue;
			}
			
			if (permissible.hasPermission("bungee.command.gnick.hidemyrank"))
			{
				continue;
			}

			if (result == null || permissible.getPower() > result.getPower())
			{
				result = permissible;
			}
		}

		if (result == null)
		{
			return PermissionsManager.getManager().getGroup("default");
		}

		return result;
	}

	@Override
	public Collection<String> getAlternateGroups() {
		return permissions.getValidRanks(GamePlugin.getInstance().getPermissionPlace());
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

				setGameMode(Bukkit.getDefaultGameMode());
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

				if (getLocation() == null || online.getLocation() == null)
				{
					continue;
				}
				
				if (getLocation().getWorld().equals(online.getLocation().getWorld()))
				{
					continue;
				}
				
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
	public int getPermissionValue(String key) {
		List<PermissionSet> perm = permissions.getPermissions(GamePlugin.getInstance().getPermissionPlace());

		int a = 0;
		for (PermissionSet permissionSet : perm)
		{
			JsonElement jsonElement = permissionSet.getValue(key);
			if (jsonElement.isJsonObject())
			{
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				if (jsonObject.entrySet().isEmpty())
				{
					continue;
				}
			}

			if (a < jsonElement.getAsInt())
			{
				a = jsonElement.getAsInt();
			}
		}

		return a;
	}

	@Override
	public int getVipLevel() {
		int res = getPermissionValue("badblock.viplevel");
		return res;
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
		for(BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers())
		{
			if(player == this || !player.canSee(this))
				return;

			player.hidePlayer(this);
			player.showPlayer(this);
		}
	}

	@Override
	public void addIntoMatchmaking(String worldSystem)
	{
		GamePlugin.getInstance().getPartyManager().getParty(getName(), new Callback<Party>()
		{
			@Override
			public void done(Party result, Throwable error)
			{
				String[] extraPlayers = new String[] {};

				if (result != null && result.getPlayers().containsKey(getName().toLowerCase()))
				{
					PartyPlayer player = result.getPlayers().get(getName().toLowerCase());
					if (PartyPlayerRole.ADMIN.equals(player.getRole()) && player.isFollow())
					{
						List<String> extra = new ArrayList<>();
						for (PartyPlayer partyPlayer : result.getPlayers().values())
						{
							if (partyPlayer.isFollow() && !player.getName().equalsIgnoreCase(partyPlayer.getName())
									&& PartyPlayerState.ACCEPTED.equals(partyPlayer.getState()))
							{
								extra.add(partyPlayer.getName());
							}
						}
						extraPlayers = new String[extra.size()];
						extraPlayers = extra.toArray(extraPlayers);
					}
				}

				new MatchmakingEnterRequest(getName().toLowerCase(), GamePlugin.getInstance().getCluster(), worldSystem, extraPlayers, System.currentTimeMillis())
				.send(
						GamePlugin.getInstance().getRabbitService()
						);
			}
		});
	}

	@Override
	public void sendActionBar(String message) {

	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		Set<PermissionAttachmentInfo> s = new HashSet<>();
		List<Permission> pms = new ArrayList<>();
		List<String> ranks = permissions.getValidRanks(GamePlugin.getInstance().getPermissionPlace());
		for (String rank : ranks)
		{
			Permissible permissible = PermissionsManager.getManager().getGroup(rank);
			
			if (permissible == null || permissible.getPermissions() == null)
			{
				continue;
			}
			
			for (PermissionSet set : permissible.getPermissions())
			{
				if (set == null || set.getPermissions() == null)
				{
					continue;
				}
				
				if (set.getPlaces() == null || !set.getPlaces().contains(GamePlugin.getInstance().getPermissionPlace()))
				{
					continue;
				}

				pms.addAll(set.getPermissions());
			}
		}

		List<PermissionSet> customPerms = permissions.getPermissions(GamePlugin.getInstance().getPermissionPlace());
		if (customPerms != null)
		{
			for (PermissionSet permission : customPerms)
			{
				if (permission.getPermissions() != null)
				{
					pms.addAll(permission.getPermissions());
				}
			}
		}

		for (Permission pm : pms)
		{
			String pmString = pm.isAll() ? pm.getPermission() + "*" : pm.getPermission();
			s.add(new PermissionAttachmentInfo(this, pmString, null, PermissionResult.YES.equals(pm.getResult())));
		}
		return s;
	}

	@Override
	public boolean isPermissionSet(String perm)
	{
		return hasPermission(perm);
	}

	@Override
	public boolean isPermissionSet(org.bukkit.permissions.Permission perm)
	{
		return hasPermission(perm.getName());
	}



}