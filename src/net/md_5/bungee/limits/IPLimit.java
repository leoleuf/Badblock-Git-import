package net.md_5.bungee.limits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Queues;

public class IPLimit
{

	private static final long MAX_HITS	= 30;
	private static final long TIME		= 30_000;
	private static final long TIME_AFH	= 30_000;

	private static Map<String, Queue<Long>>	maps		=	new HashMap<>();
	private static List<String>				toBeBlocked =	new ArrayList<>();

	public static IPStatus incrementAndGetStatus(String ip)
	{
		long time = System.currentTimeMillis();
		Queue<Long> queue = maps.containsKey(ip) ? maps.get(ip) : Queues.newConcurrentLinkedQueue();
		IPStatus result = IPStatus.ALLOWED;
		if (queue.size() >= MAX_HITS)
		{
			long pollTime = queue.poll();
			double difference = time - pollTime;
			if (difference <= TIME)
			{
				result = IPStatus.HIT;
			}
		}
		queue.add(time);
		maps.put(ip, queue);
		return result;
	}

	public static void block(String ip)
	{
		if (toBeBlocked.contains(ip))
		{
			return;
		}
		toBeBlocked.add(ip);
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec("sh /home/iplogs/drop.sh " + ip);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, TIME_AFH);
	}

	public enum IPStatus
	{
		HIT,
		ALLOWED;
	}

}
