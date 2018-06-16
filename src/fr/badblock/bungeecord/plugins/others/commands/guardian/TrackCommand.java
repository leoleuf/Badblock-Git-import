package fr.badblock.bungeecord.plugins.others.commands.guardian;

import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TrackCommand extends Command {

	public TrackCommand() {
		super("track", "guardian.modo");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage("§8(§b§lGuardian§8) §bVous devez être un joueur pour pouvoir exécuter cette commande.");
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if (args.length != 1) {
			sender.sendMessage("§8(§b§lGuardian§8) Utilisation: §b/track <pseudo>");
			return;
		}
		String pseudo = args[0];
		ProxiedPlayer p = BungeeCord.getInstance().getPlayer(pseudo);
		if (p == null) {
			sender.sendMessage("§8(§b§lGuardian§8) §8Le joueur '" + pseudo + "' n'est pas connecté.");
			return;
		}
		String message = "";
		int i = 0;
		for (String arg : args) {
			i++;
			String spacer = " ";
			if (args.length == i)
				spacer = "";
			message += arg + spacer;
		}
		BadBlockBungeeOthers.getInstance().getProxy().getConsole()
				.sendMessage("[Guardian] " + player.getName() + " (on the server "
						+ player.getServer().getInfo().getName() + ") issued server command: /track " + message
						+ " (on the server " + p.getServer().getInfo().getName() + ")");
		if (p.getServer() == null || p.getServer().getInfo() == null || p.getServer().getInfo().getName() == null
				|| p.getServer().getInfo().getName().startsWith("login")) {
			player.sendMessage(
					"§8(§b§lGuardian§8) §cCe joueur est entrain de se connecter, impossible de vous y téléporter.");
			return;
		}
		if (player.getServer().getInfo().getName().equals(p.getServer().getInfo().getName())) {
			// Déjà connecté sur son serveur, on va envoyer un joli message au serveur où il
			// est
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Vanish");
			out.writeUTF(player.getName());
			out.writeUTF(p.getName());
			player.getServer().sendData("Guardian", out.toByteArray());
			return;
		} else {
			player.connect(p.getServer().getInfo());
			BungeeCord.getInstance().getScheduler().schedule(BadBlockBungeeOthers.getInstance(), new Runnable() {
				@Override
				public void run() {
					ProxiedPlayer plo = (ProxiedPlayer) sender;
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Vanish");
					out.writeUTF(plo.getName());
					out.writeUTF(p.getName());
					plo.getServer().sendData("Guardian", out.toByteArray());
				}
			}, 1, TimeUnit.SECONDS);
		}
	}

}
