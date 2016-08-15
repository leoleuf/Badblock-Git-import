package fr.badblock.api.utils.maths;

import org.bukkit.Location;

public class Selection{
	private Location firstBound;
	private Location secondBound;

	public Selection(Location firstBound, Location secondBound){
		this.firstBound = firstBound;
		this.secondBound = secondBound;
	}
	/* Permet d'obtenir la distance entre une position l1 et l2 */
	private double distance(double l1, double l2){
		return Math.abs(l1 - l2);
	}

	private boolean isBetween(Location location){
		final Location l1 = firstBound; /* Je fais ça pour que les noms soient plus petits c'est plus pratique là de suite */
		final Location l2 = secondBound;
		final double distanceX = distance(l1.getX(), l2.getX());
		final double distanceY = distance(l1.getY(), l2.getY());
		final double distanceZ = distance(l1.getZ(), l2.getZ());

		/* Si la distance entre le X de la Loc l1 et le X de la loc location est plus grande que la distance entre le X de l1 et l2
		     ou
			 Si la distance entre le X de la Loc l2 et le X de la loc location est plus grande que la distance entre le X de l1 et l2
		 */
		if(distance(l1.getX(), location.getX()) > distanceX || distance(l2.getX(), location.getX()) > distanceX){
			return false; /* On en déduit que le point X n'est pas dans la zone : on retourne faux */
		}
		/* De même pour les Y */
		if(distance(l1.getY(), location.getY()) > distanceY || distance(l2.getY(), location.getY()) > distanceY){
			return false; 
		}
		/* De même pour les Z */
		if(distance(l1.getZ(), location.getZ()) > distanceZ || distance(l2.getZ(), location.getZ()) > distanceZ){
			return false; 
		}
		return true; /* La position semble être entre l1 et l2, on retourne vrai */
	}
	public boolean isBetweenLarge(Location location){
		final Location l1 = firstBound; /* Je fais ça pour que les noms soient plus petits c'est plus pratique là de suite */
		final Location l2 = secondBound;
		final double distanceX = distance(l1.getX(), l2.getX()) + 3;
		final double distanceY = distance(l1.getY(), l2.getY()) + 3;
		final double distanceZ = distance(l1.getZ(), l2.getZ()) + 3;

		/* Si la distance entre le X de la Loc l1 et le X de la loc location est plus grande que la distance entre le X de l1 et l2
		     ou
			 Si la distance entre le X de la Loc l2 et le X de la loc location est plus grande que la distance entre le X de l1 et l2
		 */
		if(distance(l1.getX(), location.getX()) > distanceX || distance(l2.getX(), location.getX()) > distanceX){
			return false; /* On en déduit que le point X n'est pas dans la zone : on retourne faux */
		}
		/* De même pour les Y */
		if(distance(l1.getY(), location.getY()) > distanceY || distance(l2.getY(), location.getY()) > distanceY){
			return false; 
		}
		/* De même pour les Z */
		if(distance(l1.getZ(), location.getZ()) > distanceZ || distance(l2.getZ(), location.getZ()) > distanceZ){
			return false; 
		}
		return true; /* La position semble être entre l1 et l2, on retourne vrai */
	}
	public boolean isInSelection(Location location){
		return isBetween(location);
	}
}