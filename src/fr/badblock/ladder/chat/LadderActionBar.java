package fr.badblock.ladder.chat;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ActionBar;
import fr.badblock.ladder.api.entities.BungeeCord;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import lombok.Getter;
import lombok.Setter;

public class LadderActionBar implements ActionBar {
	@Getter@Setter private int    fadeIn, fadeOut, stay;
	@Getter@Setter private String message;

	public LadderActionBar(String message){
		this.fadeIn  = 5;
		this.fadeOut = 5;
		this.stay    = 20;

		this.message = message;
	}

	@Override
	public void send(Player... players) {
		for(final Player player : players){
			player.sendPacket(new PacketPlayerChat(player.getName(), ChatAction.ACTION_BAR, new String[]{message}, fadeIn, stay, fadeOut));
		}
	}

	@Override
	public void broadcast(BungeeCord... servers) {
		for(final BungeeCord server : servers){
			server.sendPacket(new PacketPlayerChat(null, ChatAction.ACTION_BAR, new String[]{message}, fadeIn, stay, fadeOut));
		}
	}

	@Override
	public void broadcastAll() {
		Ladder.getInstance().broadcastPacket(new PacketPlayerChat(null, ChatAction.ACTION_BAR, new String[]{message}, fadeIn, stay, fadeOut));
	}
}
