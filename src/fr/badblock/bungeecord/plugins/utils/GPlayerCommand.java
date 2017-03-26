package fr.badblock.bungeecord.plugins.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import fr.badblock.bungeecord.api.CommandSender;
import fr.badblock.bungeecord.api.plugin.Command;
import fr.badblock.bungeecord.plugins.ladder.LadderBungee;

public class GPlayerCommand extends Command {

	public GPlayerCommand() {
		super("gplayer", "bungeeutils.gplayer");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) return;
		try{
			File file = new File("players.dat");
			if (file.exists()) file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter writer = new PrintWriter("players.dat", "UTF-8");
			for (String string : LadderBungee.getInstance().connectPlayers) {

				writer.println(string);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
