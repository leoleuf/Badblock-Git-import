package fr.badblock.gameapi.fakeentities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.packets.out.play.PlayEntityStatus.EntityStatus;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Repr�sente une entit� qui n'est pas g�r�e c�t� serveur. Ne peut �tre qu'une
 * entit� vivante.
 * 
 * @author LeLanN
 *
 * @param <T> Le watcher correspondant au type de l'entit� g�r�e.
 */
public interface FakeEntity<T extends WatcherEntity> {
	/**
	 * Supprime l'entit� si ce n'est pas fait et la supprime du cache (ne pourra
	 * plus �tre r�utilis�e).
	 */
	public void destroy();

	/**
	 * R�cup�re la rotation de la t�te de l'entit�
	 * 
	 * @return La position de la t�te
	 */
	public float getHeadYaw();

	/**
	 * L'entit� de l'ID
	 * 
	 * @return L'ID
	 */
	public int getId();

	/**
	 * R�cup�re la position du joueur
	 * 
	 * @return La position
	 */
	public Location getLocation();

	/**
	 * R�cup�re le type de l'entit�
	 * 
	 * @return Le type
	 */
	public EntityType getType();

	/**
	 * R�cup�re la visibilit� de l'entit�
	 * 
	 * @return La visibilit�
	 */
	public Visibility getVisibility();

	/**
	 * R�cup�re les watchers de l'entit�
	 * 
	 * @return Les watchers
	 */
	public T getWatchers();

	/**
	 * V�rifie si l'entit� est remove.
	 * 
	 * @return Si elle est remove.
	 */
	public boolean isRemoved();

	/**
	 * Tue (naturellement) l'entit�. Autrement, m�me effet que remove().
	 */
	public void kill();

	/**
	 * D�place (en essayant de le faire naturellement) l'entit�
	 * 
	 * @param location
	 *            La nouvelle position
	 */
	public void move(Location location);

	/**
	 * Donne un statut � l'entit�
	 * 
	 * @param status
	 *            Le statut
	 */
	public void playStatus(EntityStatus status);

	/**
	 * Supprime l'entit�. L'entit� pour �tre respawn. Pour la supprimer de la
	 * m�moire utiliser {@link destroy}.
	 */
	public void remove();

	/**
	 * Supprime l'entit� pour un seul joueur.
	 * 
	 * @param player Le joueur
	 * @deprecated
	 */
	public default void remove(BadblockPlayer player){
		removePlayer(EntityViewList.WHITELIST, player);
	}

	/**
	 * Add player in a view list
	 * @param list The list
	 * @param player The player
	 */
	public void addPlayer(EntityViewList list, BadblockPlayer player);
	
	/**
	 * Remove player from a view list
	 * @param list The list
	 * @param player The player
	 */
	public void removePlayer(EntityViewList list, BadblockPlayer player);
	
	/**
	 * Check if the player is in a view list
	 * @param list The list
	 * @param player The player
	 * @return Boolean
	 */
	public boolean isPlayerIn(EntityViewList list, BadblockPlayer player);
	
	/**
	 * Si le joueur voit l'entit�
	 * 
	 * @param player
	 *            Le joueur
	 * @return Si il la voit
	 */
	@Deprecated
	public default boolean see(BadblockPlayer player){
		return isPlayerIn(EntityViewList.WHITELIST, player);
	}

	/**
	 * Change une partie de l'�quimement de l'entit�
	 * 
	 * @param equipmentSlot Le slot
	 * @param itemstack L'item
	 */
	public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemstack);

	/**
	 * Modifie la rotation de la t�te de l'entit�
	 * 
	 * @param yaw La position de la t�te
	 */
	public void setHeadYaw(float yaw);

	/**
	 * D�finit la visibilit� de l'entit�
	 * 
	 * @param visibility La visibilit�
	 */
	public FakeEntity<?> setVisibility(Visibility visibility);

	/**
	 * Affiche l'entit� � un joueur.
	 * 
	 * @param player Le joueur
	 */
	@Deprecated
	public default void show(BadblockPlayer player){
		addPlayer(EntityViewList.WHITELIST, player);
	}

	/**
	 * T�l�porte l'entit�
	 * 
	 * @param location *            La nouvelle position
	 */
	public void teleport(Location location);

	/**
	 * Update les watchers de l'entit� (apr�s qu'ils aient �t� modifi�e)
	 */
	public void updateWatchers();
	
	/**
	 * Check if the player can see the entity judging by the visibility and view lists
	 * @param player The player
	 * @return Boolean
	 */
	public default boolean canSeeEntity(BadblockPlayer player){
		switch(getVisibility()){
			case PLAYER: return isPlayerIn(EntityViewList.WHITELIST, player);
			case SERVER: return isPlayerIn(EntityViewList.BLACKLIST, player);
		}
		
		return false;
	}
	
	public enum EntityViewList {
		/**
		 * Use with {@link Visibility#SERVER}
		 */
		BLACKLIST,
		/**
		 * Use with {@link Visibility#PLAYER}
		 */
		WHITELIST;
	}
	
	public enum Visibility {
		SERVER, PLAYER;
	}
}
