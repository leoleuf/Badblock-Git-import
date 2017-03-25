package fr.badblock.bungeecord.ladder.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FileUtils {
	private static Gson gson = new GsonBuilder().setPrettyPrinting()
	         									.disableHtmlEscaping()
	         									.create();
	
	public static JsonArray loadArray(File file){
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader(file));

			return jsonElement.getAsJsonArray();
		} catch (FileNotFoundException e) {}

		return new JsonArray();
	}

	public static JsonObject loadObject(File file){
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader(file));

			return jsonElement.getAsJsonObject();
		} catch (FileNotFoundException e) {}

		return new JsonObject();
	}
	
	public static <T> T load(File file, Class<T> clazz){
		try {
			return gson.fromJson(new FileReader(file), clazz);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void save(File file, Object object, boolean indented){
		JsonElement element = gson.toJsonTree(object);
		save(file, element, indented);
	}
	
	public static void save(File file, JsonElement element, boolean indented){
		try {
			FileWriter writer = new FileWriter(file);  
			String toSave     = !indented ? element.toString() : gson.toJson(element);;
			
			writer.write(toSave);
			writer.close();
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
}
