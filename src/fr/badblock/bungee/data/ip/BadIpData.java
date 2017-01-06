package fr.badblock.bungee.data.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.annotations.Expose;
import com.mongodb.BasicDBObject;

import fr.badblock.bungee.data.ip.threading.IpDataWorker;
import fr.badblock.commons.data.PunishData;
import lombok.Getter;
import lombok.Setter;

public class BadIpData {

	public static Map<String, BadIpData> ips = new HashMap<>();

	@Expose @Getter	public String						ip;
	@Expose @Getter	@Setter public BasicDBObject		data;

	public BadIpData(String ip) {
		this.ip = ip;
		loadData();
	}

	public void updateData() {
		data.put("ip", this.getIp());
		IpDataWorker.save(this);
	}

	public void loadData() {
		IpDataWorker.populate(this);
		if (!data.containsField("punish")) {
			data.put("punish", new PunishData());
			this.updateData();
		}
	}
	
	public PunishData getPunishData() {
		if (!data.containsField("punish")) {
			PunishData punishData = new PunishData();
			data.put("punish", punishData);
			this.updateData();
			return punishData;
		}
		return new PunishData((BasicDBObject) data.get("punish"));
	}

	public void updateDataFromClone(BadIpData badIpData) {
		this.setData(badIpData.getData());
	}

	public String getLastName() {
		if (!data.containsField("lastName")) data.put("lastName", "");
		return (String) data.get("lastName");
	}

	@SuppressWarnings("unchecked")
	public List<String> getUsernames() {
		if (!data.containsField("usernames")) data.put("usernames", new ArrayList<>());
		return (List<String>) data.get("usernames");
	}

	@SuppressWarnings("unchecked")
	public List<UUID> getUuids() {
		if (!data.containsField("uuids")) data.put("uuids", new ArrayList<>());
		return (List<UUID>) data.get("uuids");
	}

	public void addUsername(String name) {
		List<String> usernames = getUsernames();
		if (!usernames.contains(name)) {
			usernames.add(name);
			data.put("usernames", usernames);
		}
	}

	public void addUUID(UUID uuid) {
		List<UUID> uuids = getUuids();
		if (!uuids.contains(uuid)) {
			uuids.add(uuid);
			data.put("uuids", uuids);
		}
	}

	public void setLastName(String name) {
		data.put("lastName", name);
	}

}
