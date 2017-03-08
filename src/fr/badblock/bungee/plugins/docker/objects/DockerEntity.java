package fr.badblock.bungee.plugins.docker.objects;

import lombok.Getter;

@Getter public class DockerEntity {

	private DockerEntityType type;
	
	public DockerEntity(DockerEntityType type) {
		this.type = type;
	}
	
}
