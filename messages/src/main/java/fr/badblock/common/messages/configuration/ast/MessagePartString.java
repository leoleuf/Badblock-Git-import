package fr.badblock.common.messages.configuration.ast;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessagePartString extends MessagePart
{
	private BaseComponent[] components;
	
	public MessagePartString(final String part)
	{
		this.components = TextComponent.fromLegacyText(part);
	}

	@Override
	protected TextComponent get()
	{
		return new TextComponent(components);
	}
}
