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

	public void setCreatureBehaviour(CreatureBehaviour behaviour);

	public CreatureBehaviour getCreatureBehaviour();

	default void addCreatureFlag(CreatureFlag flag){
		if(!getFlags().contains(flag)){
			getFlags().add(flag);
			regenerateAttributes();
		}
	}
	
	default void addCreatureFlags(CreatureFlag... flags){
		for(CreatureFlag flag : flags){
			addCreatureFlag(flag);
		}
	}

	default void removeCreatureFlag(CreatureFlag flag){
		getFlags().remove(flag);
		regenerateAttributes();
	}
	
	default void removeCreatureFlags(CreatureFlag... flags){
		for(CreatureFlag flag : flags){
			getFlags().remove(flag);
		}
		
		regenerateAttributes();
	}

	default boolean hasCreatureFlag(CreatureFlag flag){
		return getFlags().contains(flag);
	}
	
	default boolean hasCreatureFlags(CreatureFlag... flags){
		for(CreatureFlag flag : flags){
			if(!hasCreatureFlag(flag))
				return false;
		}
		
		return true;
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
