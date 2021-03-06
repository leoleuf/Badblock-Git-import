package net.minecraft.server.v1_8_R3;

import java.util.List;

import com.mojang.authlib.GameProfile;

public class CommandDeop extends CommandAbstract {

    public CommandDeop() {}

    @Override
	public String getCommand() {
        return "deop";
    }

    @Override
	public int a() {
        return 3;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.deop.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length == 1 && astring[0].length() > 0) {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.getPlayerList().getOPs().a(astring[0]);

            if (gameprofile == null) {
                throw new CommandException("commands.deop.failed", new Object[] { astring[0]});
            } else {
                minecraftserver.getPlayerList().removeOp(gameprofile);
                a(icommandlistener, this, "commands.deop.success", new Object[] { astring[0]});
            }
        } else {
            throw new ExceptionUsage("commands.deop.usage", new Object[0]);
        }
    }

    @Override
	public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, MinecraftServer.getServer().getPlayerList().n()) : null;
    }
}
