package fr.badblock.ladder.api.entities;

import java.net.InetAddress;

import fr.badblock.ladder.api.data.DataHandler;
import fr.badblock.ladder.api.utils.Punished;
import fr.badblock.permissions.Permissible;

public interface OfflinePlayer extends DataHandler {
	/**
	 * Récupère le pseudo du joueur
	 * @return Son pseudo
	 */
	public String 	   getName();
	
	public String      getNickName();
	
	public void		   setNickname(String nickName);
	
	/**
	 * Vérifie si le joueur à une permission
	 * @param permission La permission
	 * @return Si le joueur a la permission ou non
	 */
	public boolean 	   hasPermission(String permission);
	
	/**
	 * Renvoit les permissions du joueur. Utilisé principalement par Ladder, n'utiliser que si bonne raison.
	 * @return Les permissions du joueur
	 */
	public Permissible getAsPermissible();
	
	public <T> T getPermissionValue(String key, Class<T> clazz);
	
	/**
	 * Renvoit le joueur comme une entité pouvant être punnie (ban & mute)
	 * @return Le joueur comme pouvant être punni
	 */
	public Punished    getAsPunished();
	
	/**
	 * Renvoit le DataHandler de l'IP du joueur
	 * @return Le DataHandler
	 */
	public PlayerIp   getIpData();
	
	/**
	 * Renvoit l'IP du joueur comme une entitée pouvant être punnie (ban & mute)
	 * @return L'IP comme pouvant être punnie
	 */
	public Punished    getIpAsPunished();
	
	public void		   savePunishions();
	
	public InetAddress getLastAddress();
	
	public boolean     hasPlayed();
}
