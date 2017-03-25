package fr.badblock.common.docker.factories;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class CommandFactory {

	private String playerName;
	private String command;
	
}
