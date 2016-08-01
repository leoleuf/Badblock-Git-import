package fr.badblock.ladder.api.chat;

import com.google.gson.JsonObject;

/**
 * Repr�sente un message sous forme JSON (possibilit� d'ajout� des events)
 * @author LeLanN
 */
public interface RawMessage extends ChatMessage {
	/**
	 * Ajoute ou change le click event du message
	 * @param type Le type de l'event
	 * @param parse Un boolean indiquant si il faut serializer le message
	 * @param value La ou les valeurs � ajouter
	 */
	public RawMessage setClickEvent(ClickEventType type, boolean parse, String... value);

	/**
	 * Ajoute ou change le hover event du message
	 * @param type Le type de l'event
	 * @param parse Un boolean indiquant si il faut serializer le message
	 * @param value La ou les valeurs � ajouter
	 */
	public RawMessage setHoverEvent(HoverEventType type, boolean parse, String... value);
	
	/**
	 * Change la couleur de la premi�re partie du texte
	 * @param color La nouvelle couleur
	 */
	public RawMessage setMainColor(ChatColor color);
	
	/**
	 * Ajoute un message � la fin de celui-ci
	 * @param message Le message
	 */
	public RawMessage add(RawMessage message);
	
	/**
	 * Ajoute des messages � la fin de celui-ci
	 * @param all Les messages
	 */
	public RawMessage addAll(RawMessage... all);
	
	/**
	 * Renvoit le RawMessage sous forme de JsonObject. Principalement utile de mani�re interne.
	 * @return Le RawMessage en JSON
	 */
	public JsonObject asJsonObject();
	
	/**
	 * Repr�sente les types d'event de type hover (lorsque l'on passe sur le message)
	 * @author LeLanN
	 */
	public enum HoverEventType{
		/**
		 * Affiche un texte
		 */
		SHOW_TEXT,
		/**
		 * Affiche un achievement
		 */
		SHOW_ACHIEVEMENT,
		/**
		 * Affiche les carat�ristiques d'un item
		 */
		SHOW_ITEM;
	}
	
	/**
	 * Repr�sente les types d'event de type click (lorsque l'on clique sur le message)
	 * @author LeLanN
	 */
	public enum ClickEventType{
		/**
		 * Ouvre une URL
		 */
		OPEN_URL,
		/**
		 * Ouvre un fichier
		 */
		OPEN_FILE,
		/**
		 * Execute une commande
		 */
		RUN_COMMAND,
		/**
		 * Propose une commande au joueur
		 */
		SUGGEST_COMMAND;
	}
}
