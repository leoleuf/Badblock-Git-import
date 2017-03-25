package fr.badblock.automessage;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor public class InsistentMessage {
	
	public long				   everyXSeconds;
	public List<String[]> 	   message;
	public String			   permission;
	
}
