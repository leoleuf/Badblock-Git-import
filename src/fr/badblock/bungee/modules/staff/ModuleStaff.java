package fr.badblock.bungee.modules.staff;

import fr.badblock.bungee.modules.staff.commands.ChatStaffCommand;
import fr.badblock.bungee.modules.staff.commands.OnlineStaffCommand;
import fr.badblock.bungee.modules.staff.commands.StaffCommand;
import fr.badblock.bungee.modules.staff.listeners.PlayerDisconnectChatStaffListener;
import fr.badblock.bungee.modules.staff.listeners.PlayerLoginChatStaffListener;
import fr.badblock.bungee.modules.staff.listeners.StaffContactListener;
import net.md_5.bungee.api.plugin.Plugin;

public class ModuleStaff extends Plugin
{

	@Override
	public void onEnable()
	{
		new ChatStaffCommand(this);
		new OnlineStaffCommand(this);
		new StaffCommand(this);

		new PlayerDisconnectChatStaffListener(this);
		new PlayerLoginChatStaffListener(this);
		new StaffContactListener(this);
	}
	
}