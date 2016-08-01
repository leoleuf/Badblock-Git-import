package fr.badblock.ladder.http;

import com.google.gson.JsonObject;

import lombok.Data;

@Data public abstract class LadderPage {
	private String path;
	
	public LadderPage(String path) {
		this.path = path;
	}
	
	public abstract JsonObject call(JsonObject input);
}
