package fr.badblock.ladder.http;


import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;

public class SlackMessage
{
	public static final String KEYS_CHANNEL = "info_given-keys";
	
	private final String message;
	private final String name;
	private final boolean useMarkdown;
	private final String webhookUrl = "https://hooks.slack.com/services/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";
	private String channel = "prive_perms";
	
	public SlackMessage(String message, String name, boolean useMarkdown)
	{
		this.message = message;
		this.name = name;
		this.useMarkdown = useMarkdown;
	}
	
	public SlackMessage(String message, String name, boolean useMarkdown, String channel)
	{
		this(message, name, useMarkdown);
		
		this.channel = channel;
	}
	
	public void run()
	{
		new Thread() {
			@Override
			public void run() {
				JsonObject json = new JsonObject();
				
				json.addProperty("text", message);
				json.addProperty("username", name);
				json.addProperty("link_names", 1);
				json.addProperty("parse", "full");
				json.addProperty("icon_emoji", ":klabaconfiant:");
				json.addProperty("channel", channel);
				json.addProperty("mrkdwn", Boolean.valueOf(useMarkdown));
				
				String jsonStr = "payload=" + json.toString();
				
				try
				{
					HttpURLConnection webhookConnection = (HttpURLConnection)new URL(webhookUrl).openConnection();
					webhookConnection.setRequestMethod("POST");
					webhookConnection.setDoOutput(true);
					BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream());Throwable localThrowable2 = null;
					try
					{
						bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
						bufOut.flush();
						bufOut.close();
					}
					catch (Throwable localThrowable1)
					{
						localThrowable2 = localThrowable1;throw localThrowable1;
					}
					finally
					{
						if (bufOut != null) {
							if (localThrowable2 != null) {
								try
								{
									bufOut.close();
								}
								catch (Throwable x2)
								{
									localThrowable2.addSuppressed(x2);
								}
							} else {
								bufOut.close();
							}
						}
					}
					webhookConnection.getResponseCode();
					webhookConnection.disconnect();
					webhookConnection = null;
				}
				catch (Exception ignored) {
					//ignored.printStackTrace();
				}
			}
		}.start();
	}
}