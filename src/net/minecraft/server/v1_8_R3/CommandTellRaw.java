package net.minecraft.server.v1_8_R3;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

public class CommandTellRaw extends CommandAbstract {

    public CommandTellRaw() {}

    @Override
	public String getCommand() {
        return "tellraw";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.tellraw.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new ExceptionUsage("commands.tellraw.usage", new Object[0]);
        } else {
            EntityPlayer entityplayer = a(icommandlistener, astring[0]);
            String s = a(astring, 1);

            try {
                IChatBaseComponent ichatbasecomponent = IChatBaseComponent.ChatSerializer.a(s);

                entityplayer.sendMessage(ChatComponentUtils.filterForDisplay(icommandlistener, ichatbasecomponent, entityplayer));
            } catch (JsonParseException jsonparseexception) {
                Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);

                throw new ExceptionInvalidSyntax("commands.tellraw.jsonException", new Object[] { throwable == null ? "" : throwable.getMessage()});
            }
        }
    }

    @Override
	public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, MinecraftServer.getServer().getPlayers()) : null;
    }

    @Override
	public boolean isListStart(String[] astring, int i) {
        return i == 0;
    }
}
