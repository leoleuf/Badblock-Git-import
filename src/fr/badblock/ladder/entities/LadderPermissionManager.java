package fr.badblock.ladder.entities;

import java.io.File;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.badblock.ladder.api.data.DataHandler;
import fr.badblock.ladder.api.utils.FileUtils;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;

public class LadderPermissionManager extends PermissionManager {
	private final File file;

	public LadderPermissionManager(File configuration){
		super(FileUtils.loadArray(configuration));

		this.file = configuration;
	}

	public JsonElement handlePermissionPacket(DataAction data, String key){
		if(data != DataAction.REQUEST) {
			return new JsonObject(); // Modification isn't allowed
		}

		if(key.equals("*")){
			return saveAsJson();
		} else if(key.equalsIgnoreCase("list")){
			JsonArray result = new JsonArray();

			for(String group : groups.keySet()){
				result.add(new JsonPrimitive(group));
			}

			return result;
		} else {
			PermissibleGroup group = groups.get(key.toLowerCase());

			if(group == null)
				return new JsonObject();
			else return group.saveAsJson();
		}
	}

	public PermissiblePlayer createPlayer(String name, DataHandler playerData) {
		JsonObject object = playerData.getData();
		JsonObject permissions = new JsonObject();

		if (object.get("permissions") != null) {
			System.out.println("Fetch permissions in DataHandler json object (createPlayer() - " + name + ") > " + object.get("permissions").getAsJsonObject());
			permissions = object.get("permissions").getAsJsonObject();
		} else {
			System.out.println("Added 'permissions' in data json object (createPlayer() - " + name + ")");
			object.add("permissions", permissions);
		}

		if (permissions.entrySet().isEmpty()) {
			System.out.println("Entry set empty 'permissions', set to default group (createPlayer() - " + name + ")");
			permissions.addProperty("group", "default");
			permissions.addProperty("end", Integer.valueOf(-1));
			permissions.add("permissions", new JsonArray());
		}

		return new PermissiblePlayer(name, permissions);
	}

	public void save() {
		FileUtils.save(this.file, saveAsJson(), true);
	}
}
