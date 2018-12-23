package fr.badblock.bungee.modules.staff;

import fr.badblock.bungee.modules.staff.commands.ChatStaffCommand;
import fr.badblock.bungee.modules.staff.commands.OnlineStaffCommand;
import net.md_5.bungee.api.plugin.Plugin;

public class ModuleStaff extends Plugin
{

	@Override
	public void onEnable()
	{
		new ChatStaffCommand(this);
		new OnlineStaffCommand(this);
	}
	
}