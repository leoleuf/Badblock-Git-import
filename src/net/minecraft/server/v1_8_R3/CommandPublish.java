package net.minecraft.server.v1_8_R3;

public class CommandPublish extends CommandAbstract {

    public CommandPublish() {}

    @Override
	public String getCommand() {
        return "publish";
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.publish.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        String s = MinecraftServer.getServer().a(WorldSettings.EnumGamemode.SURVIVAL, false);

        if (s != null) {
            a(icommandlistener, this, "commands.publish.started", new Object[] { s});
        } else {
            a(icommandlistener, this, "commands.publish.failed", new Object[0]);
        }

    }
}
