package net.minecraft.server.v1_8_R3;

import java.util.Iterator;

public class CommandGamemodeDefault extends CommandGamemode {

    public CommandGamemodeDefault() {}

    @Override
	public String getCommand() {
        return "defaultgamemode";
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.defaultgamemode.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new ExceptionUsage("commands.defaultgamemode.usage", new Object[0]);
        } else {
            WorldSettings.EnumGamemode worldsettings_enumgamemode = this.h(icommandlistener, astring[0]);

            this.a(worldsettings_enumgamemode);
            a(icommandlistener, this, "commands.defaultgamemode.success", new Object[] { new ChatMessage("gameMode." + worldsettings_enumgamemode.b(), new Object[0])});
        }
    }

    protected void a(WorldSettings.EnumGamemode worldsettings_enumgamemode) {
        MinecraftServer minecraftserver = MinecraftServer.getServer();

        minecraftserver.setGamemode(worldsettings_enumgamemode);
        EntityPlayer entityplayer;

        if (minecraftserver.getForceGamemode()) {
            for (Iterator iterator = MinecraftServer.getServer().getPlayerList().v().iterator(); iterator.hasNext(); entityplayer.fallDistance = 0.0F) {
                entityplayer = (EntityPlayer) iterator.next();
                entityplayer.a(worldsettings_enumgamemode);
            }
        }

    }
}
