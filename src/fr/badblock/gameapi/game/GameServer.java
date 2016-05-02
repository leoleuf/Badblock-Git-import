package fr.badblock.gameapi.game;

import fr.badblock.gameapi.events.api.PlayerReconnectionPropositionEvent;

/**
 * Classe permettant de g�rer le statut des jeux
 * @author LeLanN
 * @author xMalware
 */
public interface GameServer {
	/**
	 * D�finir le statut de la partie
	 * @param gameState Le statut
	 */
	public void setGameState(GameState gameState);

	/**
	 * R�cup�rer le statut de la partie
	 * @return gameState Le statut
	 */
	public GameState getGameState();
	
	/**
	 * D�finit le nombre max de joueurs
	 * @param maxPlayers Le nombre max de joueurs
	 */
	public void setMaxPlayers(int maxPlayers);

	/**
	 * R�cup�re le nombre max de joueurs
	 * @return Le nombre max de joueurs
	 */
	public int getMaxPlayers();
	
	/**
	 * D�finit le traitement des joueurs si il se reconnecte apr�s le d�but de la partie
	 * @param type Le type de traitement
	 */
	public void whileRunningConnection(WhileRunningConnectionTypes type);
	
	/**
	 * Si la phase du jeu change (entr�e en Deathmatch par exemple), utiliser ceci pour cancel les propositions pour rejoindre le serveur<br>
	 * Autrement, utilis� automatiquement � la fin de la partie
	 */
	public void cancelReconnectionInvatations();
	
	/**
	 * Repr�sente les diff�rents types de reconnection
	 * @author LeLanN
	 */
	public static enum WhileRunningConnectionTypes {
		/**
		 * Met le joueur en spectateur
		 */
		SPECTATOR,
		/**
		 * R�cup�re les donn�es joueurs stock�es si le joueur avait d�co<br>
		 * Si cette politique n'est pas utilisable � chaque fois, vous pouvez cancel l'event {@link PlayerReconnectionPropositionEvent}
		 */
		BACKUP;
	}
}
