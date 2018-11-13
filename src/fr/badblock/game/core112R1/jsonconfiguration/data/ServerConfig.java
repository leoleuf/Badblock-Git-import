package fr.badblock.game.core112R1.jsonconfiguration.data;

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
	
}