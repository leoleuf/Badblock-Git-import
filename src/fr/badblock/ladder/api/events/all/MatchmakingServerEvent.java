package fr.badblock.ladder.api.events.all;

import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Appelé quand un serveur dit s'il est disponible ou non
 * @author LeLanN
 */
@AllArgsConstructor
public class MatchmakingServerEvent extends Event {
	@Getter private final Bukkit  	   bukkit;
	@Getter private final ServerStatus status;
	@Getter private final int 		   players,
									   slots;

	@Getter public enum ServerStatus {
		WAITING(1),
		RUNNING(2),
		FINISHED(3),
		STOPING(4);

		private final int id;

		private ServerStatus(int id){
			this.id = id;
		}

		public static ServerStatus getStatus(int id){
			for(final ServerStatus status : values()){
				if(status.getId() == id)
					return status;
			}

			System.out.println(id);
			return ServerStatus.RUNNING;
		}
	}
}