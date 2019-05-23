package fr.badblock.game.core112R1.i18n;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import fr.badblock.gameapi.utils.i18n.Message;
import lombok.Getter;
import lombok.Setter;

public class GameMessage implements Message {
	public transient File file;
	
	private boolean useHeader,
					useShortHeader,
					useFooter;
	
	private String[][] messages;
	
	@Getter @Setter private boolean unknown;
	
	public GameMessage(){}
	
	public GameMessage(String whenUnknow){
		this.useHeader 		= false;
		this.useShortHeader = false;
		this.useFooter		= false;
		
		verify(whenUnknow);
	}
	
	public void verify(String whenUnknow){
		if(messages == null || messages.length == 0){
			unknown = true;
			messages = new String[][]{new String[]{whenUnknow}};
		}
	}
	
	@Override
	public boolean useHeader() {
		return useHeader;
	}

	@Override
	public boolean useShortHeader() {
		return useShortHeader;
	}

	@Override
	public boolean useFooter() {
		return useFooter;
	}

	@Override
	public boolean isRandomMessage() {
		return messages.length > 1;
	}

	@Override
	public String[] getUnformattedMessage() {
		String[] message = messages[new Random().nextInt(messages.length)];
		return Arrays.copyOf(message, message.length);
	}

	@Override
	public String[][] getAllMessages() {
		return messages;
	}
}
