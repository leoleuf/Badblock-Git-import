package fr.badblock.gameapi.players;

import org.bukkit.World;

import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameServer.WhileRunningConnectionTypes;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;

/**
 * Donne les anciennes donn�es d'un joueur ayant d�connect� apr�s le d�but de la
 * partie.<br>
 * Si le mini-jeu l'autorise, ces informations seront r�utilis�es (voir
 * {@link GameServer#whileRunningConnection(WhileRunningConnectionTypes)})
 * 
 * @author LeLanN
 */
public interface BadblockOfflinePlayer extends BadblockPlayerData {
	/**
	 * R�cup�re le CustomObjective vu par le joueur avant la d�connection
	 * 
	 * @return Le CustomObjective (null si non d�finit)
	 */
	public CustomObjective getCustomObjective();

	/**
	 * R�cup�re la fausse dimension dans laquelle le joueur �tait (afin de lui
	 * recharger au retour :o)
	 * 
	 * @return La fausse dimension
	 */
	public World.Environment getFalseDimension();
}
