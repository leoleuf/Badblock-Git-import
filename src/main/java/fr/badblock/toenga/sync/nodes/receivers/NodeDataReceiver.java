package fr.badblock.toenga.sync.nodes.receivers;

import fr.badblock.api.common.nodesync.NodeData;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.ToengaQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.toenga.sync.nodes.NodeSyncManager;

public class NodeDataReceiver extends RabbitListener
{

	public NodeDataReceiver(RabbitService rabbitService)
	{
		super(rabbitService, ToengaQueues.NODE_SYNC, RabbitListenerType.SUBSCRIBER, true);
	}

	@Override
	public void onPacketReceiving(String body)
	{
		if (body == null)
		{
			return;
		}
		
		NodeData nodeData = GsonUtils.getGson().fromJson(body, NodeData.class);
		
		if (nodeData == null)
		{
			return;
		}
		
		NodeSyncManager.getInstance().put(nodeData);
	}
	
}
