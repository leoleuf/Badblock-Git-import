package fr.badblock.bungeecord.plugins.others.listeners;

import java.sql.ResultSet;

import com.google.gson.Gson;

import fr.badblock.bungeecord.plugins.others.database.BadblockDatabase;
import fr.badblock.bungeecord.plugins.others.database.Request;
import fr.badblock.bungeecord.plugins.others.database.Request.RequestType;
import fr.badblock.bungeecord.plugins.others.utils.IPHubObject;
import fr.badblock.bungeecord.plugins.others.utils.NetworkUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.AsyncDataLoadRequest;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AsyncDataLoadRequestListener implements Listener
{

	private Gson gson = new Gson();

	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(AsyncDataLoadRequest event)
	{
		String ip = event.getHandler().getAddress().getAddress().getHostAddress();
		BadblockDatabase.getInstance().addSyncRequest(new Request("SELECT COUNT(id) AS count FROM blockedIps WHERE ip = '" + ip + "'", RequestType.GETTER)
		{
			@Override
			public void done(ResultSet resultSet)
			{
				try
				{
					if (resultSet.next())
					{
						int count = resultSet.getInt("count");
						if (count > 0)
						{
							event.getDone().done(new Result(null, ChatColor.RED + "Votre IP a été bloqué par notre système automatisé pour cause de mauvaise réputation."), null);
							event.setCancelled(true);
							return;
						}
					}
					if (!isGoodIP(ip))
					{
						BadblockDatabase.getInstance().addSyncRequest(new Request("INSERT INTO blockedIps(ip) VALUES('" + ip + "')", RequestType.SETTER));
						event.getDone().done(new Result(null, ChatColor.RED + "Votre IP a été bloqué par notre système automatisé pour cause de mauvaise réputation."), null);
						event.setCancelled(true);
					}
				}
				catch(Exception error)
				{
					error.printStackTrace();
				}
			}
		});
	}

	private boolean isGoodIP(String ip)
	{
		String urlBuilder = "http://v2.api.iphub.info/ip/" + ip;
		String apiKey = "MTU4NTpTMXV2TXdZbFJ0YlVZOGV2aGo3dUV0dG4zOVprTWVldQ==";
		String sourceCode = NetworkUtils.fetchSourceCodeWithAPI(urlBuilder, apiKey);
		try
		{
			IPHubObject object = gson.fromJson(sourceCode, IPHubObject.class);
			if (object == null)
			{
				return true;
			}
			return object.getBlock() == 0;
		}catch(Exception error)
		{
			System.out.println("Error (GOODIP-CHECK) : " + sourceCode);
			return true;
		}
	}

}
