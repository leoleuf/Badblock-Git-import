package fr.badblock.gameapi.particles;

import org.bukkit.util.Vector;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Repr�sente un effet de particule.<br>
 * R�cup�rable avec {@link GameAPI#createParticleEffect(ParticleEffectType)}<br>
 * Envoyable avec
 * {@link BadblockPlayer#sendParticle(org.bukkit.Location, ParticleEffect)}
 * 
 * @author LeLanN
 */
public interface ParticleEffect {
	/**
	 * R�cup�re le nombre de particules � afficher
	 * 
	 * @return Le nombre de particules
	 */
	public int getAmount();

	/**
	 * R�cup�re les datas de l'entit�. Voir {@link ParticleData} pour savoir
	 * quand les appliquer.
	 * 
	 * @return Les datas
	 */
	public ParticleData getData();

	/**
	 * R�cup�re la v�locit� de la particule
	 * 
	 * @return La v�locit�
	 */
	public Vector getOffset();

	/**
	 * R�cup�re la vitesse de la particule. Voir {@link ParticleEffectType} pour
	 * voir l'effet sur chaque particule.
	 * 
	 * @return La vitesse
	 */
	public float getSpeed();

	/**
	 * R�cup�re la particule � afficher.
	 * 
	 * @return La particule
	 */
	public ParticleEffectType getType();

	/**
	 * V�rifie si la particule s'affiche de loin
	 * 
	 * @return Si elle s'affiche de loin
	 */
	public boolean isLongDistance();

	/**
	 * D�finit le nombre de particules � afficher
	 * 
	 * @param amount
	 *            Le nombre de particules
	 * @return L'effet
	 */
	public ParticleEffect setAmount(int amount);

	/**
	 * D�finit les datas de l'entit�. Voir {@link ParticleData} pour savoir
	 * quand les appliquer.
	 * 
	 * @param data
	 *            Les datas
	 * @return L'effet
	 */
	public ParticleEffect setData(ParticleData data);

	/**
	 * D�finit si la particule s'affiche de loin
	 * 
	 * @param longDistance
	 *            Si elle s'affiche de loin
	 * @return L'effet
	 */
	public ParticleEffect setLongDistance(boolean longDistance);

	/**
	 * D�finit la v�locit� de la particule
	 * 
	 * @param offset
	 *            La v�locit�
	 * @return L'effet
	 */
	public ParticleEffect setOffset(Vector offset);

	/**
	 * D�finit la vitesse de la particule. Voir {@link ParticleEffectType} pour
	 * voir l'effet sur chaque particule.
	 * 
	 * @param speed
	 *            La vitesse
	 * @return L'effet
	 */
	public ParticleEffect setSpeed(float speed);

	/**
	 * D�finit la particule � afficher
	 * 
	 * @param type
	 *            La particule
	 * @return L'effet
	 */
	public ParticleEffect setType(ParticleEffectType type);
}
