package fr.badblock.ladder.http;

import java.util.Map;

import com.google.gson.JsonObject;

import lombok.Data;

@Data public abstract class LadderPage {
	private String path;
	
	public LadderPage(String path) {
		this.path = path;
	}
	
	public abstract JsonObject call(Map<String, String> input);
}
