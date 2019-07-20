package fr.badblock.common.messages;

import net.md_5.bungee.api.chat.BaseComponent;

public interface IReceiver
{
	public void sendMessage(BaseComponent message);

	public void sendActionBar(String message);
	
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);
	
	public String getName();
}