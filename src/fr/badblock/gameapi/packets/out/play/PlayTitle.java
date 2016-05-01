package fr.badblock.gameapi.packets.out.play;

import fr.badblock.gameapi.packets.BadblockOutPacket;

public interface PlayTitle extends BadblockOutPacket {
	public String getContent();
	
	public PlayTitle setContent(String content);
	
	public Action getAction();
	
	public PlayTitle setAction(Action action);
	
	public long getFadeIn();
	
	public long getStay();
	
	public long getFadeOut();
	
	public PlayTitle setFadeIn(long time);
	
	public PlayTitle setStay(long time);
	
	public PlayTitle setFadeOut(long time);
	
	public enum Action {
		/**
		 * Change le message du haut
		 */
		TITLE(),
		/**
		 * Change le message du bas
		 */
		SUBTITLE(),
		/**
		 * Change les timings (temps d'affichages, affichera le title � l'�cran)
		 */
		TIMES(),
		/**
		 * Fait dispara�tre le title de l'�cran (utiliser TIMES pour le remettre)
		 */
		CLEAR(),
		/**
		 * R�initialise tout le packet
		 */
		RESET();
	}
}
