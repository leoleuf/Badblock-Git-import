package fr.badblock.bungeecord.plugins.others.commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CodeCommand extends Command {

	public CodeCommand() {
		super("code");
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

	public static Entry<Integer, String> getURLSource(String url, String playerName, String code) throws IOException
	{
		URL urlObject = new URL(url);
		HttpsURLConnection urlConnection = (HttpsURLConnection) urlObject.openConnection();

		//add reuqest header
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		String urlParameters = "internal_username=" + playerName + "&code=" + code;

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

		if (args.length < 1)
		{
			sender.sendMessage(" §6Pour recharger ton compte BadBlock, envoie");
			sender.sendMessage(" §6par SMS §bCODE §aau §d83303");
			sender.sendMessage(" ");
			sender.sendMessage(" §d1250 points boutique (équivalent à 4,5€)");
			sender.sendMessage(" §aSaisis le code reçu par SMS en faisant");
			sender.sendMessage(" §c/code §bLECODE");
			sender.sendMessage(" §7Exemple: /code D9A1Q2Z");
			return;
		}

		String code = args[0];

		new Thread()
		{
			@Override
			public void run()
			{
				ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
				String url = "https://badblock.fr/shop/recharge/dedipass-process-ig";
				String source;
				try {
					Entry<Integer, String> entry = getURLSource(url, proxiedPlayer.getName(), code);

					source = entry.getValue();

					if (entry.getKey() >= 200 && entry.getKey() <= 299)
					{
						if (source != null && source.contains("invalide"))
						{
							proxiedPlayer.sendMessage("§cLe code entré est incorrect. Veuillez vérifier votre code. En cas de problème, envoyez un ticket sur le Forum de BadBlock.");
							return;
						}
						
						source = source.replace("&times;", "");
						source = source.trim();

						proxiedPlayer.sendMessage(source);
					}
					else
					{
						proxiedPlayer.sendMessage("§cUn problème est survenu lors de la validation du code (2). Veuillez contacter un administrateur de BadBlock.");
					}
				} catch (IOException e) {
					e.printStackTrace();
					proxiedPlayer.sendMessage("§cUn problème est survenu lors de la validation du code. Veuillez contacter un administrateur de BadBlock.");
				}
			}
		}.start();
	}

}