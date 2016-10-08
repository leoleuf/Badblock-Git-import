package fr.badblock.gameapi.packets.out.play;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;

/**
 * Packet envoy� au joueur quand un autre joueur entre dans son champs de vision
 * 
 * @author LeLanN
 */
public interface PlayNamedEntitySpawn extends BadblockOutPacket {
	/**
	 * R�cup�re l'ID de l'entit�
	 * 
	 * @return L'ID
	 */
	public int getEntityId();

	/**
	 * R�cup�re la position
	 * 
	 * @return La position
	 */
	public Location getLocation();

	/**
	 * R�cup�re l'UUID
	 * 
	 * @return L'UUID
	 */
	public UUID getUniqueId();
	
	/**
	 * R�cup�re le type d'objet dans la main
	 * @return Le type
	 */
	public Material getItemInHand();

	/**
	 * R�cup�re les watchers de l'entit�
	 * 
	 * @return Les watchers
	 */
	public WatcherEntity getWatchers();

	/**
	 * D�finit l'ID de l'entit�
	 * 
	 * @param id
	 *            L'ID
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setEntityId(int id);

	/**
	 * D�finit la position
	 * 
	 * @param location
	 *            La position
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setLocation(Location location);

	/**
	 * D�finit l'UUID
	 * 
	 * @param uniqueId
	 *            L'UUID
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setUniqueId(UUID uniqueId);

	/**
	 * D�finit le type d'objet dans la main
	 * 
	 * @param type
	 *            Le type
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setItemInHand(Material type);

	
	/**
	 * D�finit les watchers de l'entit�
	 * 
	 * @param watcher
	 *            Les watchers
	 * @return Le packet
	 */
	public PlayNamedEntitySpawn setWatchers(WatcherEntity watcher);

}
