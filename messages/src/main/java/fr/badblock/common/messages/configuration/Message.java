package fr.badblock.common.messages.configuration;

import java.util.Map;

import fr.badblock.common.messages.IReceiver;

public class Message
{
	private String type;
	private Map<String, String> configuration;

	public Message(String type, Map<String, String> config)
	{
		this.type = type;
		this.configuration = config;
	}

	public void send(IReceiver receiver)
	{

	}
}