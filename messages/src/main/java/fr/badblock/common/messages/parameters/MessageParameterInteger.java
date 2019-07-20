package fr.badblock.common.messages.parameters;

public class MessageParameterInteger extends MessageParameter
{
	public final long value;
	
	public MessageParameterInteger(final long value)
	{
		this.value = value;
	}
	
	public MessageParameterInteger(final int value)
	{
		this((long) value);
	}

	@Override
	public MessageParameter getChild(String name)
	{
		switch (name)
		{
			case "hex":
				return new MessageParameterString(Long.toHexString(value));
			case "bin":
				return new MessageParameterString(Long.toBinaryString(value));
			default:
				throw unknownChild(name);
		}
	}

	@Override
	public String toString()
	{
		return Long.toString(value);
	}
}