package fr.badblock.api.runnables;

import fr.badblock.api.MJPlugin;
import fr.badblock.api.MJPlugin.GameStatus;
import fr.badblock.api.game.BPlayersManager;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;
import lombok.Getter;

public class SignsRunnable extends BRunnable {
	@Getter private static SignsRunnable instance;
	
	public SignsRunnable() {
		super(240 * 20L);
		instance = this;
	}

	@Override
	public void run(){
		GameStatus status = MJPlugin.getInstance().getStatus();
		ServerStatus result = null;
		
		if(status == GameStatus.WAITING_PLAYERS && MJPlugin.getInstance().maxPlayer() == BPlayersManager.getInstance().getPlayers().size())
			result = ServerStatus.RUNNING;
		else if(status == GameStatus.WAITING_PLAYERS)
			result = ServerStatus.WAITING;
		else if(status == GameStatus.ENDING)
			result = ServerStatus.FINISHED;
		
		sendStatus(result);
	}
	
	public void sendStatus(ServerStatus status){
		MJPlugin.getSpeaker().sendKeepalive(status, BPlayersManager.getInstance().getPlayers().size(), MJPlugin.getInstance().maxPlayer());
	}
}
