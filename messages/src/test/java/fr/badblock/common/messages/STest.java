package fr.badblock.common.messages;

import org.junit.Test;

import net.md_5.bungee.api.chat.TextComponent;

public class STest
{
	@Test
	public void testCoucou()
	{
		System.out.println(TextComponent.fromLegacyText("&cJe suis un chat ").length);
	}
}
