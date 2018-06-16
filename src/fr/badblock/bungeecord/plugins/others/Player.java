package fr.badblock.bungeecord.plugins.others;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.bungeecord.plugins.others.modules.BadPseudoModule;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Getter
@Setter
public class Player {

	private static Map<String, Player> players = new HashMap<>();

	private String pseudo;
	private long lastMessageTime;
	private String lastMessage;
	private Map<String, Long> spamMessages;

	public Player(ProxiedPlayer player) {
		if (BadPseudoModule.getInstance().check(player))
			return;
		this.pseudo = player.getName();
		this.spamMessages = new HashMap<>();
		players.put(pseudo, this);
	}

	public void remove() {
		players.remove(pseudo);
	}

	public static Player get(ProxiedPlayer proxiedPlayer) {
		if (!players.containsKey(proxiedPlayer.getName()))
			return new Player(proxiedPlayer);
		return players.get(proxiedPlayer.getName());
	}

}
