package fr.badblock.gameapi.utils.i18n;

/**
 * Repr�sente la version traduite d'un message, tel que trouvable dans la
 * configuration. La classe n'est uniquement que la repr�sentation brute de la
 * configuration, et ne permet pas de formatter le message. Passer par
 * {@link fr.badblock.gameapi.utils.i18n.Language} pour cela.
 * 
 * @author LelanN
 */
public interface Message {
	/**
	 * Renvoit tous les versions possibles du messages. Si le Message n'est pas
	 * al�toire, un seul sera renvoy�.
	 * 
	 * @return La totalit� des messages.
	 */
	public String[][] getAllMessages();

	/**
	 * Renvoit le message 'brute', sans remplacement des param�tres ou des
	 * couleurs. N�anmoins, si le message est al�atoire, en renvoit un au
	 * hasard.
	 * 
	 * @return Le message non formatt�.
	 */
	public String[] getUnformattedMessage();

	/**
	 * V�rifie si le message est al�atoire, autrement dit si plusieurs messages
	 * ont �t� configur�s et que un sera choisit au hasard.
	 */
	public boolean isRandomMessage();

	/**
	 * V�rifie si le message utilise le footer (voir
	 * {@link fr.badblock.gameapi.utils.i18n.Language})
	 */
	public boolean useFooter();

	/**
	 * V�rifie si le message utilise le header "long" (voir
	 * {@link fr.badblock.gameapi.utils.i18n.Language})
	 */
	public boolean useHeader();

	/**
	 * V�rifie si le message utilise le header "court" (voir
	 * {@link fr.badblock.gameapi.utils.i18n.Language})
	 */
	public boolean useShortHeader();
}
