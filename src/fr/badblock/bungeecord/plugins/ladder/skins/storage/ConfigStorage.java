package fr.badblock.bungeecord.plugins.ladder.skins.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigStorage {

	private static final String localefile = "config.json";
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static final ConfigStorage instance = new ConfigStorage();

	public static final ConfigStorage getInstance() {
		return instance;
	}

	public static void init(File datafolder) {
		File fullpath = new File(datafolder, localefile);
		try (FileReader reader = new FileReader(fullpath)) {
			ConfigStorage other = gson.fromJson(reader, ConfigStorage.class);
			for (Field field : ConfigStorage.class.getFields()) {
				Object value = field.get(other);
				if (value != null) {
					field.set(instance, value);
				}
			}
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
		}
		try (FileWriter writer = new FileWriter(fullpath)) {
			datafolder.mkdirs();
			writer.write(gson.toJson(instance));
		} catch (IOException e) {
		}
	}
    public boolean UPDATE_CHECK = true;
    public boolean USE_AUTOIN_SKINS = false;
    public boolean USE_BOT_FEATURE = true;
    public int SKIN_CHANGE_COOLDOWN = 30;
	public String GET_PROFILE_URL = "https://mcapi.ca/uuid/players/['{username}']";
	public String GET_SKIN_PROFILE_URL = "https://mcapi.ca/name/uuid/{uuid}";

}
