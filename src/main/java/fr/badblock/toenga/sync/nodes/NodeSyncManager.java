package fr.badblock.toenga.sync.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.badblock.api.common.nodesync.NodeData;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.sync.nodes.receivers.NodeDataReceiver;
import lombok.Data;
import lombok.Getter;

@Data
public class NodeSyncManager
{

	@Getter private static NodeSyncManager instance;

	private	Map<String, NodeData>	nodes		= new HashMap<>();

	public NodeSyncManager()
	{
		instance = this;
		RabbitService rabbitService = Toenga.instance.getRabbitService();
		rabbitService.addListener(new NodeDataReceiver(rabbitService));
	}
	
	public Set<NodeData> getAvailableNodes()
	{
		return nodes.values().stream().filter(nodeData -> nodeData.isValid()).collect(Collectors.toSet());
	}
	
	public void put(NodeData nodeData)
	{
		nodes.put(nodeData.getNodeIdentifier().getFullIdentifier(), nodeData);
	}

}
