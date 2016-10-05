package fr.badblock.gameapi.players.scoreboard;

import org.bukkit.scoreboard.Scoreboard;

import com.google.gson.JsonArray;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Repr�sente le ScoreBoard commun � tous les joueurs, avec des m�thodes
 * pr�cr�es.
 * 
 * @author LeLanN
 */
public interface BadblockScoreboard {
	/**
	 * Repr�sente un �l�ment du vote
	 * 
	 * @author LeLanN
	 */
	@Data
	@AllArgsConstructor
	public static class VoteElement {
		private String internalName;
		private String displayName;
	}

	/**
	 * Commence un vote � partir d'une liste de valeur
	 * 
	 * @param maps
	 *            Un array JSON avec toutes les maps sous forme de VoteElement.
	 */
	public void beginVote(JsonArray maps);

	/**
	 * Si la m�thode est appel�e, l'API va register un objectif (scoreboard)
	 * pour g�rer l'affichage de la vie sous les pseudos joueurs.
	 */
	public void doBelowNameHealth();

	/**
	 * Si la m�thode est appel�e, l'API va register des teams (scoreboard) pour
	 * g�rer l'affichage des groups des joueurs dans la tablist et au dessus des
	 * noms du joueur<br>
	 */
	public void doGroupsPrefix();
	
	/**
	 * Si la m�thode est appel�e l'API va afficher un holograme au dessus des joueurs prenant des d�gats
	 */
	public void doOnDamageHologram();

	/**
	 * Si la m�thode est appel�e, l'API va register un objectif (scoreboard)
	 * pour g�rer l'affichage de la vie dans la tablist.
	 */
	public void doTabListHealth();

	/**
	 * Si la m�thode est appel�e, l'API va register des teams (scoreboard) pour
	 * g�rer l'affichage des teams des joueurs dans la tablist et au dessus des
	 * noms du joueur<br>
	 * A appel� apr�s avoir register les teams via
	 * {@link GameAPI#registerTeams(int, org.bukkit.configuration.ConfigurationSection)}
	 * !
	 */
	public void doTeamsPrefix();

	/**
	 * Le vote s'arr�te (les joueurs ne peuvent plus voter).
	 */
	public void endVote();

	/**
	 * R�cup�re le Scoreboard Bukkit
	 * 
	 * @return Le scoreboard
	 */
	public Scoreboard getHandler();
	
	/**
	 * Savoir si il y a ou non les prefix des groupes en affichage dans le scoreboard
	 * @return
	 */
	public boolean hasShownGroupPrefix();
	
	/**
	 * R�cup�re le nom d'affichage utilis� pour le joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @return Le nom, ou null si aucun
	 */
	public TranslatableString getUsedName(BadblockPlayer player);

	/**
	 * R�cup�re le nombre de votes pour le gagnant
	 * 
	 * @return Le nombre de votes
	 */
	public int getVotesForWinner();

	/**
	 * R�cup�re la map gagnante
	 * 
	 * @return La map gagnante
	 */
	public VoteElement getWinner();

	/**
	 * Ouvre l'inventaire de vote � un joueur
	 * 
	 * @param player
	 *            Le joueur
	 * @return L'inventaire custom
	 */
	public void openVoteInventory(BadblockPlayer locale);
}
