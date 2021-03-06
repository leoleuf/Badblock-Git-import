package net.minecraft.server.v1_8_R3;

import java.util.List;
import java.util.Random;

public class CommandWeather extends CommandAbstract {

    public CommandWeather() {}

    @Override
	public String getCommand() {
        return "weather";
    }

    @Override
	public int a() {
        return 2;
    }

    @Override
	public String getUsage(ICommandListener icommandlistener) {
        return "commands.weather.usage";
    }

    @Override
	public void execute(ICommandListener icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring.length <= 2) {
            int i = (300 + (new Random()).nextInt(600)) * 20;

            if (astring.length >= 2) {
                i = a(astring[1], 1, 1000000) * 20;
            }

            WorldServer worldserver = MinecraftServer.getServer().worldServer[0];
            WorldData worlddata = worldserver.getWorldData();

            if ("clear".equalsIgnoreCase(astring[0])) {
                worlddata.i(i);
                worlddata.setWeatherDuration(0);
                worlddata.setThunderDuration(0);
                worlddata.setStorm(false);
                worlddata.setThundering(false);
                a(icommandlistener, this, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(astring[0])) {
                worlddata.i(0);
                worlddata.setWeatherDuration(i);
                worlddata.setThunderDuration(i);
                worlddata.setStorm(true);
                worlddata.setThundering(false);
                a(icommandlistener, this, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(astring[0])) {
                    throw new ExceptionUsage("commands.weather.usage", new Object[0]);
                }

                worlddata.i(0);
                worlddata.setWeatherDuration(i);
                worlddata.setThunderDuration(i);
                worlddata.setStorm(true);
                worlddata.setThundering(true);
                a(icommandlistener, this, "commands.weather.thunder", new Object[0]);
            }

        } else {
            throw new ExceptionUsage("commands.weather.usage", new Object[0]);
        }
    }

    @Override
	public List<String> tabComplete(ICommandListener icommandlistener, String[] astring, BlockPosition blockposition) {
        return astring.length == 1 ? a(astring, new String[] { "clear", "rain", "thunder"}) : null;
    }
}
