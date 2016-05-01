package fr.badblock.gameapi.players.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Repr�sente l'avancement d'un joueur dans un achievement (si il l'a d�j� r�ussi (et quand), sa progression).<br>
 * Peut �tre obtenu via {@link fr.badblock.gameapi.players.data.PlayerData#getAchievementState(fr.badblock.gameapi.players.PlayerAchievement)}
 * @author LeLanN
 */
@Data@AllArgsConstructor
public class PlayerAchievementState {
	private boolean succeeds;
	private String	succeedsDate;
	private double  progress;

	public PlayerAchievementState(){
		this(false, "", 0);
	}
	
	/**
	 * Change l'achievement en 'r�ussi'. Sauvegarde la date de r�ussite et r�initialisera l'avanc�e.
	 */
	public void succeed(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		this.succeeds 	  = true;
		this.succeedsDate = dateFormat.format(new Date());
		this.progress	  = 0d;
	}
	
	/**
	 * Change la progression du joueur dans son achievement
	 * @param progression Les 'points' de progression a ajouter.
	 */
	public void progress(double progression){
		this.progress += progression;
	}
}
