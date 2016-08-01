package fr.badblock.ladder.api.chat;

/**
 * Repr�sente un Title
 * @author LeLanN
 */
public interface Title extends ChatMessage, TimedMessage {
	/**
	 * R�cup�re le message principal
	 * @return Le message
	 */
	public String getTitle();

	/**
	 * R�cup�re le message du bas
	 * @return Le message
	 */
	public String getSubTitle();
	
	/**
	 * D�finit le message principal
	 * @param title Le message
	 */
	public void setTitle(final String title);
	
	/**
	 * D�finit le sous-message
	 * @param subTitle Le message
	 */
	public void setSubTitle(final String subTitle);
}
