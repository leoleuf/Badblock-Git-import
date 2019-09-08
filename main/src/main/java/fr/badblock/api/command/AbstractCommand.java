package fr.badblock.api.command;

import fr.badblock.api.BadBlockAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {

    protected BadBlockAPI badBlockAPI;

    public AbstractCommand(BadBlockAPI badBlockAPI){
        this.badBlockAPI = badBlockAPI;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return onCommand(commandSender, s, strings);
    }

    protected abstract boolean onCommand(CommandSender sender, String label, String[] arguments);

    protected boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender || sender.isOp())
            return true;

        boolean result = false;
        if (sender instanceof Player)
           // result = badBlockAPI.getPlayerManager().getPlayerData(sender.getName()).hasPermissions(permission);

        //if (!result)
            sender.sendMessage(badBlockAPI.getConfig().getString("command.nopermission"));
        return result;
    }
}
