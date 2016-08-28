package fr.badblock.gameapi.utils.i18n.messages;

import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Liste de messages utilis�s par l'API ou � d�stination des plugins annexes
 * pour unifier le syst�me de traduction (pas de doublon) et simplifier le
 * d�veloppement.<br>
 * Messages commandes.
 * 
 * @author LeLanN
 */
public class CommandMessages {
	/**
	 * Lorsqu'un joueur n'a pas la permission d'utilis� une commande.
	 * 
	 * @return Le message
	 */
	public static TranslatableString noPermission() {
		return new TranslatableString("commands.nopermission");
	}
}
