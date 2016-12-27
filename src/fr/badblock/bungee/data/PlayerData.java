package fr.badblock.bungee.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data public class PlayerData {

	public String 			name;
	public PlayerDataType	type;
	public String			value;
	
	public enum PlayerDataType {
		SEND_TO_BUKKIT,
		SEND_MESSAGE,
		DISCONNECT;
	}
	
}
