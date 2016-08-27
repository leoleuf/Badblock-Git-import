package fr.badblock.ladder.data;

import java.io.File;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import fr.badblock.ladder.api.data.DataHandler;
import fr.badblock.ladder.api.utils.FileUtils;

public abstract class LadderDataHandler implements DataHandler {
	private final File   file;
	private JsonObject   data;

	public LadderDataHandler(File folder, String key){
		this.file = new File(folder, key.toLowerCase() + ".dat");

		reloadData();
	}

	@Override
	public JsonObject getData() {
		return data;
	}

	@Override
	public void updateData(JsonObject object) {
		addObjectInObject(data, object);
		saveData();
	}

	private void addObjectInObject(JsonObject base, JsonObject toAdd){
		for(final Entry<String, JsonElement> entry : toAdd.entrySet()){
			String key = entry.getKey();
			JsonElement element = toAdd.get(key);

			if(!base.has(key) || !element.isJsonObject()){
				base.add(key, element);
			} else {
				addObjectInObject(base.get(key).getAsJsonObject(), element.getAsJsonObject());
			}
		}
	}

	@Override
	public void setData(JsonObject object) {
		this.data = object;
		saveData();
	}

	@Override
	public void removeData() {
		file.delete();
		data = new JsonObject();
	}

	@Override
	public void reloadData() {
		if(!file.exists()) {
			data = new JsonObject();
			return;
		}

		try {
			data = FileUtils.loadObject(file);
		} catch(Exception e){
			data = new JsonObject();
		}
	}

	@Override
	public void saveData() {
		DataSavers.save(this);
	}
	
	public void saveSync(){
		if(!data.entrySet().isEmpty())
			FileUtils.save(file, data, true);
		else if(file.exists()) file.delete();
	}
}
