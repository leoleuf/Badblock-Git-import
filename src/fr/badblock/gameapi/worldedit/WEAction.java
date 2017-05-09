package fr.badblock.gameapi.worldedit;

import org.bukkit.command.CommandSender;

public interface WEAction {
	/**
	 * Renvoi le nombre total d'it�ration � faire
	 * Le r�sultat peut �tre une estimation et non un chiffre exacte
	 * @return Un entier
	 */
	public long getTotalIterationCount();
	
	/**
	 * Renvoi le nombre d'appel fait � l'action
	 * @return Un entier
	 */
	public long getIterationCount();

	/**
	 * Renvoi l'utilisateur ayant demand� 
	 * @return
	 */
	public CommandSender getApplicant();
	
	/**
	 * V�rifie si une ou plusieurs autre action doit �tre faite
	 * @return Un bool�en
	 */
	public boolean hasNext();
	
	/**
	 * Fait la prochaine action
	 */
	public void next();
	
	/**
	 * Appel� lorsque l'action commence � �tre trait�e
	 */
	public void notifyStart();
	
	/**
	 * Appl� lorsque l'action n'est plus trait�e (possiblment en cours de traitement)
	 */
	public void notifyEnd();
}
