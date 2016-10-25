package fr.badblock.ladder.bungee;

import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.ladder.bungee.utils.Punished;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerChat.ChatAction;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.AsyncDataLoadRequest.Result;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Title;
import net.md_5.bungee.protocol.packet.Title.Action;

@Getter@Setter(value=AccessLevel.PACKAGE) public class Player {
	private InetSocketAddress   address;
	private       UUID			      uniqueId;
	private  	  String			  name;
	private  	  String			  nickName;
	private 	  ServerInfo		  server;
	private 	  JsonObject		  readOnlyData;
	private		  PermissiblePlayer	  permissions;
	private		  Punished			  punished;

	private		  Callback<Result>	  done;
	
	public Player(PacketPlayerJoin e){
		this.readOnlyData = new JsonObject();
		this.address      = e.getAddress();
		this.uniqueId     = e.getUniqueId();
		this.name         = e.getPlayerName();
		this.nickName     = e.getNickName().isEmpty() ? e.getPlayerName() : e.getNickName();
		this.server       = null;
		
		punished          = new Punished();
	}
	
	public Player(PacketPlayerLogin e, Callback<Result> done){
		this.readOnlyData = new JsonObject();
		this.address      = e.getAddress();
		this.name         = e.getPlayerName();
		this.nickName	  = e.getPlayerName();
		this.server       = null;
		
		this.done = done;
	}

	public void sendPacket(Packet packet){
		LadderBungee.getInstance().getClient().sendPacket(packet);
	}

	private void addObjectInObject(JsonObject base, JsonObject toAdd){
		for(final Entry<String, JsonElement> entry : toAdd.entrySet()){
			String key = entry.getKey();
			JsonElement element = toAdd.get(key);

			if(!base.has(key) || !element.isJsonObject()){
				base.add(key, element);
			} else {
				addObjectInObject(base.get(key).getAsJsonObject(), element.getAsJsonObject());
			}
		}
	}

	public void update(PacketPlayerJoin e) {
		this.address      = e.getAddress();
		this.uniqueId     = e.getUniqueId();
		this.name         = e.getPlayerName();
		this.nickName     = e.getNickName().isEmpty() ? e.getPlayerName() : e.getNickName();
	}
	
	protected void updateData(String result){
		JsonObject object = new JsonParser().parse(result).getAsJsonObject();
		addObjectInObject(readOnlyData, object);

		//System.out.println("Creating PermissiblePlayer of the current entity " + name + " (" + readOnlyData + " - updateData(String))");
		permissions  = LadderBungee.getInstance().getPermissions().createPlayer(name, readOnlyData);
		punished	 = Punished.fromJson(readOnlyData);
	}

	protected void receiveData(String result){
		JsonObject object = new JsonParser().parse(result).getAsJsonObject();

		if(done != null){
			done.done(new Result(object, null), null);
			done = null;
		}
		
		if(object.has("uniqueId"))
			uniqueId = UUID.fromString(object.get("uniqueId").getAsString());

		//System.out.println("Creating PermissiblePlayer of the current entity " + name + " (" + readOnlyData + " - receiveData(String))");
		this.permissions  = LadderBungee.getInstance().getPermissions().createPlayer(name, object);
		this.punished	  = Punished.fromJson(object);
		this.readOnlyData = object;
	}

	@SuppressWarnings("deprecation")
	protected void handle(PacketPlayerChat chat){
		ProxiedPlayer player = BungeeCord.getInstance().getPlayer(uniqueId);
		if(player == null) return;

		if(chat.getType() == ChatAction.MESSAGE_FLAT){
			player.sendMessages(chat.getMessages());
		} else if(chat.getType() == ChatAction.MESSAGE_JSON){
			try {
				BaseComponent[] message = ComponentSerializer.parse(chat.getMessages()[0].toString());
				player.sendMessage(message);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else if(chat.getType() == ChatAction.ACTION_BAR){
			Title title = new Title();
			title.setAction(Action.TIMES);
			title.setFadeIn(chat.getFadeIn());
			title.setStay(chat.getStay());
			title.setFadeOut(chat.getFadeOut());

			player.unsafe().sendPacket(title);
			player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(chat.getMessages()[0]));
		} else if(chat.getType() == ChatAction.COMMAND){
			for(String command : chat.getMessages())
				BungeeCord.getInstance().getPluginManager().dispatchCommand(player, command);
		} else if(chat.getType() == ChatAction.TABLIST){
			player.setTabHeader(TextComponent.fromLegacyText(chat.getMessages()[0]), TextComponent.fromLegacyText(chat.getMessages()[1]));
		} else if(chat.getType() == ChatAction.TITLE){
			player.sendTitle(BungeeCord.getInstance().createTitle().fadeIn(chat.getFadeIn())
					.fadeOut(chat.getFadeOut())
					.stay(chat.getStay())
					.title(TextComponent.fromLegacyText(chat.getMessages()[0]))
					.subTitle(TextComponent.fromLegacyText(chat.getMessages()[1])));
		}
	}
}	
