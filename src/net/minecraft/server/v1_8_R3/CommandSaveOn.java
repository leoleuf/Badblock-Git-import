package net.minecraft.server.v1_8_R3;

public class CommandSaveOn extends CommandAbstract {

    public CommandSaveOn() {}

    @Override
	public String getCommand() {
        return "save-on";
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.save-on.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worldServer.length; ++i) {
            if (minecraftserver.worldServer[i] != null) {
                WorldServer worldserver = minecraftserver.worldServer[i];

                if (worldserver.savingDisabled) {
                    worldserver.savingDisabled = false;
                    flag = true;
                }
            }
        }

        if (flag) {
            a(icommandlistener, this, "commands.save.enabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
        }
    }
}
