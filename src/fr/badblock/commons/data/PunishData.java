package fr.badblock.commons.data;

import lombok.Data;

@Data public class PunishData {
	private boolean ban,
					mute;
	
	private long	banEnd,
					muteEnd;
	
	private String	banReason,
					muteReason;
	
	private String	banner,
					muter;
}
