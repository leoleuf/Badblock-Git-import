package fr.badblock.ladder.data;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import fr.badblock.ladder.Proxy;
import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.api.entities.PlayerIp;
import fr.badblock.ladder.api.utils.Punished;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import lombok.Getter;
import lombok.Setter;

public class LadderIpDataHandler extends LadderDataHandler implements PlayerIp {
	@Getter private final InetAddress address;
	private final Punished	  		  punished;
	
	@Getter@Setter
	private String					  lastUser;
	@Getter
	private List<UUID>				  players;
	
	public LadderIpDataHandler(InetAddress address) {
		super(Proxy.IPS_FOLDER, address.getHostAddress());

		this.address   = address;
		this.punished  = Punished.fromJson(getData());
		
		this.players   = new ArrayList<>();
	}	

	@Override
	public Punished getAsPunished() {
		return punished;
	}

	@Override
	public String getIp() {
		return address.getHostAddress();
	}

	@Override
	public void savePunishions() {
		punished.save(getData());
	}

	@Override
	public Collection<UUID> getCurrentPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	@Override
	public void sendToServers() {
		for(UUID uniqueId : getCurrentPlayers()){
			Player player = Ladder.getInstance().getPlayer(uniqueId);
			if(player != null)
				player.getBungeeServer().sendPacket(new PacketPlayerData(DataType.IP, DataAction.SEND, getIp(), getData().toString()));
		}
	}
}
