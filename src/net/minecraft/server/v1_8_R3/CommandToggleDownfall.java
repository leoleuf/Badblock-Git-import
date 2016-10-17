package net.minecraft.server.v1_8_R3;

public class CommandToggleDownfall extends CommandAbstract {

    public CommandToggleDownfall() {}

    @Override
	public String getCommand() {
        return "toggledownfall";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.downfall.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        this.d();
        a(icommandlistener, this, "commands.downfall.success", new Object[0]);
    }

    protected void d() {
        WorldData worlddata = MinecraftServer.getServer().worldServer[0].getWorldData();

        worlddata.setStorm(!worlddata.hasStorm());
    }
}
