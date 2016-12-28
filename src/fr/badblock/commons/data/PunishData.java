package fr.badblock.commons.data;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.mongodb.BasicDBObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data@EqualsAndHashCode(callSuper=false) public class PunishData extends BasicDBObject implements Serializable {
	
	private static final long serialVersionUID = -3763404142845364760L;

	@Expose private boolean ban,
					mute;
	
	@Expose private long	banEnd,
					muteEnd;
	
	@Expose private String	banReason,
					muteReason;
	
	@Expose private String	banner,
					muter;

	public PunishData(BasicDBObject p) {
		super(p);
	}
	
	public PunishData() {
	}
	
}
