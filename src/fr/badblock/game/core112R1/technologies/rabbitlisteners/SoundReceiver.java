package fr.badblock.game.core112R1.technologies.rabbitlisteners;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.game.core112R1.GamePlugin;
import fr.badblock.game.core112R1.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;

public class SoundReceiver extends RabbitListener 
{

	public SoundReceiver()
	{
		super(GameAPI.getAPI().getRabbitService(), "gameapi.sound", RabbitListenerType.SUBSCRIBER, false);
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				if (body == null)
				{
					return;
				}

				String[] splitter = body.split(";");

				String rawSound = splitter[0];

				if (rawSound.equals("NOTE_PIANO"))
				{
					rawSound = "BLOCK_NOTE_XYLOPHONE";
				}
				else if (rawSound.equals("NOTE_PLING"))
				{
					rawSound = "BLOCK_NOTE_PLING";
				}
				else if (rawSound.equals("CHICKEN_EGG_POP"))
				{
					rawSound = "ENTITY_CHICKEN_EGG";
				}
				else if (rawSound.equals("LEVEL_UP"))
				{
					rawSound = "ENTITY_PLAYER_LEVELUP";
				}

				Sound sound = getSound(rawSound);

				if (sound == null)
				{
					return;
				}

				Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						if (splitter.length == 2)
						{
							String playerName = splitter[1];

							BadblockPlayer player = BukkitUtils.getPlayer(playerName);
							if (player == null)
							{
								Optional<BadblockPlayer> pla = BukkitUtils.getAllPlayers().parallelStream().filter(pl -> 
								((GameBadblockPlayer)pl).getRealName() != null && ((GameBadblockPlayer)pl).getRealName().equalsIgnoreCase(playerName)
										).findFirst();

								if (pla == null || !pla.isPresent())
								{
									return;
								}

								player = pla.get();
							}

							player.playSound(sound);
							return;
						}

						BukkitUtils.getAllPlayers().forEach(player -> player.playSound(sound));
					}
				});
			}
		});
	}

	private static Sound getSound(String rawSound)
	{
		for (Sound sound : Sound.values())
		{
			if (sound.name().equalsIgnoreCase(rawSound))
			{
				return sound;
			}
		}

		return null;
	}

}