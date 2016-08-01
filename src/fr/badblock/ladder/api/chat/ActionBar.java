package fr.badblock.ladder.api.chat;

/**
 * Réprésente une Action Bar
 * @author LeLanN
 */
public interface ActionBar extends ChatMessage, TimedMessage {
	/**
	 * Récupère le message
	 * @return Le message
	 */
	public String getMessage();
	
	/**
	 * Définit le message
	 * @param message Le message
	 */
	public void setMessage(final String message);
}
