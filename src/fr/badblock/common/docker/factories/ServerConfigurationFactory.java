package fr.badblock.common.docker.factories;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class ServerConfigurationFactory {
	
	private long 	  	 id;
	private String		 ip;
	private long		 port;
	private int			 slots;
	private String		 bungeeName;
	private String		 screenName;
	private String 		 world;
	private String 		 system;
	private long	  	 ram;
	private String 		 command;
	private int    		 nettyThreads;
	private int    		 parallelThreads;
	private List<String> plugins;
	private long		 timeExecuted;
	private String 		 mode;
	private String		 jarServer;
	private String		 logFolder;
	private String		 logFile;
	private boolean		 random;
	private String		 toWorld;
	private String		 rabbitHostname;
	private String		 ladderIp;
	private int			 ladderPort;
	
	public ServerConfigurationFactory(long id, String ip, long port, int slots, String bungeeName, String screenName, String world, String systemName, long ram, String command, int nettyThreads, 
			int parallelThreads, List<String> plugins, String mode, String jarServer, String logFolder, String logFile, boolean random, String toWorld, String rabbitHostname, String ladderIp, int ladderPort) {
		setId(id);
		setIp(ip);
		setPort(port);
		setSlots(slots);
		setBungeeName(bungeeName);
		setScreenName(screenName);
		setWorld(world);
		setSystem(system);
		setRam(ram);
		setCommand(command);
		setNettyThreads(nettyThreads);
		setParallelThreads(parallelThreads);
		setPlugins(plugins);
		setTimeExecuted(System.currentTimeMillis());
		setMode(mode);
		setJarServer(jarServer);
		setLogFolder(logFolder);
		setLogFile(logFile);
		setRandom(random);
		setToWorld(toWorld);
		setRabbitHostname(rabbitHostname);
		setLadderIp(ladderIp);
		setLadderPort(ladderPort);
	}
	
}
