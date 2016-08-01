package fr.badblock.ladder.api.chat;

/**
 * Repr�sente un message ayant un temps
 * @author LeLanN
 */
public interface TimedMessage {
	/**
	 * R�cup�re le fade in (temps de passage de transparent � opaque)
	 * @return Le fade in
	 */
	public int getFadeIn();
	
	/**
	 * R�cup�re le fade out (temps de passage de opaque � transparent)
	 * @return Le fade out
	 */
	public int getFadeOut();
	
	/**
	 * R�cup�re le delay (temps d'affichage du title)
	 * @return Le delay
	 */
	public int getStay();
	
	/**
	 * Change le fade in [voir getFadeIn()]
	 * @param fadeIn Le fade in
	 */
	public void setFadeIn(final int fadeIn);
	
	/**
	 * Change le fade out [voir getFadeOut()]
	 * @param fadeOut Le fade out
	 */
	public void setFadeOut(final int fadeOut);
	
	/**
	 * Change le delay [voir getDelay()]
	 * @param delay Le delay
	 */
	public void setStay(final int delay);
}
