package fr.badblock.common.messages.parameters;

public class MessageParameterFloat extends MessageParameter
{
	public final double value;
	
	public MessageParameterFloat(final double value)
	{
		this.value = value;
	}
	
	public MessageParameterFloat(final float value)
	{
		this((double) value);
	}

	@Override
	public MessageParameter getChild(String name)
	{
		if ("hex".equals(name))
			return new MessageParameterString(Double.toHexString(value));

		throw unknownChild(name);
	}

	@Override
	public String toString()
	{
		return Double.toString(value);
	}
}