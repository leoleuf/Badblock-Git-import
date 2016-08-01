package fr.badblock.ladder.api.data;

import com.google.gson.JsonObject;

public interface DataHandler {
	public JsonObject getData();
	
	public void 	  reloadData();
	public void       updateData(JsonObject object);
	public void		  setData(JsonObject object);
	
	public void 	  removeData();
	public void 	  saveData();
}
