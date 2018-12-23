package fr.badblock.bungee.modules.modo;

import fr.badblock.bungee.modules.modo.commands.MCommand;
import fr.badblock.bungee.modules.modo.commands.ModoCommand;
import fr.badblock.bungee.modules.modo.commands.objects.PunishmentReasons;
import fr.badblock.bungee.modules.modo.listeners.ModoSessionStartListener;
import fr.badblock.bungee.modules.modo.listeners.ModoSessionStopListener;
import fr.badblock.bungee.modules.modo.listeners.PunishmentBannedIPListener;
import fr.badblock.bungee.modules.modo.listeners.PunishmentBannedListener;
import fr.badblock.bungee.modules.modo.listeners.PunishmentMutedListener;
import fr.badblock.bungee.modules.modo.listeners.PunishmentWarnReconnectListener;
import net.md_5.bungee.api.plugin.Plugin;

public class ModuleModo extends Plugin
{
	
	@Override
	public void onEnable()
	{
		new PunishmentReasons();
		
		new MCommand(this);
		new ModoCommand(this);

		new ModoSessionStartListener(this);
		new ModoSessionStopListener(this);
		new PunishmentBannedIPListener(this);
		new PunishmentBannedListener(this);
		new PunishmentMutedListener(this);
		new PunishmentWarnReconnectListener(this);
	}
	
}