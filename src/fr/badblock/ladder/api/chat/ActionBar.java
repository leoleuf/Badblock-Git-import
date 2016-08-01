package fr.badblock.ladder.api.chat;

/**
 * R�pr�sente une Action Bar
 * @author LeLanN
 */
public interface ActionBar extends ChatMessage, TimedMessage {
	/**
	 * R�cup�re le message
	 * @return Le message
	 */
	public String getMessage();
	
	/**
	 * D�finit le message
	 * @param message Le message
	 */
	public void setMessage(final String message);
}
