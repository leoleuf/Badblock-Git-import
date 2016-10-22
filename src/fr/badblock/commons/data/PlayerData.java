package fr.badblock.commons.data;

import java.util.UUID;

public class PlayerData {
	public String     name;
	public String     nickName;
	public String     loginPassword;
	
	public PunishData punish;
	
	public UUID       uniqueId;
	public long       lastLogin;
	
	public PlayerGameData game = new PlayerGameData();
}
