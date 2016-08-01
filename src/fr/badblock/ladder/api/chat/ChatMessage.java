package fr.badblock.ladder.api.chat;

import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;

/**
 * Repésente un message (raw message, title, action bar)
 * @author LeLanN
 */
public interface ChatMessage {
	/**
	 * Envoit le message aux joueurs
	 * @param players Les joueurs
	 */
	public void send(final Player... players);
	
	/**
	 * Envoit le message aux serveurs bungeecords
	 * @param servers Les serveurs
	 */
	public void broadcast(final BungeeCord... servers);
	
	/**
	 * Envoit le message à tous les bungeecords
	 */
	public void broadcastAll();
}
