package fr.badblock.bungee.data.players;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.bson.BSONObject;

import com.google.gson.annotations.Expose;

import fr.badblock.bungee.data.players.threading.PlayerDataWorker;
import lombok.Getter;
import lombok.Setter;

public class BadOfflinePlayer {

	private boolean loaded = false;

	@Expose @Getter protected final InetAddress			lastAddress;
	@Getter @Setter public BSONObject 					data;
	@Expose @Getter public String	  					name;
	@Expose @Getter public UUID							uniqueId;

	public BadOfflinePlayer(String name, InetAddress address) {
		this.name = name;
		if(address == null && getData().containsField("lastIp"))
			try {
				address = InetAddress.getByName((String) getData().get("lastIp"));
			} catch (UnknownHostException unused){}

		this.lastAddress  = address;
		loadData();
	}

	public boolean hasPlayed() {
		return (this instanceof BadPlayer) || lastAddress != null;
	}

	public void updateData() {
		if (!loaded) {
			System.out.println("[BadBungee] Trying to updateData by using not-loaded data!");
			return;
		}
		if (this.lastAddress != null)
			getData().put("lastIp", this.lastAddress.getHostAddress());
		PlayerDataWorker.save(this);
	}

	private void loadData() {
		PlayerDataWorker.populate(this);
		boolean mustBeUpdated = false;
		// Name
		if (!getData().containsField("name")) {
			getData().put("name", name);
			mustBeUpdated = true;
		}else this.name = (String) getData().get("name");
		// UniqueId
		if (!getData().containsField("uniqueId")) {
			uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
			getData().put("uniqueId", uniqueId);
			mustBeUpdated = true;
		}else this.uniqueId = UUID.fromString((String) getData().get("uniqueId"));
		// Login password
		if (!getData().containsField("loginPassword")) {
			getData().put("loginPassword", "");
			mustBeUpdated = true;
		}
		// Online mode
		if (!getData().containsField("onlineMode")) {
			getData().put("onlineMode", "false");
			mustBeUpdated = true;
		}
		loaded = true;
		if (mustBeUpdated) {
			this.updateData();
		}
	}

}
