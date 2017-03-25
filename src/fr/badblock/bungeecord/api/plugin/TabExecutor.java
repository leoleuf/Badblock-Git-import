package fr.badblock.bungeecord.api.plugin;

import fr.badblock.bungeecord.api.CommandSender;

public interface TabExecutor
{

    public Iterable<String> onTabComplete(CommandSender sender, String[] args);
}
