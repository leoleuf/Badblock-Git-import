package fr.badblock.ladder.api.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import fr.badblock.ladder.api.Ladder;

public class FileUtils {
	private static Gson gson = new GsonBuilder().setPrettyPrinting()
			.disableHtmlEscaping()
			.create();

	public static JsonArray loadArray(File file){
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader(file));

			return jsonElement.getAsJsonArray();
		} catch (FileNotFoundException e) {

		} catch(JsonParseException e){
			try {
				/**
				 * Le JSON de certains fichiers est parfois corrompus avec une partie du fichier dupliquer à la fin ...
				 * C'est la seule situation observée du coup si erreur on essaye de récupérer juste le début
				 */
				
				String json = IOUtils.getContent(file);
				json = tryRepairJson(json, "[", "]");

				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(new FileReader(file));

				return jsonElement.getAsJsonArray();
			} catch (Exception exception){
				Ladder.getInstance().getLogger().log(Level.SEVERE, "Impossible de lire le fichier JSON " + file.getName() + " !", exception);
				
				if(file.exists()){
					/**
					 * Si on n'arrive tout de même pas à récupérer les données (snif) on sauvegarde en tant que corrompu pour pouvoir en récupérer une partie (si possible)
					 * On renome donc le fichier en question en <fichier>.json_corruptedNBR
					 */
					file.renameTo(new File(file.getAbsoluteFile().getParentFile(), file.getName() + "_corrupted" + new Random().nextInt(29377438)));
				}
				
				return new JsonArray(); // dans tous les cas, on renvoit un JsonObject pour ne pas interrompre l'action.
			}
		}

		return new JsonArray();
	}

	public static JsonObject loadObject(File file){
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader(file));

			return jsonElement.getAsJsonObject();
		} catch (FileNotFoundException e) {
			
		} catch(JsonParseException e){
			String json = "";
			try {
				/**
				 * Le JSON de certains fichiers est parfois corrompus avec une partie du fichier dupliquer à la fin ...
				 * C'est la seule situation observée du coup si erreur on essaye de récupérer juste le début
				 */
				
				 IOUtils.getContent(file);
				json = tryRepairJson(json, "{", "}");

				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(new FileReader(file));

				return jsonElement.getAsJsonObject();
			} catch (Exception exception){
				Ladder.getInstance().getLogger().log(Level.SEVERE, "Impossible de lire le fichier JSON " + file.getName() + " !\nContenu : " + json, exception);
				
				if(file.exists()){
					/**
					 * Si on n'arrive tout de même pas à récupérer les données (snif) on sauvegarde en tant que corrompu pour pouvoir en récupérer une partie (si possible)
					 * On renome donc le fichier en question en <fichier>.json_corruptedNBR
					 */
					file.renameTo(new File(file.getAbsoluteFile().getParentFile(), file.getName() + "_corrupted" + new Random().nextInt(29377438)));
				}
				
				return new JsonObject(); // dans tous les cas, on renvoit un JsonObject pour ne pas interrompre l'action.
			}
		}

		return new JsonObject();
	}

	private static String tryRepairJson(String data, String open, String close){
		String result = "";
		boolean isString = false, isEchap = false;
		int numberOpen = 1;
		
		for(int i=1;i<data.length();i++){
			String c = Character.toString(data.charAt(i));
			
			if(c.equals("\"")){
				if(!isEchap)
					isString = !isString;
				else isEchap = false;
			} else if(c.equals("\\") && isString) {
				isEchap = !isEchap;
			} else if(isString) {
				isEchap = false;
			} else if(c.equals(open) && !isString){
				numberOpen++;
			} else if(c.equals(close) && !isString){
				numberOpen--;
				if(numberOpen == 0) break;
			}

			result += c.charAt(i);
		}

		return result;
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
