package fr.badblock.minecraftserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {
	public static Gson	gson 	   = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT)
			 .disableHtmlEscaping().create();
	public static Gson	prettyGson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT)
			 .disableHtmlEscaping()
			 .setPrettyPrinting().create();


	public static JsonArray loadArray(File file){
		if(!file.exists() || file.length() == 0){
			save(file, "[]");
		}
		
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(getInputStream(file));

			return jsonElement.getAsJsonArray();
		} catch (FileNotFoundException unused) {

		} catch(JsonParseException | UnsupportedEncodingException e){
			e.printStackTrace();
		}

		return new JsonArray();
	}

	private static InputStreamReader getInputStream(File file) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(new FileInputStream(file), "UTF-8");
	}

	public static JsonObject loadObject(File file){
		if(!file.exists() || file.length() == 0){
			save(file, "{}");
		}
		
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(getInputStream(file));

			return jsonElement.getAsJsonObject();
		} catch(JsonParseException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(FileNotFoundException unused){}

		return new JsonObject();
	}

	public static <T> T load(File file, Class<T> clazz){
		try {
			if(!file.exists())
				save(file, "{}");
			
			return gson.fromJson(getInputStream(file), clazz);
		} catch (JsonSyntaxException | JsonIOException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch(FileNotFoundException unused){
			return null;
		}
	}

	public static <T> T convert(JsonElement element, Class<T> clazz){
		return gson.fromJson(element, clazz);
	}

	public static void save(File file, Object object, boolean indented){
		JsonElement element = gson.toJsonTree(object);
		save(file, element, indented);
	}

	public static void save(File file, JsonElement element, boolean indented){
		String toSave = !indented ? gson.toJson(element) : prettyGson.toJson(element);;
		save(file, toSave);
	}

	public static void save(File file, String toSave){
		try {
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

			writer.write(toSave);
			writer.close();
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
}
