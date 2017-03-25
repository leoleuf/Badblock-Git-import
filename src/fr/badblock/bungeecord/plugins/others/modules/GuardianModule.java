package fr.badblock.bungeecord.plugins.others.modules;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ChatColor;
import fr.badblock.bungeecord.api.ProxyServer;
import fr.badblock.bungeecord.api.chat.BaseComponent;
import fr.badblock.bungeecord.api.chat.ClickEvent;
import fr.badblock.bungeecord.api.chat.HoverEvent;
import fr.badblock.bungeecord.api.chat.TextComponent;
import fr.badblock.bungeecord.api.chat.HoverEvent.Action;
import fr.badblock.bungeecord.api.connection.ProxiedPlayer;
import fr.badblock.bungeecord.api.connection.Server;
import fr.badblock.bungeecord.api.event.PluginMessageEvent;
import fr.badblock.bungeecord.api.plugin.Listener;
import fr.badblock.bungeecord.event.EventHandler;
import fr.badblock.bungeecord.plugins.others.BadBlockBungeeOthers;
import fr.badblock.bungeecord.plugins.others.commands.guardian.GReportsCommand;
import fr.badblock.bungeecord.plugins.others.guardian.objects.GuardianKick;
import fr.badblock.bungeecord.plugins.others.guardian.objects.GuardianReport;
import fr.badblock.bungeecord.plugins.others.modules.abstracts.Module;

public class GuardianModule extends Module implements Listener {
	
	private ProxyServer proxy = BungeeCord.getInstance();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) throws IOException{
		if (!(e.getSender() instanceof Server)) return;
		if (e.getTag().equals("GuardianBroad")) {
			ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
			DataInputStream in = new DataInputStream(stream);
			proxy.broadcast(in.readUTF());
			in.close();
			stream.close();
		}else if (e.getTag().equals("GuardianKick")) {
			ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
			DataInputStream in = new DataInputStream(stream);
			String json = in.readUTF();
			GuardianKick kick = new Gson().fromJson(json, GuardianKick.class);
			ProxiedPlayer proxiedPlayer = proxy.getPlayer(kick.getUniqueId());
			if (proxiedPlayer != null) {
				proxiedPlayer.disconnect(kick.getMessage());
			}
			in.close();
			stream.close();
		}else if (e.getTag().equals("GuardianBan")) {
			ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
			DataInputStream in = new DataInputStream(stream);
			String json = in.readUTF();
			GuardianKick kick = new Gson().fromJson(json, GuardianKick.class);
			ProxiedPlayer proxiedPlayer = proxy.getPlayer(kick.getUniqueId());
			if (proxiedPlayer != null) {
				BadBlockBungeeOthers.getInstance().getProxy().getPluginManager().dispatchCommand(BadBlockBungeeOthers.getInstance().getProxy().getConsole(), "tempban " + proxiedPlayer.getName() + " 2d Cheat §b(banni par Guardian)§c");
				BadBlockBungeeOthers.getInstance().getProxy().getPluginManager().dispatchCommand(BadBlockBungeeOthers.getInstance().getProxy().getConsole(), "tempbanip " + proxiedPlayer.getName() + " 2d Cheat §b(banni par Guardian)§c");
				proxiedPlayer.disconnect(kick.getMessage());
			}
			in.close();
			stream.close();
		}else if (e.getTag().equals("GuardianReport")) {
			ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
			DataInputStream in = new DataInputStream(stream);
			String json = in.readUTF();
			GuardianReport guardianReport = new Gson().fromJson(json, GuardianReport.class);
			String server = "Inconnu";
			ProxiedPlayer proxiedPlayer = proxy.getPlayer(guardianReport.getUniqueId());
			if (proxiedPlayer != null) server = proxiedPlayer.getServer().getInfo().getName();
			TextComponent component = getMessage(guardianReport.getMessage().replace("[SERVER]", "[" + server + "]"), "Cliquez pour vous téléporter au joueur");
			component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/track " + proxiedPlayer.getName()));
			proxy.getConsole().sendMessage(component);
			BungeeCord.getInstance().getPlayers().parallelStream().filter(player -> player.hasPermission("guardian.modo")).filter(player -> !GReportsCommand.exclusions.contains(player.getUniqueId())).forEach(player -> {
				player.sendMessage(component);
			});
			in.close();
			stream.close();
		}
	}
	
	public TextComponent getMessage(String message, String lore) {
		// Envoi du message au joueur
		TextComponent textComponent = new TextComponent(message);
		// Ajout du lore
		if (!"".equals(lore)) {
			String[] lines = lore.split("\n");
			List<BaseComponent> components = new ArrayList<>();
			int i = 0;
			for (String line : lines) {
				i++;
				String addon = i != lines.length ? "\n" : "";
				components.add(new TextComponent(ChatColor.translateAlternateColorCodes('&', line) + addon));
			}
			BaseComponent[] arr = (BaseComponent[]) components.toArray(new BaseComponent[components.size()]);
			textComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, arr));
		}
		return textComponent;
	}
	
}
