package fr.badblock.ladder.entities;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import com.google.gson.JsonElement;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.utils.Punished;
import fr.badblock.ladder.data.LadderDataHandler;
import fr.badblock.permissions.Permissible;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.Permission;
import lombok.Getter;

public class LadderOfflinePlayer extends LadderDataHandler implements OfflinePlayer {
	@Getter protected final String				name;
	
	@Getter protected final PermissiblePlayer 	permissions;
	protected final 		Punished			punished;

	@Getter protected final InetAddress			lastAddress;
	
	@Getter private 		String				loginPassword;
	
	public Punished getPunished(){
		punished.checkEnd();
		return punished;
	}
	
	@Override
	public boolean hasPlayed() {
		return (this instanceof Player) || lastAddress != null || !permissions.getSuperGroup().equalsIgnoreCase("default") || getData().has("game");
	}
	
	public LadderOfflinePlayer(String name, InetAddress address) {
		super(Proxy.PLAYER_FOLDER, name);
		if(address == null && getData().has("lastIp"))
			try {
				address = InetAddress.getByName(getData().get("lastIp").getAsString());
			} catch (UnknownHostException unused){}
		

		UUID uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
		if (!getData().has("uniqueId"))
			getData().addProperty("uniqueId", uniqueId.toString());

		Proxy.getInstance().getOfflineCachePlayers().put(this.getNickName(), this);
		if (!getData().has("loginPassword"))
			getData().addProperty("loginPassword", "");
	
		this.name 		  = name;
		
		this.lastAddress  = address;
		this.permissions  = Proxy.getInstance().getPermissions().createPlayer(name, getData());
		this.punished     = Punished.fromJson(getData());
		
		Proxy.getInstance().getIpData(address);
		
		savePunishions();
	}
	
	@Override
	public void saveData(){
		punished.save(getData());
		//System.out.println("Save permissions in file (saveData()) > " + this.name + " > " + permissions.saveAsJson());
		
		getData().add("permissions", permissions.saveAsJson());
		
		if(lastAddress != null) {
			getData().addProperty("lastIp", lastAddress.getHostAddress());
		}
		
		super.saveData();
	}

	@Override
	public String getNickName() {
		if(getData().has("nickName"))
			return getData().get("nickName").getAsString();
		getData().addProperty("nickName", getName());
		this.saveData();
		return getName();
	}

	@Override
	public void setNickName(String nickName) {
		getData().addProperty("nickName", nickName);
		Player player = Ladder.getInstance().getPlayer(this.name);
		if (player != null) player.sendToBungee("nickName");
		saveData();
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return permissions.hasPermission(new Permission(permission));
	}

	@Override
	public Permissible getAsPermissible() {
		return permissions;
	}
	
	@Override
	public <T> T getPermissionValue(String key, Class<T> clazz) {
		JsonElement el = permissions.getValue(key);
		
		return el == null ? null : Ladder.getInstance().getGson().fromJson(permissions.getValue(key), clazz);
	}

	@Override
	public Punished getAsPunished() {
		return punished;
	}

	@Override
	public PlayerIp getIpData() {
		if(lastAddress == null) return null;
		return Proxy.getInstance().getIpData(lastAddress);
	}

	@Override
	public void savePunishions() {
		punished.checkEnd();
		punished.save(getData());
	}

	@Override
	public Punished getIpAsPunished() {
		return getIpData().getAsPunished();
	}
}
