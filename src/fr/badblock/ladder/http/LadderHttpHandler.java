package fr.badblock.ladder.http;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.http.players.PageExist;
import fr.badblock.ladder.http.players.PageGetData;
import fr.badblock.ladder.http.players.PageIsConnected;
import fr.badblock.ladder.http.players.PageSendMessage;

public class LadderHttpHandler extends AbstractHandler {
	
	private Map<String, LadderPage> pages = Maps.newConcurrentMap();
	private Gson gson = new Gson();

	public LadderHttpHandler(int port) {
		AbstractHandler abstractHandler = this;
		new Thread() {
			@Override
			public void run() {
				Server server = new Server(port);
				server.setHandler(abstractHandler);
				try {
					server.start();
					server.join();
				} catch (Exception e) {
					Ladder.getInstance().getConsoleCommandSender().sendMessage("§b[LadderHTTP] §cUnable to create HttpHandler (port " + port + "). See the stack trace:");
					e.printStackTrace();
					return;
				}
				addHandler(new PageGetData());
				addHandler(new PageIsConnected());
				addHandler(new PageSendMessage());
				addHandler(new PageExist());
			}
		}.start();

	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (target.equals("/favicon.ico")) return;
		if(pages.containsKey(target)){
			JsonObject object =  gson.fromJson(baseRequest.getReader(), JsonObject.class);

			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_ACCEPTED);

			response.getWriter().println(pages.get(target).call(object));
			baseRequest.setHandled(true);
		} else {
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.getWriter().println("403 Not allowed");

			baseRequest.setHandled(true);
		}
	}

	public void addHandler(LadderPage page){
		if(!pages.containsKey(page.getPath()))
			pages.put(page.getPath(), page);
	}

}