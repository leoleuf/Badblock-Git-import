package net.minecraft.server.v1_8_R3;

public class CommandSeed extends CommandAbstract {

    public CommandSeed() {}

    @Override
	public boolean canUse(ICommandListener icommandlistener) {
        return MinecraftServer.getServer().T() || super.canUse(icommandlistener);
    }

    @Override
	public String getCommand() {
        return "seed";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.seed.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        Object object = icommandlistener instanceof EntityHuman ? ((EntityHuman) icommandlistener).world : MinecraftServer.getServer().getWorldServer(0);

        icommandlistener.sendMessage(new ChatMessage("commands.seed.success", new Object[] { Long.valueOf(((World) object).getSeed())}));
    }
}
