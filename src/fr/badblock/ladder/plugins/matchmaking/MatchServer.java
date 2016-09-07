package fr.badblock.ladder.plugins.matchmaking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import com.google.common.collect.Lists;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.ChatColor;
import fr.badblock.ladder.api.entities.Bukkit;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.events.EventHandler;
import fr.badblock.ladder.api.events.Listener;
import fr.badblock.ladder.api.events.all.MatchmakingJoinEvent;
import fr.badblock.ladder.api.events.all.MatchmakingServerEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class MatchServer extends Thread implements Listener {
	@Getter private String 			serversName;
	private int 					maxPlayers;

	private List<UUID> 				joiningEntities;
	private Queue<String> 			availableServers;

	private Queue<UUID>				wantToJoin;

	private boolean					run = false;

	public MatchServer(String serverName, int maxPlayers){
		this.serversName 	  = serverName;
		this.maxPlayers 	  = maxPlayers;

		this.joiningEntities  = Lists.newArrayList();
		this.wantToJoin		  = new LinkedList<>();
		this.availableServers = new LinkedList<>();
	}

	public void end(){
		run = false;
	}

	public int knowServers(){
		return availableServers.size();
	}

	public int knowPlayers(){
		return wantToJoin.size();
	}

	public boolean isKnow(Bukkit server){
		return availableServers.contains(server.getName().toLowerCase());
	}

	protected Reponse fillServer(Bukkit server, Reponse previous){
		if(server == null)
			return new Reponse(ReponseType.GOOD, null, 0);

		int firstSize     = server.getPlayers().size();
		List<UUID> tp     = new ArrayList<>();

		if(previous.getReponse() == ReponseType.WARN){
			firstSize = previous.getWhenWarningCount();
			tp		  = previous.getWhenWarning();
		}

		int waitedPlayers = maxPlayers - firstSize;


		for(int i=0; i < joiningEntities.size(); i++){
			UUID entity = joiningEntities.get(i);
			Player player = Ladder.getInstance().getPlayer(entity);

			if(player == null || player.getRequestedGame() == null || !player.getRequestedGame().startsWith(serversName)){
				joiningEntities.remove(i); i--; continue;
			} else if(player.getPlayersWithHim() != null && player.getPlayersWithHim().size() + 1 > waitedPlayers){
				continue;
			} else {
				joiningEntities.remove(i); i--;
				player.sendMessage(ChatColor.GREEN + "Téléportation dans une partie !");
				player.connect(server);

				tp.add(entity);

				if(player.getPlayersWithHim() != null) {
					for(UUID uniqueId : player.getPlayersWithHim()) {
						Player p = Ladder.getInstance().getPlayer(uniqueId);
						if(p != null) {
							p.canJoinHimself(true);
							p.connect(server);
							p.canJoinHimself(false);

							waitedPlayers--;
						}
					}
				}
			}

			waitedPlayers--;
			if(waitedPlayers == 0){
				break;
			}
		}

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException unused){}

		if(firstSize == server.getPlayers().size() && tp.size() != 0) {
			if(previous.getReponse() == ReponseType.WARN){
				System.out.println("Ce serveur semble posé problème : " + server.getName());
				for(UUID entity : tp)
					joiningEntities.add(0, entity);
				
				return new Reponse(ReponseType.GOOD, null, 0);
			} else return new Reponse(ReponseType.WARN, tp, firstSize);
		} else if(server.getPlayers().size() == waitedPlayers){
			System.out.println(server.getName() + " filled");
			return new Reponse(ReponseType.GOOD, null, 0);
		} else {
			return new Reponse(ReponseType.WAIT, null, 0);
		}
	}

	@EventHandler
	public void onServerKeepalive(MatchmakingServerEvent e){
		/*String name = e.getBukkit().getName().toLowerCase();

		if(!name.startsWith(serversName))
			return;

		if(e.isAlive() && !availableServers.contains(name)){ // waiting for players
			availableServers.add(name);
		} else if(!e.isAlive() && availableServers.contains(name)){
			availableServers.remove(name);
		}*/
	}

	@EventHandler
	public void onPlayerJoinMatchmaking(MatchmakingJoinEvent e){
		if(!e.getServer().startsWith(serversName))
			return;

		Player player = e.getPlayer();

		if(!player.canJoinHimself()){
			player.sendMessage(ChatColor.RED + "Vous êtes actuellement dans une partie dont vous n'êtes pas le chef. Impossible de join.");
		} else if(wantToJoin.contains(player.getUniqueId()) || joiningEntities.contains(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "Votre demande a déjà été prise en compte, veuillez patienter.");
		} else if(player.getPlayersWithHim().size() + 1 > maxPlayers){
			player.sendMessage(ChatColor.RED + "Vous êtes trop nombreux dans votre partie ! Ce jeu n'accepte que " + maxPlayers + " au maximum !");
		} else {
			player.sendMessage(ChatColor.GREEN + "Recherche d'une partie en cours ...");
			player.setRequestedGame(serversName);

			wantToJoin.add(player.getUniqueId());
		}
	}

	@Override
	public void run(){
		run = true;
		while(run){
			Reponse previous = new Reponse(ReponseType.GOOD, null, 0);
			
			try {
				String server = availableServers.peek();

				if(server != null){
					previous = fillServer(Ladder.getInstance().getBukkitServer(server), previous);

					if(previous.getReponse() == ReponseType.GOOD && !availableServers.isEmpty()){
						availableServers.remove();
					}
				}

				UUID entity = null;
				while((entity = wantToJoin.poll()) != null){
					joiningEntities.add(entity);
				}

				Thread.sleep(50L);
			} catch (Exception unused){}
		}

		System.out.println("Moi je run plus #" + serversName);
	}

	public enum ReponseType {
		WAIT,
		WARN,
		GOOD;
	}

	@AllArgsConstructor@Data
	public class Reponse {
		private ReponseType reponse;
		private List<UUID>  whenWarning;
		private int			whenWarningCount;
	}
}