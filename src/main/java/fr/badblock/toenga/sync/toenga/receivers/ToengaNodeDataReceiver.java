package fr.badblock.toenga.sync.toenga.receivers;

import fr.badblock.api.common.tech.rabbitmq.ToengaQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.sync.toenga.ToengaNode;
import fr.badblock.toenga.sync.toenga.ToengaSyncManager;

public class ToengaNodeDataReceiver extends RabbitListener
{

	public ToengaNodeDataReceiver()
	{
		super(Toenga.instance.getRabbitService(), ToengaQueues.TOENGA_SYNC, RabbitListenerType.SUBSCRIBER, true);
	}

	@Override
	public void onPacketReceiving(String body)
	{
		if (body == null)
		{
			return;
		}
		
		ToengaNode toengaNode = GsonUtils.getGson().fromJson(body, ToengaNode.class);
		
		if (toengaNode == null)
		{
			return;
		}
		
		ToengaSyncManager.getInstance().put(toengaNode);
	}	
	
}
