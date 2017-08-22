package fr.badblock.rabbitconnector;

public enum RabbitListenerType {

	MESSAGE_BROKER,
	SUBSCRIBER;

	public static RabbitListenerType get(String name) {
		for (RabbitListenerType type : values())
			if (type.name().equalsIgnoreCase(name)) return type;
		return null;
	}
	
}
