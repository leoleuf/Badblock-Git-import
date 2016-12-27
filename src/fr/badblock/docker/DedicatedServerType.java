package fr.badblock.docker;

public enum DedicatedServerType {

	ANIMATION,
	PRODUCTION,
	NONE;

	public static DedicatedServerType get(String dedicatedServerType) {
		for (DedicatedServerType serverType : values())
			if (dedicatedServerType.equalsIgnoreCase(serverType.name())) return serverType;
		return null;
	}
	
}
