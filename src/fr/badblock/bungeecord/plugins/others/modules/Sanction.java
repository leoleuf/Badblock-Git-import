package fr.badblock.bungeecord.plugins.others.modules;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sanction {

	public String pseudo;
	public String type;
	public long   expire;
	public long   timestamp;
	public String reason;
	public String banner;
	public String fromIp;
	public String proof;
	public boolean auto;
	
}
