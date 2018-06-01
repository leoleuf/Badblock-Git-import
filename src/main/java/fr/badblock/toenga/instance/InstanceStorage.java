package fr.badblock.toenga.instance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fr.badblock.toenga.matchmaking.MatchmakingEntry;
import fr.badblock.toenga.models.ToengaModel;
import fr.badblock.toenga.utils.ToengaHeap;

public class InstanceStorage
{
	private Map<String, InstanceStorageCluster> clusters;
	
	public InstanceStorage()
	{
		this.clusters = new HashMap<>();
	}
	
	public InstanceStorageCluster getCluster(String cluster)
	{
		if(!clusters.containsKey(cluster))
		{
			clusters.put(cluster, new InstanceStorageCluster());
		}
		
		return clusters.get(cluster);
	}

	public class InstanceStorageCluster
	{
		private Map<String, Set<ToengaInstance>> waitingInstancesByType;
		private Map<String, Queue<MatchmakingEntry>> entriesByType;
		private Map<String, Object> fillingThreads;
		
		
		public InstanceStorageCluster()
		{
			this.waitingInstancesByType = new HashMap<>();
			this.entriesByType = new HashMap<>();
			this.fillingThreads = new HashMap<>();
		}
		
		public Set<ToengaInstance> getInstances(String name)
		{
			Set<ToengaInstance> queue = null;
			
			if(!waitingInstancesByType.containsKey(name))
			{
				queue = new HashSet<>();
				waitingInstancesByType.put(name, queue);

				return queue;
			}

			return waitingInstancesByType.get(name);
		}
		
		public Queue<MatchmakingEntry> getQueue(String name)
		{
			Queue<MatchmakingEntry> queue = null;
			
			if(!entriesByType.containsKey(name))
			{
				queue = new ToengaHeap<>(new EntryComparator());
				entriesByType.put(name, queue);
			}

			return entriesByType.get(name);
		}
		
		private void notify(String name)
		{
			if(fillingThreads.containsKey(name))
			{
				Object o = fillingThreads.get(name);
				o.notify();
			}
		}
		
		public void registerFilling(ToengaModel model, Object o)
		{
			this.fillingThreads.put(model.name, o);
		}
		
		public void unregisterFilling(ToengaModel model)
		{
			this.fillingThreads.remove(model.name);
		}
		
		public void instanceUpdate(ToengaInstance instance)
		{
			Set<ToengaInstance> queue = getInstances(instance.getModel().name);

			if(!queue.contains(instance))
				queue.add(instance);
			notify(instance.getModel().name);
		}
		
		public void instanceRemove(ToengaInstance instance)
		{
			Set<ToengaInstance> queue = getInstances(instance.getModel().name);
			queue.remove(instance);
		}
		
		public void entryUpdate(String model, MatchmakingEntry entry)
		{
			Queue<MatchmakingEntry> queue = getQueue(model);

			queue.add(entry);
			notify(model);
		}
		
		public void entryRemove(String model, MatchmakingEntry entry)
		{
			Queue<MatchmakingEntry> queue = getQueue(model);
			queue.remove(entry);
		}
	}
	
	public class EntryComparator implements Comparator<MatchmakingEntry>
	{
		@Override
		public int compare(MatchmakingEntry a, MatchmakingEntry b)
		{
			if(a.priority != b.priority)
				return a.priority < b.priority ? 1 : -1;

			if(a.createdAt == b.createdAt)
				return 0;
			
			return a.createdAt < b.createdAt ? -1 : 1;
		}
	}
}
