package fr.badblock.bungee.plugins.docker.objects;

import java.util.ArrayList;
import java.util.List;

public class ServerEntity extends DockerEntity {

	private String 					name;
	private int	   					portRange;
	private List<SubServerEntity>	subServers;
	private String					command;
	private String					maintenance;
	
	public ServerEntity(String name, int portRange, String command, String maintenance) {
		super(DockerEntityType.SERVER);
		this.name = name;
		this.portRange = portRange;
		this.subServers = new ArrayList<>();
		this.command = command;
		this.maintenance = maintenance;
	}

}
