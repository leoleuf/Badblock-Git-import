package fr.badblock.api.utils.bukkit.title;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.badblock.api.utils.bukkit.ChatUtils;

public class Title {
	private String title, subTitle;
	private int stay, fadeIn, fadeOut;
	private boolean only18;
	
	public Title(String title){
		this(title, 0);
	}
	public Title(String title, boolean only18){
		this(title, 0);
		this.only18 = only18;
	}
	public Title(String title, int stay){
		this(title, null, stay);
	}
	public Title(String title, int stay, int fadeIn, int fadeOut){
		this(title, null, stay, fadeIn, fadeOut);
	}
	public Title(String title, String subTitle){
		this(title, subTitle, 0);
	}
	public Title(String title, String subTitle, int stay){
		this(title, subTitle, stay, 0, 0);
	}
	public Title(String title, String subTitle, int stay, int fadeIn, int fadeOut){
		this(title, subTitle, stay, fadeIn, fadeOut, false);
	}
	public Title(String title, String subTitle, int stay, int fadeIn, int fadeOut, boolean only18){
		setTitle(title);
		setSubTitle(subTitle);
		setStay(stay);
		setFadeIn(fadeIn);
		setFadeOut(fadeOut);
		this.only18 = only18;
	}
	
	public boolean isOnly18() {
		return only18;
	}
	public void setOnly18(boolean only18) {
		this.only18 = only18;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		if(title != null && title.isEmpty())
			this.title = null;
		else this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		if(subTitle != null && subTitle.isEmpty())
			this.subTitle = null;
		else this.subTitle = subTitle;
	}
	public int getStay() {
		return stay;
	}
	public void setStay(int stay) {
		this.stay = stay <= 0 ? 40 : stay;
	}
	public int getFadeIn() {
		return fadeIn;
	}
	public void setFadeIn(int fadeIn) {
		this.fadeIn = fadeIn <= 0 ? 40 : fadeIn;
	}
	public int getFadeOut() {
		return fadeOut;
	}
	public void setFadeOut(int fadeOut) {
		this.fadeOut = fadeOut <= 0 ? 40 : fadeOut;
	}
	
	public void send(Player p){
		if(fadeIn >= stay) fadeIn = 0;
		if(fadeOut >= stay) fadeOut = 0;
		
		TitleManager.clear(p);
		
		if(title != null){
			title = ChatUtils.colorReplace(title);
			TitleManager.sendTitle(p, fadeIn, stay, fadeOut, title, only18);
		}
		if(subTitle != null){
			subTitle = ChatUtils.colorReplace(subTitle);
			if(title == null){
				TitleManager.sendTitle(p, fadeIn, stay, fadeOut, "&4yo", only18);
			}
			TitleManager.sendSubTitle(p, fadeIn, stay, fadeOut, subTitle, only18);
		}
	}
	public void broadcast(Player... players){
		for(final Player player : players)
			send(player);
	}
	public void broadcast(Collection<? extends Player> players){
		for(final Player player : players){
			send(player);
		}
	}

	public void broadcast(){
		broadcast(Bukkit.getOnlinePlayers());
	}
	
}
