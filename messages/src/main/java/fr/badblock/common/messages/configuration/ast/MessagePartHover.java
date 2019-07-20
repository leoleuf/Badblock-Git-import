package fr.badblock.common.messages.configuration.ast;

import java.util.Map;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MessagePartHover extends MessagePart
{
	private String messageKey;
	private Map<String, String> values;
	
	public MessagePartHover(String messageKey, Map<String, String> args)
	{
		this.messageKey = messageKey;
		this.values = args;
	}
	
	@Override
	protected TextComponent get()
	{
		TextComponent sub = this.sub.get();
		TextComponent message = null;

		sub.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[] { message }));
		
		return sub;
	}
}
