package fr.badblock.toenga.sync.toenga;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fr.badblock.api.common.tech.rabbitmq.ToengaQueues;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.sync.toenga.receivers.ToengaNodeDataReceiver;
import lombok.Data;
import lombok.Getter;

@Data
public class ToengaSyncManager
{

	@Getter private static ToengaSyncManager instance;

	private	Map<String, ToengaNode> nodes		= new HashMap<>();
	private ToengaLocalSender		localSender = null;
	private ToengaNode				localNode   = null;

	public ToengaSyncManager(Toenga toenga)
	{
		instance = this;
		toenga.getRabbitService().addListener(new ToengaNodeDataReceiver(toenga));
		setLocalSender(new ToengaLocalSender(toenga));
		getLocalSender().start();
	}

	public boolean isInSameCluster(ToengaNode toengaNode)
	{
		return !Collections.disjoint(toengaNode.getClusters(), Toenga.instance.getStaticConfiguration().getTypes());
	}
	
	public boolean isMaster()
	{
		Optional<String> optional = getAvailableSameClusterNodes().parallelStream().map(node -> node.getName()).sorted().findFirst();
		return optional.isPresent() ? optional.get().equalsIgnoreCase(Toenga.instance.getHostname()) : false;
	}

	public Set<ToengaNode> getAvailableSameClusterNodes()
	{
		nodes.values().stream().forEach(toengaNode -> System.out.println(toengaNode.isValid() + " / " + isInSameCluster(toengaNode)));
		return nodes.values().stream().filter(toengaNode -> toengaNode.isValid() && isInSameCluster(toengaNode)).collect(Collectors.toSet());
	}
	
	public Set<ToengaNode> getAvailableNodes()
	{
		return nodes.values().stream().filter(toengaNode -> toengaNode.isValid()).collect(Collectors.toSet());
	}

	public Set<ToengaNode> getAvailableNodes(String cluster)
	{
		return nodes.values().stream().filter(toengaNode -> toengaNode.isValid() && toengaNode.getClusters().contains(cluster)).collect(Collectors.toSet());
	}

	public ToengaNode generateLocalData()
	{
		long keepAliveTime = Toenga.instance.getConfiguration().getKeepAliveTime() * 2;
		if (localNode == null)
		{
			localNode = new ToengaNode(Toenga.instance.getHostname(), Toenga.instance.getStaticConfiguration().getTypes(), keepAliveTime);
		}
		else
		{
			localNode.update(keepAliveTime);
		}
		return localNode;
	}

	public void keepAlive()
	{
		System.out.println("Master: " + isMaster());
		ToengaNode toengaNode = generateLocalData();
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(-1, toengaNode.toJson());
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, ToengaQueues.TOENGA_SYNC, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		Toenga.instance.getRabbitService().sendPacket(rabbitPacket);
	}

	public void put(ToengaNode toengaNode)
	{
		nodes.put(toengaNode.getName(), toengaNode);
	}

}
