package fr.badblock.ladder.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.http.SlackMessage;
import lombok.Data;

public class CommandGiveKey extends Command {
	public static final String username_regex = "^[a-zA-Z0-9_-]{3,15}$";

	public CommandGiveKey() {
		super("givekey", "ladder.command.givekey");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			help(sender);
			return;
		}

		int id;

		try {
			id = Integer.parseInt(args[0]);
		} catch(Exception e){
			help(sender);
			return;
		}

		List<String> players =  new ArrayList<>();

		if(args[1].equals("-l")){
			if(args.length < 3){
				help(sender);
				return;
			}

			new Thread("theSearcherOfKeys") {
				@Override
				public void run() {
					try {
						URL url = new URL(args[2]);

						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestProperty("User-Agent", "Mozilla/5.0");
						connection.setConnectTimeout(10000);
						connection.setReadTimeout(10000);

						if(connection.getResponseCode() != 200){
							throw new Exception("Error code: " + connection.getResponseCode());
						}

						BufferedReader reader = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8") );

						String line = null;

						while((line = reader.readLine()) != null)
							if(line.matches(username_regex))
								players.add(line);
						workOnIt(sender, players, id);
					} catch(Exception e){
						e.printStackTrace();
						sender.sendMessage(ChatColor.RED + "Improssible de lire la page : " + e.getMessage());

						return;
					}
				}
			}.start();
		} else {
			for(int i=1;i<args.length;i++){
				if(args[i].matches(username_regex))
					players.add(args[i]);
			}
			workOnIt(sender, players, id);
		}

		
	}
	
	private void workOnIt(CommandSender sender, List<String> players, int id) {
		if(players.size() == 0){
			sender.sendMessage(ChatColor.RED + "Aucun joueur trouvé pour appliquer le give !");
		} else {
			List<String> added = new ArrayList<>();

			players.stream().map(username -> Ladder.getInstance().getOfflinePlayer(username)).filter(p -> p.hasPlayed()).forEach(player -> {
				JsonObject hub = getAndAdd(player.getData(), "game", "other", "hub");

				JsonArray array = hub.has("chests") ? hub.get("chests").getAsJsonArray() : new JsonArray();
				array.add( Ladder.getInstance().getGson().toJsonTree( new CustomChest(id, false) ));
				hub.add("chests", array);

				added.add(player.getName());
				player.saveData();

				if(player instanceof Player){
					Player p = (Player) player;
					p.sendToBukkit("game");
				}
			});

			new SlackMessage("Ajout d'une clé (" + id + ") à " + StringUtils.join(added, ", ") + " par " + sender.getName(), "BottyChest", false, SlackMessage.KEYS_CHANNEL).run();;
		}
	}

	private JsonObject getAndAdd(JsonObject object, String... keys){
		for(String key : keys){
			if(object.has(key))
				object = object.get(key).getAsJsonObject();
			else{
				JsonObject res = new JsonObject();

				object.add(key, res);
				object = res;
			}
		}

		return object;
	}

	private void help(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "Utilisation n°1 : /givekey <id> <player1> <player2> ...");
		sender.sendMessage(ChatColor.RED + "Utilisation n°2 : /givekey <id> -l <url>");
	}

	@Data
	public class CustomChest {
		public UUID			uuid;
		public int 	   		typeId;
		public boolean 		opened;

		public CustomChest(int typeId, boolean opened) {
			this.setUuid(UUID.randomUUID());
			this.setTypeId(typeId);
			this.setOpened(opened);
		}
	}
}