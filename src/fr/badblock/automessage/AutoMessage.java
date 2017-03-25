package fr.badblock.automessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.plugins.Plugin;
import fr.badblock.ladder.api.plugins.PluginsManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class AutoMessage extends Plugin {

	@Getter @Setter private static AutoMessage	instance;

	private Gson 								gson;
	private AutoMessageConfig					configuration;
	private boolean								reload;

	@Override
	public void onEnable() {
		setInstance(this);
		Ladder ladder = Ladder.getInstance();
		PluginsManager pluginsManager = ladder.getPluginsManager();
		pluginsManager.registerCommand(this, new AutoMessageCommand());
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		this.setGson(gsonBuilder.create());
		this.reloadConfiguration();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Getter @Setter private long								seconds;
			@Getter Map<InsistentMessage, Integer>		ids = new HashMap<>();
			@Override
			public void run() {
				if (reload) {
					getIds().clear();
					this.seconds = 0;
					reload = false;
				}
				setSeconds(getSeconds() + 1);
				getConfiguration().getInsistentMessages().stream().filter(insistentMessage -> getSeconds() % insistentMessage.getEveryXSeconds() == 0).forEach(insistentMessage -> {
					int id = 0;
					if (getIds().containsKey(insistentMessage)) id = getIds().get(insistentMessage) + 1;
					getIds().put(insistentMessage, id);
					// Back to start ? ;o
					if (id > insistentMessage.getMessage().size() - 1) {
						id = 0; 
						getIds().put(insistentMessage, 0);
					}
					String[] messages = insistentMessage.getMessage().get(id);
					if (messages != null) {
						final String[] shownMessages = getColoredMessages(messages);
						// For everyone
						if (insistentMessage.getPermission() == null || insistentMessage.getPermission().isEmpty())
							ladder.broadcast(shownMessages);
						else // For permissible players
							ladder.getOnlinePlayers().stream().filter(player -> player.hasPermission(insistentMessage.getPermission())).forEach(player -> player.sendMessages(shownMessages));
					}
				});
			}
		}, 1000, 1000);
	}

	private static String[] getColoredMessages(String message[]) {
		for (int i = 0; i < message.length; i++)
			if (message[i] != null)
				message[i] = ChatColor.translateAlternateColorCodes('&', message[i]);
		return message;
	}

	public void reloadConfiguration() {
		File folder = getDataFolder();
		if (!folder.exists()) folder.mkdirs();
		File file = new File(getDataFolder(), "config.json");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		String o = "";
		try {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					o += line;
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		AutoMessageConfig autoMessageConfig = o.isEmpty() ? new AutoMessageConfig() : this.getGson().fromJson(o, AutoMessageConfig.class);
		setConfiguration(autoMessageConfig);
		if (o.isEmpty()) {
			// save all messages
			try {
				byte data[] = getGson().toJson(this.getConfiguration()).getBytes();
				FileOutputStream out = new FileOutputStream(new File(this.getDataFolder(), "config.json"));
				out.write(data);
				out.close();
			}catch(Exception error) {
				error.printStackTrace();
			}
		}
		this.reload = true;
	}

}
