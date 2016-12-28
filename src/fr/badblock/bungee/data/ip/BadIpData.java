package fr.badblock.bungee.data.ip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.BSONObject;

import com.google.gson.annotations.Expose;

import fr.badblock.bungee.data.ip.threading.IpDataWorker;
import fr.badblock.commons.data.PunishData;
import lombok.Getter;
import lombok.Setter;

public class BadIpData {
	
	public static Map<String, BadIpData> ips = new HashMap<>();

	@Expose @Getter	public String						ip;
	@Expose @Getter public PunishData					punish;
	@Getter	@Setter public BSONObject					data;
	
	public BadIpData(String ip) {
		this.ip = ip;
		loadData();
		ips.put(ip, this);
	}
	
	public void updateData() {
		data.put("punish", punish);
		data.put("ip", this.getIp());
		IpDataWorker.save(this);
	}
	
	private void loadData() {
		IpDataWorker.populate(this);
		if (!data.containsField("punish")) {
			punish = new PunishData();
			data.put("punish", punish);
		}
		else punish = (PunishData) data.get("punish");
		data.put("ip", this.getIp());
	}

	public void updateDataFromClone(BadIpData badIpData) {
		this.punish = badIpData.getPunish();
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
		if (!getUsernames().contains(name))
			data.put("usernames", getUsernames().add(name));
	}
	
	public void addUUID(UUID uuid) {
		if (!getUuids().contains(uuid))
			data.put("uuids", getUuids().add(uuid));
	}
	
	public void setLastName(String name) {
		data.put("lastName", name);
	}
	
}
