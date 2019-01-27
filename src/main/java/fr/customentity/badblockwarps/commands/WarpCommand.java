package fr.customentity.badblockwarps.commands;

import fr.customentity.badblockwarps.BadBlockWarps;
import fr.customentity.badblockwarps.data.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by CustomEntity on 27/01/2019 for BadBlockWarps.
 */
public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("badblock.admin")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sendHelpMessage(player);
            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("alreadyexist-warp").replace("%warp%", name));
                        return true;
                    }
                    Warp.createWarp(name, true, new ArrayList<>(), player.getLocation());
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("create-warp").replace("%warp%", name));
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (!Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("doesnt-exist-warp").replace("%warp%", name));
                        return true;
                    }
                    Warp.deleteWarp(Warp.getWarpByName(name));
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("remove-warp").replace("%warp%", name));
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (!Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("doesnt-exist-warp").replace("%warp%", name));
                        return true;
                    }
                    Warp warp = Warp.getWarpByName(name);
                    if (!warp.isEnabled()) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("already-disabled-warp").replace("%warp%", warp.getName()));
                        return true;
                    }
                    Warp.disableWarp(warp);
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("disable-warp").replace("%warp%", warp.getName()));
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (!Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("doesnt-exist-warp").replace("%warp%", name));
                        return true;
                    }
                    Warp warp = Warp.getWarpByName(name);
                    if (warp.isEnabled()) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("already-enabled-warp").replace("%warp%", warp.getName()));
                        return true;
                    }
                    Warp.enableWarp(warp);
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("enable-warp").replace("%warp%", warp.getName()));
                } else if (args[0].equalsIgnoreCase("tp")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (!Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("doesnt-exist-warp").replace("%warp%", name));
                        return true;
                    }
                    Warp warp = Warp.getWarpByName(name);
                    if (!warp.isEnabled()) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("is-disabled-warp").replace("%warp%", warp.getName()));
                        return true;
                    }
                    warp.teleportPlayer(player);
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("teleport-warp").replace("%warp%", warp.getName()));
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (Warp.warps.size() == 0) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("list-warp.none-warp"));
                        return true;
                    }
                    for (String str : BadBlockWarps.getInstance().getConfig().getStringList("messages.list-warp.top-message")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', str).replace("%prefix%", BadBlockWarps.getInstance().getPrefix()));
                    }
                    for (Warp warp : Warp.getEnabledWarps()) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("list-warp.enabled-warps").replace("%warp%", warp.getName()));
                    }
                    for (Warp warp : Warp.getDisabledWarps()) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("list-warp.disabled-warps").replace("%warp%", warp.getName()));
                    }
                } else if (args[0].equalsIgnoreCase("addsigns")) {
                    if (args.length != 2) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("incorrect-arguments"));
                        return true;
                    }
                    String name = args[1];
                    if (!Warp.existWarp(name)) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("doesnt-exist-warp").replace("%warp%", name));
                        return true;
                    }
                    if (BadBlockWarps.getInstance().getSelectionHashMap().containsKey(player.getName())) {
                        player.sendMessage(BadBlockWarps.getInstance().getMessage("sign-selection-cancelled-warp").replace("%warp%", name));
                        BadBlockWarps.getInstance().getSelectionHashMap().remove(player.getName());
                        return true;
                    }
                    player.sendMessage(BadBlockWarps.getInstance().getMessage("sign-selection"));
                    BadBlockWarps.getInstance().getSelectionHashMap().put(player.getName(), Warp.getWarpByName(name));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    BadBlockWarps.getInstance().reloadConfig();
                    BadBlockWarps.getInstance().updateSigns();
                } else {
                    sendHelpMessage(player);
                }
            }
        } else {
            sender.sendMessage("Vous n'êtes pas un joueur !");
        }
        return false;
    }

    private void sendHelpMessage(Player player) {
        for (String str : BadBlockWarps.getInstance().getConfig().getStringList("messages.help-warp")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", BadBlockWarps.getInstance().getPrefix())));
        }
    }
}
