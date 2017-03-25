package fr.badblock.ladder.bungee.utils;

import com.google.gson.JsonObject;

import fr.badblock.bungeecord.BungeeCord;
import fr.badblock.bungeecord.api.ChatColor;
import lombok.Data;

@Data public class Punished {
	private boolean ban,
					mute;
	
	private long	banEnd,
					muteEnd;
	
	private String	banReason,
					muteReason;
	
	private String	banner,
					muter;
	
	public Punished(){
		ban 	   = false;
		mute 	   = false;
		banEnd     = -1;
		muteEnd    = -1;
		banReason  = null;
		muteReason = null;
		banner     = null;
		muter	   = null;
	}
	
	public void checkEnd(){
		if(ban && banEnd != -1 && banEnd < System.currentTimeMillis()) {
			ban 	  = false;
			banEnd 	  = -1;
			banReason = null;
			banner 	  = null;
		}
		
		if(mute && muteEnd != -1 && muteEnd < System.currentTimeMillis()){
			mute 	   = false;
			muteEnd    = -1;
			muteReason = null;
			muter 	   = null;
		}
	}
	
	public String buildBanReason(){
		String time = "";
		if(banEnd != -1){
			time = " pour " + Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
		}
		return ChatColor.RED + "Vous avez été banni de BadBlock : " + ChatColor.WHITE + banReason + time;
	}
	
	public String buildMuteReason(){
		String time = "";
		if(muteEnd != -1){
			time = " pour " + Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
		}
		return ChatColor.RED + "Vous êtes mute (" + muteReason + ")" + time + ".";
	}
	
	public void save(JsonObject object){
		JsonObject punish = BungeeCord.getInstance().gson.toJsonTree(this).getAsJsonObject();
		object.add("punish", punish);
	}
	
	public static Punished fromJson(JsonObject object){
		if(object.has("punish"))
			return BungeeCord.getInstance().gson.fromJson(object.get("punish").getAsJsonObject(), Punished.class);
		else return new Punished();
	}
}
