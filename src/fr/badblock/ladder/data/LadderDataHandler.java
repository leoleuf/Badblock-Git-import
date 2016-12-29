package fr.badblock.ladder.data;

import java.io.File;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.data.DataHandler;
import fr.badblock.ladder.api.utils.FileUtils;
import lombok.Getter;


public abstract class LadderDataHandler implements DataHandler {
	@Getter	private final String  key;
	private final File    file;
	private JsonObject    data;
	private boolean		loaded;
	AtomicBoolean saving  = new AtomicBoolean(false);
	AtomicBoolean reading = new AtomicBoolean(false);

	public LadderDataHandler(File folder, String key){
		this.key = key;
		this.file = new File(folder, key.toLowerCase() + ".dat");
		reloadData();
	}

	@Override
	public JsonObject getData() {
		return data;
	}

	@Override
	public void updateData(JsonObject object) {
		/*if(saving.get() || reading.get()){
			throw new ConcurrentModificationException("Trying to update data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/
		saving.set(true);

		if (!loaded && file.exists()) System.out.println("Update data (not loaded) " + file.getName());
		DataSavers.save(this, object, true);
		//addObjectInObject(data, object);
		//saveData();
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
		/*if(saving.get() || reading.get()){
			throw new ConcurrentModificationException("Trying to set data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/
		if (!loaded && file.exists()) System.out.println("Set data (not loaded) " + file.getName());
		saving.set(true);
		DataSavers.save(this, object, false);
		//this.data = object;
		//saveData();
	}

	@Override
	public void removeData() {
		/*if(saving.get() || reading.get()){
			throw new ConcurrentModificationException("Trying to remove data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/
		if (!loaded && file.exists()) System.out.println("Removed data " + file.getName());
		saving.set(true);
		
		//file.delete();
		//data = new JsonObject();
		DataSavers.save(this, new JsonObject(), false);
	}

	@Override
	public void reloadData() {
		/*if(saving.get() || reading.get()){
			throw new ConcurrentModificationException("Trying to read data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/
		reading.set(true);
		
		if(!file.exists()) {
			data = new JsonObject();
			reading.set(false);
			return;
		}

		try {
			data = FileUtils.loadObject(file);
			loaded = true;
		} catch(Exception e){
			data = new JsonObject();
			e.printStackTrace();
		}
		
		reading.set(false);
	}

	@Override
	public void saveData() {
		/*if(saving.get() || reading.get()){
			throw new ConcurrentModificationException("Trying to save data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/
		if (!loaded && file.exists()) System.out.println("Save data not loaded :-( " + file.getName());
		saving.set(true);
		
		DataSavers.save(this, data, false);
	}
	
	public void saveSync(JsonObject object, boolean update){
		if(update)
			addObjectInObject(data, object);
		else data = object;

		if (!loaded && file.exists()) System.out.println("Not loaded " + file.getName());
		
		if(!data.entrySet().isEmpty())
			FileUtils.save(file, data, true);
		else if(file.exists()) {
			file.delete();
			System.out.println("File empty! " + file.getName());
		}
	
		saving.set(false);
	}
}
