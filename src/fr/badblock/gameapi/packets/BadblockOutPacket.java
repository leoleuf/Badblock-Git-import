package fr.badblock.gameapi.packets;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Repr�sente un packet MineCraft, allant du serveur au client, tel qu'il est
 * g�r� par l'API.<br>
 * Ces packets ne sont pas interc�pt�s et sont simplement envoy� sans passer par
 * un traitement via le serveur.<br>
 * Ils peuvent �tre tr�s utiles, par exemple pour les fausses entit�s ou les
 * titles non g�r� par l'API Bukkit/Spigot en 1.8.<br>
 * <br>
 * De ce fait tous les packets ne sont pas repr�sent�s. En effet :
 * <ul>
 * <li>Pour certains, l'API Bukkit/Spigot est largement suffisante (par exemple
 * [Player].spigot().respawn() pour le packet de respawn)</li>
 * <li>Pour d'autres, il peut �tre 'dangeureux' ou simplement inefficace
 * d'envoy� le packet sans que le serveur en soit inform�</li>
 * </ul>
 * <br>
 * Si un packet n'est pas assez document�, penser � se documenter gr�ce �
 * http://wiki.vg/Protocol<br>
 * Pour instancier un packet, voir
 * {@link fr.badblock.gameapi.GameAPI#createPacket(Class)}<br>
 * La classe n'est pas � confondre avec {@link BadblockInPacket}
 * 
 * @author LeLanN
 */
public interface BadblockOutPacket extends BadblockPacket {
	/**
	 * Envoit le packet � un joueur.
	 * 
	 * @param player
	 *            Le joueur
	 */
	public void send(BadblockPlayer player);
}
