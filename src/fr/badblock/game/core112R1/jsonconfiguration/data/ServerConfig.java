package fr.badblock.game.core112R1.jsonconfiguration.data;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerConfig {
	
	// Server bonus
	public double bonusXp;
	public double bonusCoins;

	// Selected i18n work folder
	public String i18nPath;

	// Permission place
	public String permissionPlace;
	
	// Cluster
	public String cluster;
	
	public Map<String, String> equiv = new HashMap<>();
	
}