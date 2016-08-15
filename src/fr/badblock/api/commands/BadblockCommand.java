package fr.badblock.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.api.game.BPlayer;
import fr.badblock.api.game.BPlayersManager;

public class BadblockCommand extends AbstractCommand{
	public BadblockCommand() {
		super("minigames.player", "%gold%Utilisation : /badblock", true);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player concerned = null;
		if(!(sender instanceof Player)){
			sendHelp(sender);
		} else if(args.length > 0){
			concerned = Bukkit.getPlayer(args[0]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			sendMessage(sender, "%red%Le joueur " + args[0] + " est introuvable.");
		} else {
			BPlayer player = BPlayersManager.getInstance().getPlayer(concerned);
			int percent = (int) Math.round(((double)player.getXP() / (double)player.getNeededXP()) * 100);
			
			String result = "&a";
			
			for(int i=0;i<100;i++){
				if(i == percent)
					result += "&7";
				result += "|";
			}
			if(args.length == 0){
				player.sendMessage("&8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»&b &b&lStatistiques &8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»");
				player.sendMessage("&6Vous avez &a" + player.getCoins() + " BadCoins &6!");
				player.sendMessage("&6Vous êtes niveau &a" + player.getLevel() + " &6à &a" + percent + "% (" + player.getXP() + " XP / " + player.getNeededXP() + ")&6.");
				player.sendMessage(result);
				player.sendMessage("&8&l«&b&l-&8&l»&m-----------------------------&f&8&l«&b&l-&8&l»&b");
			} else {
				sendMessage(sender, "&8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»&b &b&lStatistiques &8&l«&b&l-&8&l»&m------&f&8&l«&b&l-&8&l»");
				sendMessage(sender, "&6" + player.getPlayerName() + " a &a" + player.getCoins() + " BadCoins &6!");
				sendMessage(sender, "&6" + player.getPlayerName() + " est niveau &a" + player.getLevel() + " &6à &a" + percent + "% (" + player.getXP() + " XP / " + player.getNeededXP() + ")&6.");
				sendMessage(sender, result);
				sendMessage(sender, "&8&l«&b&l-&8&l»&m-----------------------------&f&8&l«&b&l-&8&l»&b");
			}
			
		}
	}
	public static void feed(Player concerned){
		concerned.setFoodLevel(20);
	}
}