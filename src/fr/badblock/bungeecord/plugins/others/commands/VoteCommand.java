package fr.badblock.bungeecord.plugins.others.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class VoteCommand extends Command {

	public VoteCommand() {
		super("vote");
	}

	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	public static String removeTags(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}

		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}

	private static String toString(InputStream inputStream) throws IOException
	{
		if (inputStream == null)
		{
			return null;
		}

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")))
		{
			String inputLine;
			StringBuilder stringBuilder = new StringBuilder();
			while ((inputLine = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(inputLine);
			}

			return stringBuilder.toString();
		}
	}

	public static Entry<Integer, String> getURLSource(String url, String playerName, int typeId, String ip) throws IOException
	{
		URL urlObject = new URL(url);
		HttpsURLConnection urlConnection = (HttpsURLConnection) urlObject.openConnection();

		//add reuqest header
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		String urlParameters = "pseudo=" + playerName + "&type=" + typeId + "&internal_ip=" + ip;

		// Send post request
		urlConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		InputStream io = null;
		String bo = null;
		if (urlConnection != null)
		{
			if (urlConnection.getResponseCode() >= 200 &&  urlConnection.getResponseCode() <= 299)
			{
				io = urlConnection.getInputStream();
				bo = toString(io);
			}
		}

		return new AbstractMap.SimpleEntry<Integer, String>(urlConnection.getResponseCode(), bo);
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))
		{
			return;
		}
		if (args.length < 1) {
			TextComponent textComponent = new TextComponent("§b§l§nClique ici pour voter et ouvrir le lien");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique ici pour voter !").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://badblock.fr/svote"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§aClique, remplis le captcha et vote.");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique ici pour voter !").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://badblock.fr/svote"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("  ");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique ici pour voter !").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://badblock.fr/svote"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§eDeuxième étape, reçois ta récompense :");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique ici pour voter !").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://badblock.fr/svote"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§b- §dClique pour prendre ta récompense §aSkyBlock");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dClique ici pour récupérer ta récompense §aSkyBlock §d!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote skyblock"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§b- §dClique pour prendre ta récompense §eMiniJeux");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dClique ici pour récupérer ta récompense §eMiniJeux §d!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote minijeux"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§b- §dClique pour prendre ta récompense §cFaction");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dClique ici pour récupérer ta récompense §cFaction §d!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote faction"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§b- §dClique pour prendre ta récompense en §6Points Boutique");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dClique ici pour récupérer ta récompense en §6Points Boutique §d!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote ptsboutique"));
			sender.sendMessage(textComponent);
			textComponent = new TextComponent("§b- §dClique pour prendre ta récompense §3PvPBox");
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dClique ici pour récupérer ta récompense §3PvPBox §d!").create()));
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote pvpbox"));
			sender.sendMessage(textComponent);
			return;
		}

		String server = args[0];

		if (!server.equalsIgnoreCase("skyblock") && !server.equalsIgnoreCase("faction")
				&& !server.equalsIgnoreCase("minijeux") && !server.equalsIgnoreCase("ptsboutique") && !server.equalsIgnoreCase("pvpbox"))
		{
			sender.sendMessage("§cServeur invalide.");
			sender.sendMessage("§ePour recevoir ta récompense, il faut taper une des deux commandes :");
			sender.sendMessage("§b- §d/vote skyblock §bpour récupérer une récompense §aSkyBlock");
			sender.sendMessage("§b- §d/vote minijeux §bpour récupérer une récompense §eMiniJeux");
			sender.sendMessage("§b- §d/vote faction §bpour récupérer une récompense §cFaction");
			sender.sendMessage("§b- §d/vote ptsboutique §bpour récupérer une récompense §6Boutique");
			sender.sendMessage("§b- §d/vote pvpbox §bpour récupérer une récompense en §3PvPBox");
			return;
		}

		int id = 1;

		if (server.equalsIgnoreCase("ptsboutique"))
		{
			id = 1;
		}
		else if (server.equalsIgnoreCase("skyblock"))
		{
			id = 2;
		}
		else if (server.equalsIgnoreCase("minijeux"))
		{
			id = 3;
		}
		else if (server.equalsIgnoreCase("faction"))
		{
			id = 4;
		}
		else if (server.equalsIgnoreCase("pvpbox"))
		{
			id = 5;
		}

		final int typeId = id;

		new Thread()
		{
			@Override
			public void run()
			{
				ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
				// sender.getName() + "&ip=" + proxiedPlayer.getAddress().getAddress().getHostAddress()
				String url = "https://badblock.fr/vote/award";
				String source;
				try {
					Entry<Integer, String> s = getURLSource(url, proxiedPlayer.getName(), typeId, proxiedPlayer.getAddress().getAddress().getHostAddress());
					source = s.getValue();

					if (s.getKey() == 404)
					{
						proxiedPlayer.sendMessage("§cVous devez être inscrit sur le Site Internet pour pouvoir utiliser la commande /vote. Inscris-toi avece le pseudo §e"
								+ proxiedPlayer.getName() + " §csur §b§nhttps://badblock.fr/");
						return;
					}

					if (s.getKey() == 405)
					{
						proxiedPlayer.sendMessage("§cVous n'avez pas voté. Si vous avez quand-même voté, essayez de récupérer votre récompense depuis le site Internet" 
								+ " §b§nhttps://badblock.fr/");
						return;
					}

					if (source == null)
					{
						proxiedPlayer.sendMessage("§cUne erreur est survenue lors du vote..");
						return;
					}

					source = removeTags(source);
					source = source.replace("&times;", "");
					source = source.trim();

					String l = "";
					for (char c : source.toCharArray())
					{
						if (Character.isLetter(c) || Character.isDigit(c) || Character.isSpace(c) || c == '.' || c == '\'')
						{
							l += c;
						}
					}
					l = l.trim();

					if (!source.toLowerCase().contains("gagn"))
					{
						proxiedPlayer.sendMessage("§c" + s);
					}
					else
					{
						proxiedPlayer.sendMessage("§a" + s);
					}
				} catch (IOException e) {
					e.printStackTrace();
					proxiedPlayer.sendMessage("§cUn problème est survenu lors du vote. Veuillez contacter un administrateur de BadBlock.");
				}
			}
		}.start();
	}

}