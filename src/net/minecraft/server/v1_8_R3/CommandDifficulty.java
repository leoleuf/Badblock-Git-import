package net.minecraft.server.v1_8_R3;

import java.util.List;

public class CommandDifficulty extends CommandAbstract {

    public CommandDifficulty() {}

    @Override
	public String getCommand() {
        return "difficulty";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.difficulty.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new ExceptionUsage("commands.difficulty.usage", new Object[0]);
        } else {
            EnumDifficulty enumdifficulty = this.e(astring[0]);

            MinecraftServer.getServer().a(enumdifficulty);
            a(icommandlistener, this, "commands.difficulty.success", new Object[] { new ChatMessage(enumdifficulty.b(), new Object[0])});
        }
    }

    protected EnumDifficulty e(String s) throws ExceptionInvalidNumber {
        return !s.equalsIgnoreCase("peaceful") && !s.equalsIgnoreCase("p") ? (!s.equalsIgnoreCase("easy") && !s.equalsIgnoreCase("e") ? (!s.equalsIgnoreCase("normal") && !s.equalsIgnoreCase("n") ? (!s.equalsIgnoreCase("hard") && !s.equalsIgnoreCase("h") ? EnumDifficulty.getById(a(s, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    @Override
	public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, new String[] { "peaceful", "easy", "normal", "hard"}) : null;
    }
}
