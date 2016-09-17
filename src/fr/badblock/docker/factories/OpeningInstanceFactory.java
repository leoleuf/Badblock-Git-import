package fr.badblock.docker.factories;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningInstanceFactory {

	private long id;
	private String ip;
	private long port;
	private int slots;
	private String bungeeName;
	private String screenName;
	private String world;
	private String system;
	private long ram;
	private String command;
	private int nettyThreads;
	private int parallelThreads;
	private long timeExecuted;
	private String mode;
	private String jarServer;
	private String logFolder;
	private String logFile;
	private boolean random;
	private String toWorld;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Process process;
	private String rabbitHostname;
	private Map<String, String> furtherInformations;

	public OpeningInstanceFactory(long id, String ip, long port, int slots, String bungeeName, String screenName,
			String world, String system, long ram, String command, int nettyThreads, int parallelThreads,
			String mode, String jarServer, String logFolder, String logFile, boolean random, String toWorld, Map<String, String> furtherInformations) {
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
		setTimeExecuted(System.currentTimeMillis());
		setMode(mode);
		setJarServer(jarServer);
		setLogFolder(logFolder);
		setLogFile(logFile);
		setRandom(random);
		setToWorld(toWorld);
		setFurtherInformations(furtherInformations);
	}

}
