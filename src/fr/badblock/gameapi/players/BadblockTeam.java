package fr.badblock.gameapi.players;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.players.data.TeamData;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;

/**
 * Repr�sente une Team utilis�e pour un MiniJeu.<br>
 * Pour les utiliser les registers avec {@link fr.badblock.gameapi.GameAPI#registerTeams(int, Class, org.bukkit.configuration.ConfigurationSection)}
 * @author LeLanN
 */
public interface BadblockTeam {
	/**
	 * R�cup�re le nom interne de la team
	 * @return Le nom interne.
	 */
	public String getKey();
	
	/**
	 * Retourne la string (i18n) du pr�fixe dans la tabulation de la Team
	 * @param color La couleur (verte ou rouge) pour savoir si la team est alli�e ou non
	 * @return La string
	 */
	public TranslatableString getTabPrefix(ChatColor color);
	
	/**
	 * R�cup�re la string (i18n) du nom dans le chat (pour le mettrre dans une phrase) de la team
	 * @return La string
	 */
	public TranslatableString getChatName();
	
	/**
	 * R�cup�re la string (i18n) du pr�fixe dans le chat de la team (quand les joueurs parlent)
	 * @return La string
	 */
	public TranslatableString getChatPrefix();
	
	/**
	 * R�cup�re la couleur (chat) de l'�quipe.
	 * @return La couleur (chat).
	 */
	public ChatColor getColor();
	
	/**
	 * R�cup�re la couleur (laine) de l'�quipe.
	 * @return La couleur (laine).
	 */
	public DyeColor getDyeColor();
	
	/**
	 * R�cup�re la couleur (autre) de l'�quipe.
	 * @return La couleur.
	 */
	public Color geNormalColor();
	
	/**
	 * R�cup�re le nombre maximum de joueurs dans la team.
	 * @return Le nombre maximum.
	 */
	public int getMaxPlayers();
	
	/**
	 * D�finit le nombre maximum de joueurs dans la team.
	 * @param maxPlayers Le nombre maximum.
	 */
	public void setMaxPlayers(int maxPlayers);
	
	/**
	 * R�cup�re le nombre de joueurs dans la team connect�s sur le serveur.
	 * @return Le nombre de joueurs.
	 */
	public int playersCurrentlyOnline();
	
	/**
	 * R�cup�re les joueurs de la team �tant connect�s
	 * @return Une collection
	 */
	public Collection<BadblockPlayer> getOnlinePlayers();
	
	/**
	 * R�cup�re les noms des joueurs de la team �tant connect�s
	 * @return Une collection
	 */
	public Collection<String> getOnlinePlayersName();
	
	/**
	 * R�cup�re les joueurs pr�sent lors du d�marrage
	 * @return Les joueurs
	 */
	public Collection<UUID> getPlayersAtStart();
	
	/**
	 * R�cup�re les joueurs pr�sent lors du d�marrage
	 * @return Les joueurs
	 */
	public Collection<String> getPlayersNameAtStart();
	
	/**
	 * Permet � un BadblockPlayer de rejoindre la team.
	 * @param player Le player
	 * @param reason La raison du join
	 * @return Si l'op�ration est un succ�s
	 */
	public boolean joinTeam(BadblockPlayer player, JoinReason reason);
	
	/**
	 * Fait quitter la team � un BadblockPlayer.
	 * @param player Le player
	 */
	public void leaveTeam(BadblockPlayer player);
	
	/**
	 * R�cup�re les donn�es ingame de la team. Attention, la classe fournie doit �tre celle donn�e
	 * dans le onEnable gr��e � {@link fr.badblock.gameapi.GameAPI#registerTeams(int, Class, org.bukkit.configuration.ConfigurationSection)}
	 * 
	 * @param clazz La classe impl�mentant TeamData
	 * @return Les donn�es joueurs (ou null si la classe donn�e n'est pas correcte)
	 */
	public <T extends TeamData> T teamData(Class<T> clazz);
	
	/**
	 * Cr�� l'item de join pour la team
	 * @param locale La langue
	 * @return L'item
	 */
	public ItemStackExtra createJoinItem(Locale locale);
}

