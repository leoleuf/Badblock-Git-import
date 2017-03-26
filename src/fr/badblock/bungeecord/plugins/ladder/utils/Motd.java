package fr.badblock.bungeecord.plugins.ladder.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor public class Motd {
	private String[] motd;
	private String   version;
	private String[] players;
	private int      maxPlayers;
}
