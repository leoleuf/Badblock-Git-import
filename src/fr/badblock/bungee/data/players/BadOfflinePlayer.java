package fr.badblock.bungee.data.players;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.bson.BSONObject;

import fr.badblock.bungee.data.threading.PlayerDataWorker;
import lombok.Getter;
import lombok.Setter;

public class BadOfflinePlayer {

	@Getter protected final InetAddress			lastAddress;
	@Getter @Setter public BSONObject 			data;
	@Getter public String	  					name;
	@Getter public UUID							uniqueId;
	
	public BadOfflinePlayer(String name, InetAddress address) {
		this.name = name;
		loadData();
		if(address == null && getData().containsField("lastIp"))
			try {
				address = InetAddress.getByName((String) getData().get("lastIp"));
			} catch (UnknownHostException unused){}
		boolean mustBeUpdated = false;
		uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
		if (!getData().containsField("uniqueId")) {
			getData().put("uniqueId", uniqueId.toString());
			mustBeUpdated = true;
		}
		else uniqueId = UUID.fromString((String) getData().get("uniqueId"));
		if (!getData().containsField("name")) {
			getData().put("name", name);
			mustBeUpdated = true;
		}
		if (!getData().containsField("loginPassword")) {
			getData().put("loginPassword", "");
			mustBeUpdated = true;
		}
		if (!getData().containsField("onlineMode")) {
			getData().put("onlineMode", "false");
			mustBeUpdated = true;
		}

		this.lastAddress  = address;
		if (mustBeUpdated) {
			this.updateData();
		}
	}
	
	public boolean hasPlayed() {
		return (this instanceof BadPlayer) || lastAddress != null;
	}
	
	public void updateData() {
		PlayerDataWorker.save(this);
	}
	
	private void loadData() {
		PlayerDataWorker.populate(this);
	}
	
}
