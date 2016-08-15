package fr.badblock.common.docker.configuration;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class ConfigurationKey {
	public String				displayName;
	public String				key;
	public List<String>			possibleValues;
	
	public ConfigurationKey(String displayName, String key, List<String> possibleValues) {
		this.setDisplayName(displayName);
		this.setKey(key);
		this.setPossibleValues(possibleValues);
	}
}
