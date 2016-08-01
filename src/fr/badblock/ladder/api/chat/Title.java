package fr.badblock.ladder.api.chat;

/**
 * Représente un Title
 * @author LeLanN
 */
public interface Title extends ChatMessage, TimedMessage {
	/**
	 * Récupère le message principal
	 * @return Le message
	 */
	public String getTitle();

	/**
	 * Récupère le message du bas
	 * @return Le message
	 */
	public String getSubTitle();
	
	/**
	 * Définit le message principal
	 * @param title Le message
	 */
	public void setTitle(final String title);
	
	/**
	 * Définit le sous-message
	 * @param subTitle Le message
	 */
	public void setSubTitle(final String subTitle);
}
