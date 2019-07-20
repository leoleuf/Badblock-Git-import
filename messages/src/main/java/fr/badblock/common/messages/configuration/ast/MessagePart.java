package fr.badblock.common.messages.configuration.ast;

import net.md_5.bungee.api.chat.TextComponent;

public abstract class MessagePart
{
	public MessagePart sub;
	public MessagePart next;
	
	protected abstract TextComponent get();

	public TextComponent construct()
	{
		TextComponent component = get();
		component.addExtra(next.construct());

		return component;
	}
}
