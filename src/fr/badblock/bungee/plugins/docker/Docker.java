package fr.badblock.bungee.plugins.docker;

import java.util.List;

import lombok.Data;

@Data public class Docker {
	
	private DockerConfiguration  configuration;
	private List<ServerEntity> 	serverEntities;
	
	
	public Docker() {
		
	}

}
