package fr.badblock.gameapi.players.scoreboard;

import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Repr�sente un objectif vu par un seul joueur, qui peut �tre mis � jour de
 * mani�re tr�s rapide et sans clignements.<br>
 * Obtenir avec {@link fr.badblock.gameapi.GameAPI#buildCustomObjective(String)}
 * 
 * @author LeLanN
 */
public interface CustomObjective {
	/**
	 * Permet de changer une ligne du scoreboard.<br>
	 * Attention, la ligne 1 correspond � la ligne du bas du scoreboard et le
	 * scoreboard ne peut en afficher que 15.<br>
	 * 
	 * @param line
	 *            La ligne du texte (entre 1 et 15)
	 * @param text
	 *            Le texte � afficher, maximum 32 caract�res (code couleur = 2
	 *            caract�res)
	 */
	public void changeLine(int line, String text);

	/**
	 * Actuallise le scoreboard
	 */
	public void generate();

	/**
	 * R�cup�re le joueur regardant cet objective
	 * 
	 * @return Le BadblockPlayer (si il n'a pas encore �t� assign� ou que le
	 *         joueur n'est plus l�, null)
	 */
	public BadblockPlayer getAssignedPlayer();

	/**
	 * Supprime une ligne du scoreboard.
	 * 
	 * @param line
	 *            La ligne � supprimer (entre 1 et 15)
	 */
	public void removeLine(int line);

	/**
	 * R�initialise l'Objective.
	 */
	public void reset();

	/**
	 * Change le nom de l'objective
	 * 
	 * @param displayName
	 *            Le nom (maximum 32 caract�res).
	 */
	public void setDisplayName(String displayName);

	/**
	 * D�finit le g�n�rateur
	 * 
	 * @param generator
	 *            Le g�n�rateur
	 */
	public void setGenerator(BadblockScoreboardGenerator generator);

	/**
	 * Affiche l'objectif � un joueur (un seul par CustomObjective !).
	 * 
	 * @param player
	 *            Le joueur
	 */
	public void showObjective(BadblockPlayer player);
}
