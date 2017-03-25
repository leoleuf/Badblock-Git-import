package fr.badblock.ladder.api.utils;

import com.google.gson.JsonObject;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
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

	private int banId;
	private int muteId;
	
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

	public String buildBanTime(){
		if(banEnd != -1){
			return Time.MILLIS_SECOND.toFrench(banEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
		} else return "à vie";
	}
	
	public String buildMuteTime(){
		if(muteEnd != -1){
			return Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
		} else return "à vie";
	}
	
	public String buildBanReason() {
		String time = "Permanent";
	    if (banEnd != -1L) time = Time.MILLIS_SECOND.toFrench(banEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
	    return "Vous êtes banni de ce serveur ! (Temps: " + time + "§r | Motif: " + banReason.replace("§", "&") + "§r)";
	}
	
	public String buildMuteReason(){
		String time = "";
		if(muteEnd != -1){
			time = " pour " + Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.DAY);
		}
		return ChatColor.RED + "Vous êtes mute (" + muteReason + ")" + time + ".";
	}
	
	public void save(JsonObject object){
		JsonObject punish = Ladder.getInstance().getGson().toJsonTree(this).getAsJsonObject();
		object.add("punish", punish);
	}
	
	public static Punished fromJson(JsonObject object){
		if(object.has("punish"))
			return Ladder.getInstance().getGson().fromJson(object.get("punish").getAsJsonObject(), Punished.class);
		else return new Punished();
	}
}
