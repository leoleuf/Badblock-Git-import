package fr.badblock.common.messages;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.common.messages.parameters.MessageParameter;
import fr.badblock.common.messages.parameters.MessageParameterInteger;
import fr.badblock.common.messages.parameters.MessageParameterString;
import lombok.NonNull;

public class Message
{
	public final String key;
	
	private Map<String, MessageParameter> messages = new HashMap<>();
	
	public Message(@NonNull final String key)
	{
		this.key = key;
	}

	public Message add(String name, String value)
	{
		this.messages.put(name, new MessageParameterString(value));
		return this;
	}
	
	public Message add(String name, long value)
	{
		this.messages.put(name, new MessageParameterInteger(value));
		return this;
	}
}
