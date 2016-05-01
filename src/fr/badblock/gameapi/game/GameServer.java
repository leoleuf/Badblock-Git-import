package fr.badblock.gameapi.game;

/**
 * Classe permettant de g�rer le statut des jeux
 * @author xMalware
 * @author LeLanN
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
}
