package fr.badblock.api;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.api.kit.KitsManager;
import fr.badblock.api.listeners.minigame.AntiSpawnKillListener;
import fr.badblock.api.listeners.minigame.ConnectionListener;
import fr.badblock.api.listeners.minigame.DeathListener;
import fr.badblock.api.listeners.minigame.FightListener;
import fr.badblock.api.listeners.minigame.InteractListener;
import fr.badblock.api.listeners.minigame.KitsInventoryListener;
import fr.badblock.api.listeners.minigame.PingListener;
import fr.badblock.api.listeners.minigame.ProtectionListener;
import fr.badblock.api.listeners.minigame.SpawnProtectionListener;
import fr.badblock.api.listeners.minigame.SpectatorListener;
import fr.badblock.api.listeners.minigame.VoteListener;
import fr.badblock.api.runnables.BRunnable;
import fr.badblock.api.runnables.SignsRunnable;
import fr.badblock.api.scoreboard.BPlayerBoard;
import fr.badblock.api.scoreboard.BSpectatorBoard;
import fr.badblock.api.utils.bukkit.ConfigUtils;
import fr.badblock.bukkit.speak.Speaker;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;
import lombok.Getter;
import lombok.Setter;

public abstract class MJPlugin extends BPlugin {
	
	public static MJPlugin getInstance() {
		return (MJPlugin) BPlugin.getInstance();
	}

	@Getter @Setter public double					boostXP				= 1;
	@Getter @Setter public double					boostBadcoins		= 1;
	
	@Getter private static Speaker speaker;
	
	@Getter@Setter protected GameStatus status;
	@Getter@Setter protected Location spawn, spectatorSpawn;

	public abstract boolean canModifyMap();
	public abstract boolean canUseFlintAndSteel();
	public abstract boolean canExplode();
	public abstract boolean canBurn();
	public abstract boolean canSpawnCustom();
	public abstract boolean canSpawn();
	public abstract boolean canDropOrPickup();
	public abstract boolean canBeDamaged();
	public abstract boolean canLostFood();
	public abstract boolean canInteract(Material m);
	public abstract boolean canFight();
	public abstract boolean enableVote();
	public abstract List<String> getVotes();
	public abstract boolean enableKits();
	public abstract List<String> getKits();
	public abstract boolean initTeams();
	
	public abstract boolean respawnAtDeathPoint();
	public abstract boolean respawnAtTeamSpawn();
	public abstract boolean respawnAtSpectatorSpawn();
	public abstract boolean respawnAutomaticly();
	public abstract String deathMessageSuffix(PlayerDeathEvent e);
	public abstract boolean lightningOnDeath();
	public abstract boolean enableAntiSpawnKill();

	public abstract BPlayerBoard getPlayerBoard(BPlayer player);

	public abstract int minPlayer();
	public abstract int maxPlayer();

	public abstract void start();

	public abstract void enableGame();
	public abstract void disableGame();
	
	public abstract int getTime();
	
	public BPlayer createPlayer(Player p, boolean spectator){
		return new BPlayer(p, spectator);
	}
	
	public void begin(){
		if(BPlayersManager.getInstance().countPlayers() >= minPlayer() && status == GameStatus.WAITING_PLAYERS){
			status = GameStatus.STARTING;
			start();
		}
	}
	public boolean mustBeSpectator(){
		return (BPlayersManager.getInstance().countPlayers() >= maxPlayer() || status == GameStatus.PLAYING || status == GameStatus.ENDING);
	}

	public void kick(Player p){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(p.getName());
		out.writeUTF("lobby");
		p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
	
	@Override
	protected void enable() {
		status = GameStatus.WAITING_PLAYERS;
//		if(!getConfig().contains("database.ip")){
//			getConfig().set("database.ip", "127.0.0.1");
//		}
//		if(!getConfig().contains("database.port")){
//			getConfig().set("database.port", 3306);
//		}
//		if(!getConfig().contains("database.user")){
//			getConfig().set("database.user", "root");
//		}
//		if(!getConfig().contains("database.password")){
//			getConfig().set("database.password", "mdp");
//		}
//		if(!getConfig().contains("database.database")){
//			getConfig().set("database.database", "mj");
//		}
		
		if(!getConfig().contains("ladderAddress")){
			getConfig().set("ladderAddress.host", "01-sys.badblock-network.fr");
			getConfig().set("ladderAddress.port", 21850);
		}
		
//		new BSQLDatabase(getConfig().getString("database.ip"), getConfig().getInt("database.port"), getConfig().getString("database.user"), getConfig().getString("database.password"), getConfig().getString("database.database"));
		registerEvent(new SpawnProtectionListener());
		registerEvent(new SpectatorListener());
		registerEvent(new ConnectionListener(this));
		registerEvent(new InteractListener());
		registerEvent(new FightListener(this));
		registerEvent(new DeathListener(this));
		registerEvent(new ProtectionListener(this));
//		registerEvent(new Server(getConfig().getString("signs.host")));
		registerEvent(new PingListener(this));
		
		speaker = new Speaker(getConfig().getString("ladderAddress.host"), getConfig().getInt("ladderAddress.port"));
		new SignsRunnable().start();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		if(enableKits()){
			new KitsManager(new File(getDataFolder(), "kits"), getKits());
			registerEvent(new KitsInventoryListener());
		}
		if(enableAntiSpawnKill())
			registerEvent(new AntiSpawnKillListener());

		spawn = ConfigUtils.loadLocation(getConfig(), "spawn");
		spectatorSpawn = ConfigUtils.loadLocation(getConfig(), "spawn-spectator");

		enableGame();
		if(enableVote()){
			registerEvent(new VoteListener());
			VoteListener.generateInventory(getVotes());
		}
		
		new BRunnable(40L){
			@Override
			public void run(){
				Bukkit.getWorlds().get(0).setTime(getTime());
			}
		}.start();
		new BSpectatorBoard();
		saveConfig();
	}
	
	public double getMaxDistance(){
		return 300.0d;
	}
	
	@Override
	protected void disable(){
		SignsRunnable.getInstance().sendStatus(ServerStatus.STOPING);
		disableGame();
	}

	@Override
	protected boolean mustFixBugs() {
		return true;
	}

	@Override
	protected boolean mustManageChat() {
		return true;
	}
	@Override
	protected boolean enableEssentialsCommands(){
		return true;
	}
	@Override
	protected boolean enableBadblockCommand(){
		return true;
	}
	public static enum GameStatus {
		WAITING_PLAYERS(0),
		STARTING(0),
		PLAYING(1),
		ENDING(1);
		
		private int status;
		
		private GameStatus(int status){
			this.status = status;
		}
		
		public int status() {
			return status;
		}
	}
}
