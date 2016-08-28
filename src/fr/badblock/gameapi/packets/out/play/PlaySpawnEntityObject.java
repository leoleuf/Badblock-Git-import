package fr.badblock.gameapi.packets.out.play;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import lombok.Getter;

/**
 * Packet faisant spawn un object.
 * 
 * @author LeLanN
 */
public interface PlaySpawnEntityObject extends BadblockOutPacket {
	/**
	 * R�cup�re l'ID de l'entit�
	 * 
	 * @return L'ID
	 */
	public int getEntityId();

	/**
	 * D�finit l'ID de l'entit�
	 * 
	 * @param id
	 *            L'ID
	 * @return Le packet
	 */
	public PlaySpawnEntityObject setEntityId(int id);

	/**
	 * R�cup�re le type de l'objet
	 * 
	 * @return Le type
	 */
	public SpawnableObjects getType();

	/**
	 * D�finit le type de l'objet
	 * 
	 * @param type
	 *            Le type
	 * @return Le packet
	 */
	public PlaySpawnEntityObject setType(SpawnableObjects type);

	/**
	 * R�cup�re la 'data' de l'entit� (voir http://wiki.vg/Object_Data pour plus
	 * d'informations)
	 * 
	 * @return La data
	 */
	public int getData();

	/**
	 * D�finit la 'data' de l'entit� (voir http://wiki.vg/Object_Data pour plus
	 * d'informations)
	 * 
	 * @param data
	 *            La data
	 * @return Le packet
	 */
	public PlaySpawnEntityObject setData(int data);

	/**
	 * R�cup�re la position
	 * 
	 * @return La position
	 */
	public Location getLocation();

	/**
	 * D�finit la position
	 * 
	 * @param location
	 *            La position
	 * @return Le packet
	 */
	public PlaySpawnEntityObject setLocation(Location location);

	/**
	 * R�cup�re la v�locit�
	 * 
	 * @return La v�locit�
	 */
	public Vector getVelocity();

	/**
	 * D�finit la v�locit�
	 * 
	 * @param velocity
	 *            La v�locit�
	 * @return Le packet
	 */
	public PlaySpawnEntityObject setVelocity(Vector velocity);

	/**
	 * Repr�sente les diff�rents objets pouvant �tre spawn avec
	 * {@link PlaySpawnEntityObject}
	 * 
	 * @author LeLanN
	 */
	public enum SpawnableObjects {
		/**
		 * Un bateau
		 */
		BOAT(1),
		/**
		 * Un item
		 */
		ITEM_STACK(2),
		/**
		 * Un minecart
		 */
		MINECART(10),
		/**
		 * Une TNT activ�e
		 */
		ACTIVATED_TNT(50),
		/**
		 * Un cristal de l'end
		 */
		ENDER_CRYSTAL(51),
		/**
		 * Une fl�che (lanc�e)
		 */
		ARROW_PROJECTILE(60),
		/**
		 * Une snowball (lanc�e)
		 */
		SNOWBALL_PROJECTILE(61),
		/**
		 * Un oeuf (lanc�)
		 */
		EGG_PROJECTILE(62),
		/**
		 * Le projectile des ghasts
		 */
		FIREBALL_PROJECTILE(63),
		/**
		 * Le projectile des blazes
		 */
		FIRECHARGE_PROJECTILE(64),
		/**
		 * Le projectile de l'enderpearl
		 */
		ENDERPEARL_PROJECTILE(65),
		/**
		 * Le projectile des Withers (les cr�nes)
		 */
		WITHERSKULL_PROJECTILE(66),
		/**
		 * Un block tombant
		 */
		FALLING_OBJECT(70),
		/**
		 * Une item frame
		 */
		ITEM_FRAME(71),
		/**
		 * Le projectile de l'oeuil de l'end (qui permet de trouver un portail)
		 */
		EYE_OF_ENDER_PROJECTILE(72),
		/**
		 * Un oeuf de dragon qui tombe
		 */
		FALLING_DRAGON_EGG(74),
		/**
		 * Le projectile d'une bottle d'xp
		 */
		EXP_BOTTLE_PROJECTILE(75),
		/**
		 * Une fus�e
		 */
		FIREWORK(76),
		/**
		 * Le noeud d'une laisse
		 */
		LEASH_KNOT(77),
		/**
		 * Un armor stand
		 */
		ARMOR_STAND(78),
		/**
		 * Le flotteur d'une c�ne � p�che
		 */
		FISHING_FLOAT(90);

		@Getter
		private byte id;

		SpawnableObjects(int id) {
			this.id = (byte) id;
		}

		public static SpawnableObjects getByValue(byte value) {
			for (SpawnableObjects c : values())
				if (c.getId() == value)
					return c;
			return null;
		}
	}
}
