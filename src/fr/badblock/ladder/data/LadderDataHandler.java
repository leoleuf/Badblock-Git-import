package fr.badblock.ladder.data;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.data.DataHandler;
import fr.toenga.common.tech.mongodb.MongoService;
import fr.toenga.common.utils.general.GsonUtils;
import lombok.Getter;


public abstract class LadderDataHandler implements DataHandler {
	@Getter	private final String  key;
	@Getter	private final String  table;
	private final String	player;
	//private final File    file;
	private JsonObject    	data;
	protected boolean		loaded;
	AtomicBoolean saving  = new AtomicBoolean(false);
	AtomicBoolean reading = new AtomicBoolean(false);

	public LadderDataHandler(String table, String key)
	{
		this.table = table;
		this.key = key;
		this.player = key.toLowerCase();
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

		if (!loaded)
		{
			saving.set(false);
			throw new RuntimeException("[DEBUG-PERTE] Essaye de mettre � jour un fichier (" + player + ") non charg�...");
		}
		saving.set(true);
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
		if (!loaded) {
			throw new RuntimeException("[DEBUG-PERTE] Essaye de set des donn�es dans un fichier (" + player + ") non charg�...");
		}
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
		if (!loaded) 
		{
			throw new RuntimeException("[DEBUG-PERTE] Essaye de supprimer des donn�es dans un fichier (" + player + ") non charg�...");
		}
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

		MongoService mongoService = Proxy.getInstance().getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection(table);
		BasicDBObject query = new BasicDBObject();

		query.put("name", this.player);
		
		DBCursor cursor = collection.find(query); 
		boolean find = cursor.hasNext();
		
		if (!find)
		{
			Proxy.getInstance().getConsoleCommandSender().sendMessage("�c" + key + " doesn't exist in the player table.");
			data = new JsonObject();
			loaded = true;
			reading.set(false);
			cursor.close();
			return;
		}
		else
		{
			JsonParser parser = new JsonParser();
			data = parser.parse(JSON.serialize(cursor.next())).getAsJsonObject();
			cursor.close();
		}

		if (!loaded)
		{
			data = new JsonObject();
		}
		reading.set(false);
		if (!loaded)
		{
			throw new RuntimeException("[DEBUG-PERTE] Donn�es impossibles � charger (" + key + "), voir erreur au dessus. On est oblig� de le recr�er..");
		}

	}

	@Override
	public void saveData() {
		/*if(saving.get() || reading.get()){
			//throw new ConcurrentModificationException("Trying to save data file while saving or reading! [saving(" + saving.get() + ") reading(" + reading.get() + ")]");
		}*/MongoService mongoService = Proxy.getInstance().getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection(table);
		BasicDBObject query = new BasicDBObject();

		query.put("name", this.player);
		
		DBCursor cursor = collection.find(query); 
		boolean find = cursor.hasNext();
		cursor.close();

		if (!loaded && find) 
		{
			return;
		}
		saving.set(true);

		DataSavers.save(this, data, false);
	}

	public void saveSync(JsonObject object, boolean update){
		if(update)
			addObjectInObject(data, object);
		else data = object;
		MongoService mongoService = Proxy.getInstance().getMongoService();
		DB db = mongoService.getDb();
		DBCollection collection = db.getCollection(table);
		BasicDBObject query = new BasicDBObject();

		query.put("name", this.player);
		
		DBCursor cursor = collection.find(query); 
		boolean find = cursor.hasNext();

		cursor.close();
		
		if (!loaded && find) {
			saving.set(false);
			throw new RuntimeException("[DEBUG-PERTE] Essaye de sauvegarder une donn�e (" + key + ") non charg�...");
		}

		if (!data.entrySet().isEmpty())
		{
			collection.insert((DBObject) JSON.parse(GsonUtils.getGson().toJson(data)));
		}

		saving.set(false);
	}
}
