package fr.badblock.common.messages.parameters;

public class MessageParameterString extends MessageParameter
{
	public final String value;
	
	public MessageParameterString(final String value)
	{
		this.value = (value == null ? "null" : value);
	}

	@Override
	public MessageParameter getChild(String name)
	{
		throw unknownChild(name);
	}

	@Override
	public String toString()
	{
		return value;
	}
}