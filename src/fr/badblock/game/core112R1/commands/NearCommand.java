package fr.badblock.game.core112R1.commands;

import java.util.Iterator;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class NearCommand extends AbstractCommand {

	public NearCommand() {
		super("near", new TranslatableString("hub.loc"), "skyblock.near");
		allowConsole(false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;

		StringBuilder stringBuilder = new StringBuilder();

		Iterator<Entity> iterator = player.getNearbyEntities(150, 255, 150).parallelStream().
				filter(en -> EntityType.PLAYER.equals(en.getType())).collect(Collectors.toList()).iterator();

		int entities = 0;
		while (iterator.hasNext())
		{
			Entity entity = iterator.next();

			if (entity instanceof BadblockPlayer)
			{
				if (!player.canSee((BadblockPlayer) entity))
				{
					continue;
				}
			}

			stringBuilder.append("§a" + entity.getName());
			entities++;

			if (iterator.hasNext())
			{
				stringBuilder.append("§7, ");
			}
		}

		if (entities > 0)
		{
			player.sendMessage("§7Joueurs aux alentours (" + entities + ") : " + stringBuilder.toString());
			return true;
		}

		player.sendMessage("§cAucun joueur autour de vous.");
		return true;
	}
}
