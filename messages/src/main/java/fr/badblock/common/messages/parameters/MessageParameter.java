package fr.badblock.common.messages.parameters;

public abstract class MessageParameter
{
	public abstract MessageParameter getChild(String name);

	public MessageParameter getSubscript(String name)
	{
		throw new IllegalArgumentException("no subscript");
	}

	@Override
	public abstract String toString();

	public RuntimeException unknownChild(String child)
	{
		throw new IllegalArgumentException("unknown child: " + child);
	}
}
