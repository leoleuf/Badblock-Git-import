package fr.badblock.gameapi.utils.entities;

import java.util.List;

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
	 * D�finit si l'entit� est invisible
	 * @param invisible Si elle est invisible
	 * @deprecated Utiliser {@link #addCreatureFlag(CreatureFlag)} et {@link #removeCreatureFlag(CreatureFlag)} � la place
	 */
	default void setInvisible(boolean invisible){
		if(invisible)
			addCreatureFlag(CreatureFlag.INVISIBLE);
		else removeCreatureFlag(CreatureFlag.INVISIBLE);
	}

	/**
	 * V�rifie si l'entit� est invisible
	 * @deprecated Utiliser {@link #hasCreatureFlag(CreatureFlag)} � la place
	 */
	default boolean isInvisible(){
		return hasCreatureFlag(CreatureFlag.INVISIBLE);
	}

	/**
	 * V�rifie si la cr�ature est r�sistante au feu
	 * @deprecated Utiliser {@link #hasCreatureFlag(CreatureFlag)} � la place
	 */
	default boolean isFireProof(){
		return hasCreatureFlag(CreatureFlag.FIREPROOF);
	}

	/**
	 * D�fini si la cr�ature est r�sistante au feu
	 * @param fireProof Sa r�sistance (ou non)
	 * @deprecated Utiliser {@link #addCreatureFlag(CreatureFlag)} et {@link #removeCreatureFlag(CreatureFlag)} � la place
	 */
	default void setFireProof(boolean fireProof){
		if(fireProof)
			addCreatureFlag(CreatureFlag.FIREPROOF);
		else removeCreatureFlag(CreatureFlag.FIREPROOF);
	}

	/**
	 * V�rifie si l'entit� est invincible (ne peut prendre aucun d�gat naturels, seul setHealth lui fera perdre de la vie)
	 * @deprecated Utiliser {@link #hasCreatureFlag(CreatureFlag)} � la place
	 */
	default boolean isInvincible(){
		return hasCreatureFlag(CreatureFlag.INVINCIBLE);
	}

	/**
	 * D�fini si la cr�ature est invincible (ne peut prendre aucun d�gats naturels, seul setHealth lui fera perdre de la vie) 
	 * @param invincible Si la cr�ature est invincible
	 * @deprecated Utiliser {@link #addCreatureFlag(CreatureFlag)} et {@link #removeCreatureFlag(CreatureFlag)} � la place
	 */
	default void setInvincible(boolean invincible){
		if(invincible)
			addCreatureFlag(CreatureFlag.INVINCIBLE);
		else removeCreatureFlag(CreatureFlag.INVINCIBLE);
	}

	/**
	 * V�rifie si l'entit� est agressive (va essayer de frapper un joueur si elle en voit un)
	 * @deprecated Utiliser {@link #hasCreatureFlag(CreatureFlag)} � la place
	 */
	default boolean isAgressive(){
		return hasCreatureFlag(CreatureFlag.AGRESSIVE);
	}

	/**
	 * D�fini si l'entit� est agressive (va essayer de frapper un joueur si elle en voit un).
	 * @param agressive Si l'entit� est agressive.
	 * @deprecated Utiliser {@link #addCreatureFlag(CreatureFlag)} et {@link #removeCreatureFlag(CreatureFlag)} � la place
	 */
	default void setAgressive(boolean agressive){
		if(agressive)
			addCreatureFlag(CreatureFlag.AGRESSIVE);
		else removeCreatureFlag(CreatureFlag.AGRESSIVE);
	}

	/**
	 * V�rifie si l'entit� peut se faire monter (clique droit sur l'entit�) par n'importe quel joueur.
	 * Pour le cochon, il ne n�cessite plus de selle et de canne.
	 * @deprecated Utiliser {@link #hasCreatureFlag(CreatureFlag)} � la place
	 */
	default boolean isRideable(){
		return hasCreatureFlag(CreatureFlag.RIDEABLE);
	}

	/**
	 * D�fini si l'entit� peut se faire monter (clique droit sur l'entit�) par n'importe quel joueur.
	 * @param rideable Si l'entit� peut se faire monter
	 * @deprecated Utiliser {@link #addCreatureFlag(CreatureFlag)} et {@link #removeCreatureFlag(CreatureFlag)} � la place
	 */
	default void setRideable(boolean rideable){
		if(rideable)
			addCreatureFlag(CreatureFlag.RIDEABLE);
		else removeCreatureFlag(CreatureFlag.RIDEABLE);
	}

	/**
	 * Change la vitesse de l'entit�
	 * @param speed La nouvelle vitesse
	 * @deprecated Utiliser {@link #setCreatureGenericAttribute(CreatureGenericAttribute, double)}
	 */
	default void setSpeed(double speed){
		setCreatureGenericAttribute(CreatureGenericAttribute.SPEED, speed);
	}

	/**
	 * R�cup�re la vitesse de l'entit�
	 * @deprecated Utiliser {@link #getCreatureGenericAttribute(CreatureGenericAttribute)}
	 */
	default double getSpeed(){
		return getCreatureGenericAttribute(CreatureGenericAttribute.SPEED);
	}

	/**
	 * Change les d�gats fait par l'entit�
	 * @param damage Les nouveaux d�gats
	 * @deprecated Utiliser {@link #setCreatureGenericAttribute(CreatureGenericAttribute, double)}
	 */
	default void setDamage(double damage){
		setCreatureGenericAttribute(CreatureGenericAttribute.DAMAGE, damage);
	}

	/**
	 * R�cup�re les d�gats fait par l'entit�
	 * @deprecated Utiliser {@link #getCreatureGenericAttribute(CreatureGenericAttribute)}
	 */
	default double getDamage(){
		return getCreatureGenericAttribute(CreatureGenericAttribute.DAMAGE);
	}

	/**
	 * V�rifie si l'entit� peut se d�placer. Si non, elle n'est plus soumise � la gravit�, n'a pas de knockback en combat, ...
	 * @deprecated Utiliser {@link #getCreatureBehaviour(CreatureBehaviour)}
	 */
	default boolean isMovable() {
		return getCreatureBehaviour() == CreatureBehaviour.MOTIONLESS;
	}

	/**
	 * Autorise ou non l'entit� � se d�placer
	 * @param movable Si l'entit� peut se d�placer
	 * @deprecated Utiliser {@link #setCreatureBehaviour(CreatureBehaviour)}
	 */
	default void setMovable(boolean movable) {
		setCreatureBehaviour(movable ? CreatureBehaviour.NORMAL : CreatureBehaviour.MOTIONLESS);
	}

	/**
	 * V�rifie si l'entit� peut voler. Une entit� non mont�e qui vole ne va pas attaquer les joueurs et va se contenter de se d�placer rapidement (� la mani�re d'une chauve-souris).
	 * @deprecated Utiliser {@link #getCreatureBehaviour(CreatureBehaviour)}
	 */
	default boolean isAllowedToFly(){
		return getCreatureBehaviour() == CreatureBehaviour.FLYING;
	}

	/**
	 * D�fini si l'entit� peut voler. Une entit� non mont�e qui vole ne va pas attaquer les joueurs et va se contenter de se d�placer rapidement (� la mani�re d'une chauve-souris).
	 * @param allowed Si l'entit� peut voler.
	 * @deprecated Utiliser {@link #setCreatureBehaviour(CreatureBehaviour)}
	 */
	default void setAllowedToFly(boolean allowed) {
		setCreatureBehaviour(allowed ? CreatureBehaviour.NORMAL : CreatureBehaviour.FLYING);
	}

	public void setCreatureBehaviour(CreatureBehaviour behaviour);

	public CreatureBehaviour getCreatureBehaviour();

	default void addCreatureFlag(CreatureFlag flag){
		if(!getFlags().contains(flag)){
			getFlags().add(flag);
			regenerateAttributes();
		}
	}

	default void removeCreatureFlag(CreatureFlag flag){
		getFlags().remove(flag);
		regenerateAttributes();
	}

	default boolean hasCreatureFlag(CreatureFlag flag){
		return getFlags().contains(flag);
	}

	public List<CreatureFlag> getFlags();

	public void setCreatureGenericAttribute(CreatureGenericAttribute attribute, double value);
	
	public double getCreatureGenericAttribute(CreatureGenericAttribute attribute);
	
	public void regenerateAttributes();

	/**
	 * R�cup�re l'entit� Bukkit
	 * @return L'entit� Bukkit
	 */
	public Entity  getBukkit();

	public enum CreatureFlag {
		RIDEABLE,
		AGRESSIVE,
		INVINCIBLE,
		FIREPROOF,
		INVISIBLE;
	}

	public enum CreatureBehaviour {
		MOTIONLESS,
		FLYING,
		NORMAL;
	}
	
	public enum CreatureGenericAttribute {
		SPEED,
		DAMAGE;
	}
}
