package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandSpawnpoint extends CommandAbstract {

    public CommandSpawnpoint() {}

    @Override
	public String getCommand() {
        return "spawnpoint";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.spawnpoint.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length > 1 && astring.length < 4) {
            throw new ExceptionUsage("commands.spawnpoint.usage", new Object[0]);
        } else {
            EntityPlayer entityplayer = astring.length > 0 ? a(icommandlistener, astring[0]) : b(icommandlistener);
            BlockPosition blockposition = astring.length > 3 ? a(icommandlistener, astring, 1, true) : entityplayer.getChunkCoordinates();

            if (entityplayer.world != null) {
                entityplayer.setRespawnPosition(blockposition, true);
                a(icommandlistener, this, "commands.spawnpoint.success", new Object[] { entityplayer.getName(), Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
            }

        }
    }

    @Override
	public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, MinecraftServer.getServer().getPlayers()) : (astring.length > 1 && astring.length <= 4 ? a(astring, 1, blockposition) : null);
    }

    @Override
	public boolean isListStart(String[] astring, int i) {
        return i == 0;
    }
}
