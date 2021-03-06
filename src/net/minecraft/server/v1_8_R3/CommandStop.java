package net.minecraft.server.v1_8_R3;

public class CommandStop extends CommandAbstract {

    public CommandStop() {}

    @Override
	public String getCommand() {
        return "stop";
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.stop.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (MinecraftServer.getServer().worldServer != null) {
            a(icommandlistener, this, "commands.stop.start", new Object[0]);
        }

        MinecraftServer.getServer().safeShutdown();
    }
}
