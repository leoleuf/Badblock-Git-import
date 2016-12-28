package fr.badblock.commons.data;

import java.io.Serializable;

import com.mongodb.BasicDBObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false) public class PunishData extends BasicDBObject implements Serializable {
	
	private static final long serialVersionUID = -3763404142845364760L;

	private boolean ban,
					mute;
	
	private long	banEnd,
					muteEnd;
	
	private String	banReason,
					muteReason;
	
	private String	banner,
					muter;
}
