package fr.badblock.ladder.chat;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.Title;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import lombok.Getter;
import lombok.Setter;

public class LadderTitle implements Title {
	@Getter@Setter private int    fadeIn, fadeOut, stay;
	@Getter@Setter private String title, subTitle;

	public LadderTitle(String title, String subtitle){
		this.fadeIn   = 5;
		this.fadeOut  = 5;
		this.stay     = 20;

		this.title    = title;
		this.subTitle = subtitle;
	}

	@Override
	public void send(Player... players) {
		for(final Player player : players){
			player.sendPacket(new PacketPlayerChat(player.getUniqueId(), 
					ChatAction.TITLE, new String[]{title, subTitle}, 
					fadeIn, stay, fadeOut)
				);
		}
	}

	@Override
	public void broadcast(BungeeCord... servers) {
		for(final BungeeCord server : servers){
			server.sendPacket(new PacketPlayerChat(null,
					ChatAction.TITLE, new String[]{title, subTitle},
					fadeIn, stay, fadeOut)
				);
		}
	}

	@Override
	public void broadcastAll() {
		Ladder.getInstance().broadcastPacket(new PacketPlayerChat(null,
					ChatAction.TITLE, new String[]{title, subTitle},
					fadeIn, stay, fadeOut));
	}
}
