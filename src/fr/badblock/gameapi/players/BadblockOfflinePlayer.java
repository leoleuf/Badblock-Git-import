package fr.badblock.gameapi.players;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameServer.WhileRunningConnectionTypes;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.utils.CustomObjective;

/**
 * Donne les anciennes donn�es d'un joueur ayant d�connect� apr�s le d�but de la partie.<br>
 * Si le mini-jeu l'autorise, ces informations seront r�utilis�es (voir {@link GameServer#whileRunningConnection(WhileRunningConnectionTypes)})
 * @author LeLanN
 */
public interface BadblockOfflinePlayer {
	/**
	 * R�cup�re l'UUID du joueur
	 * @return L'UUID
	 */
	public UUID getUniqueId();
	
	/**
	 * R�cup�re le pseudo du joueur
	 * @return Le pseudo
	 */
	public String getName();
	
	/**
	 * La derni�re position du joueur
	 * @return La derni�re position
	 */
	public Location getLastLocation();
	
	/**
	 * R�cup�re la fausse dimension dans laquelle le joueur �tait (afin de lui recharger au retour :o)
	 * @return La fausse dimension
	 */
	public World.Environment getFalseDimension();
	
	/**
	 * R�cup�re le CustomObjective vu par le joueur avant la d�connection
	 * @return Le CustomObjective (null si non d�finit)
	 */
	public CustomObjective getCustomObjective();
	
	/**
	 * R�cup�re les donn�es du joueur. Elles ne seront pas redemand�es � Ladder (elles n'ont th�oriquement pas chang�es)
	 * @return Les donn�es
	 */
	public PlayerData getPlayerData();
	
	/**
	 * R�cup�re les donn�es ingame du joueur, avant sa d�connection. Attention, la classe fournie doit avoir un constructeur sans arguments.
	 * 
	 * @param clazz La classe impl�mentant InGameData
	 * @return Les donn�es joueurs (ou null si la classe donn�e n'est pas correcte)
	 */
	public <T extends InGameData> T inGameData(Class<T> clazz);
	
	/**
	 * R�cup�re la team du joueur avant sa d�connection
	 * @return La team
	 */
	public BadblockTeam getTeam();
}
