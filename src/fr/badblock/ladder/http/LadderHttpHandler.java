package fr.badblock.ladder.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.ladder.http.players.PagePlayerAddGroup;
import fr.badblock.ladder.http.players.PagePlayerExists;
import fr.badblock.ladder.http.players.PagePlayerGetConnectedServer;
import fr.badblock.ladder.http.players.PagePlayerGetData;
import fr.badblock.ladder.http.players.PagePlayerIsConnected;
import fr.badblock.ladder.http.players.PagePlayerSendMessage;
import fr.badblock.ladder.http.players.PagePlayerSendServerMessage;
import fr.badblock.ladder.http.players.PagePlayerUpdateData;
import fr.badblock.ladder.http.players.PagePlayerUpdateShopPoints;
import fr.badblock.ladder.http.players.PageServerBroadcast;

public class LadderHttpHandler extends AbstractHandler
{

	private Map<String, LadderPage> pages = Maps.newConcurrentMap();
	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public LadderHttpHandler(int port)
	{
		AbstractHandler abstractHandler = this;
		new Thread() {
			@Override
			public void run()
			{
				Server server = new Server(port);
				server.setHandler(abstractHandler);
				try
				{
					server.start();
					server.join();
				}
				catch (Exception e)
				{
					Ladder.getInstance().getConsoleCommandSender().sendMessage("�b[LadderHTTP] �cUnable to create HttpHandler (port " + port + "). See the stack trace:");
					e.printStackTrace();
					return;
				}
			}
		}.start();
		addHandler(new PagePlayerAddGroup());
		addHandler(new PagePlayerUpdateShopPoints());
		addHandler(new PagePlayerUpdateData());
		addHandler(new PagePlayerGetData());
		addHandler(new PagePlayerGetConnectedServer());
		addHandler(new PagePlayerIsConnected());
		addHandler(new PagePlayerSendMessage());
		addHandler(new PagePlayerSendServerMessage());
		addHandler(new PageServerBroadcast());
		addHandler(new PagePlayerExists());
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		if (target.equals("/favicon.ico")) return;
		if (pages.containsKey(target))
		{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);

			baseRequest.setHandled(true);
			
			BufferedReader bufferedReader = request.getReader();
			String line = "";
			Map<String, String> maps = new HashMap<>();
			while ((line = bufferedReader.readLine()) != null) {
				String[] splitterO = line.split("&");
				for (String o : splitterO) {
					String[] splitter = o.split("=");
					if (splitter.length < 2) {
						System.out.println(target + " => Invalid JSON: " + o);
						continue;
					}
					String data = StringUtils.join(o.split("="), "=", 1);
					maps.put(o.split("=")[0], new String(data.getBytes("ISO-8859-1"), "UTF-8"));
				}
			}

			response.getWriter().println(pages.get(target).call(maps));
		}
		else
		{
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.getWriter().println("403 Not allowed");

			baseRequest.setHandled(true);
		}
	}

	public void addHandler(LadderPage page)
	{
		if(!pages.containsKey(page.getPath()))
		{
			pages.put(page.getPath(), page);
		}
	}

}