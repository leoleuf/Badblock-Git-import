package fr.badblock.gameapi.utils.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Repr�sentante une cr�ature vivante (monstre, animal ou ambient).<br>
 * Ajoute des m�thodes non pr�sente par d�faut dans Bukkit (vitesse ou invincibilit� par exemple) �vitant de passer par NMS.<br>
 * Ajoute des m�thodes non pr�sente par d�faut dans Minecraft, principalement le comportement de la cr�ature (pouvoir voler, agressivit�, ...)<br>
 * <br>
 * Les CustomCreatures sont register lorsque le plugin API s'enable.<br>
 * Pour r�cup�rer, utiliser {@link fr.badblock.gameapi.utils.entities.CreatureUtils#getAsCustom(org.bukkit.entity.Entity)}
 * @author LeLanN
 */
public interface CustomCreature {
	/**
	 * R�cup�re le type de la cr�ature.
	 * @return Le type de la cr�ature.
	 */
	public CreatureType getEntityType();
	
	/**
	 * V�rifie si la cr�ature est sur le sol
	 */
	public boolean isOnGround();
	
	/**
	 * V�rifie si la cr�ature est dans la lave
	 */
	public boolean isInLava();
	
	/**
	 * V�rifie si la cr�ature est dans l'eau
	 */
	public boolean isInWater();

	/**
	 * V�rifie si la cr�ature est r�sistante au feu
	 */
	public boolean isFireProof();
	
	public void   setInvisible(boolean invisible);
	
	/**
	 * D�fini si la cr�ature est r�sistante au feu
	 * @param fireProof Sa r�sistance (ou non)
	 */
	public void    setFireProof(boolean fireProof);
	
	/**
	 * V�rifie si l'entit� est invincible (ne peut prendre aucun d�gat naturels, seul setHealth lui fera perdre de la vie)
	 */
	public boolean isInvincible();

	/**
	 * D�fini si la cr�ature est invincible (ne peut prendre aucun d�gats naturels, seul setHealth lui fera perdre de la vie) 
	 * @param invincible Si la cr�ature est invincible
	 */
	public void    setInvincible(boolean invincible);
	
	/**
	 * Change la vitesse de l'entit�
	 * @param speed La nouvelle vitesse
	 */
	public void    setSpeed(double speed);
	
	/**
	 * R�cup�re la vitesse de l'entit�
	 */
	public double  getSpeed();
	
	/**
	 * Change les d�gats fait par l'entit�
	 * @param damage Les nouveaux d�gats
	 */
	public void    setDamage(double damage);
	
	/**
	 * R�cup�re les d�gats fait par l'entit�
	 */
	public double  getDamage();
	
	/**
	 * V�rifie si l'entit� peut se d�placer. Si non, elle n'est plus soumise � la gravit�, n'a pas de knockback en combat, ...
	 */
	public boolean isMovable();
	
	/**
	 * Autorise ou non l'entit� � se d�placer
	 * @param movable Si l'entit� peut se d�placer
	 */
	public void    setMovable(boolean movable);
	
	/**
	 * V�rifie si l'entit� est agressive (va essayer de frapper un joueur si elle en voit un)
	 */
	public boolean isAgressive();

	/**
	 * D�fini si l'entit� est agressive (va essayer de frapper un joueur si elle en voit un).
	 * @param agressive Si l'entit� est agressive.
	 */
	public void    setAgressive(boolean agressive);
	
	/**
	 * V�rifie si l'entit� peut se faire monter (clique droit sur l'entit�) par n'importe quel joueur.
	 * Pour le cochon, il ne n�cessite plus de selle et de canne.
	 */
	public boolean isRideable();

	/**
	 * D�fini si l'entit� peut se faire monter (clique droit sur l'entit�) par n'importe quel joueur.
	 * @param rideable Si l'entit� peut se faire monter
	 */
	public void    setRideable(boolean rideable);
	
	/**
	 * V�rifie si l'entit� peut voler. Une entit� non mont�e qui vole ne va pas attaquer les joueurs et va se contenter de se d�placer rapidement (� la mani�re d'une chauve-souris).
	 */
	public boolean isAllowedToFly();

	/**
	 * D�fini si l'entit� peut voler. Une entit� non mont�e qui vole ne va pas attaquer les joueurs et va se contenter de se d�placer rapidement (� la mani�re d'une chauve-souris).
	 * @param allowed Si l'entit� peut voler.
	 */
	public void    setAllowedToFly(boolean allowed);
	
	/**
	 * Fait exploser l'entit�
	 * @param power La puissance de l'explosion
	 * @param flaming Si l'explosion provoque des flammes
	 * @param smoking Si l'explosion provoque de la fum�e
	 */
	public void    explode(Location location, float power, boolean flaming, boolean smoking);
	
	/**
	 * R�cup�re l'entit� Bukkit
	 * @return L'entit� Bukkit
	 */
	public Entity  getBukkit();
}
