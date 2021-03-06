package net.minecraft.server.v1_8_R3;

public class CommandSaveOff extends CommandAbstract {

    public CommandSaveOff() {}

    @Override
	public String getCommand() {
        return "save-off";
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.save-off.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worldServer.length; ++i) {
            if (minecraftserver.worldServer[i] != null) {
                WorldServer worldserver = minecraftserver.worldServer[i];

                if (!worldserver.savingDisabled) {
                    worldserver.savingDisabled = true;
                    flag = true;
                }
            }
        }

        if (flag) {
            a(icommandlistener, this, "commands.save.disabled", new Object[0]);
        } else {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
    }
}
