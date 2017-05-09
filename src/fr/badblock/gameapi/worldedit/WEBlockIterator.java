package fr.badblock.gameapi.worldedit;

import org.bukkit.World;

public interface WEBlockIterator {
	/**
	 * R�cup�re le monde dans lequel l'it�rateur doit �tre trait�
	 * @return Un monde Bukkit
	 */
	public World getWorld();
	
	/**
	 * Renvoi le nombre total de block de l'it�rateur, possiblement une estimation
	 * @return Un entier
	 */
	public long getCount();
	
	/**
	 * Renvoi vrai si le prochain block est dans un chunk diff�rent
	 * @return Un bool�en
	 */
	public boolean hasNextChunk();
	
	/**
	 * R�cup�re le prochain chunk
	 * @return Un tableau de 2 entiers
	 */
	public int[] getNextChunk();
	
	/**
	 * V�rifie si il reste au moins un block � traiter
	 * @return Un bool�en
	 */
	public boolean hasNext();
	
	/**
	 * R�cup�re la prochaine position
	 * @return Un tableau de 3 entiers
	 */
	public int[] getNextPosition();
}
