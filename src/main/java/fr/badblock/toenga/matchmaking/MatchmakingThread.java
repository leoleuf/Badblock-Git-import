package fr.badblock.toenga.matchmaking;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import fr.badblock.api.common.sync.node.ToengaInstanceStatus;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.instance.InstanceStorage.InstanceStorageCluster;
import fr.badblock.toenga.instance.ToengaInstance;

public class MatchmakingThread extends Thread
{
	private String cluster;
	private String model;
	
	public MatchmakingThread(String cluster, String model)
	{
		this.cluster = cluster;
		this.model = model;
	}
	
	private ToengaInstance bestInstance(ToengaInstance[] instances, MatchmakingEntry entry)
	{
		ToengaInstance result = null;
		int minok = 0;
		
		for(ToengaInstance instance : instances)
		{
			ToengaInstanceStatus s = instance.getInstanceStatus();
			int minok2 = -1;
			
			if(s.totalPlaces < entry.count)
				continue;
			
			if(s.placesPerGroup >= entry.count)
			{
				for(int i = 0; i < s.freePlaces.size(); i++)
				{
					if(s.freePlaces.get(i) >= entry.count)
					{
						if(minok2 == -1 || s.freePlaces.get(i) < s.freePlaces.get(minok2))
							minok2 = i;
					}
				}
				
				if(minok2 == -1)
					continue;
			}
			
			if(result == null || result.getInstanceStatus().totalPlaces > s.totalPlaces)
			{
				result = instance;
				minok = minok2;
			}
		}
		
		if(result != null)
		{
			ToengaInstanceStatus s = result.getInstanceStatus();
		
			if(minok == -1)
			{
				int total = entry.count;
				
				while(total > 0)
				{
					if(total >= s.freePlaces.get(0))
					{
						total -= s.freePlaces.remove(0);
					}
					else
					{
						s.freePlaces.set(0, s.freePlaces.get(0) - total);
						total = 0;
					}
				}
			}
			else if(s.freePlaces.get(minok) == entry.count)
			{
				s.freePlaces.remove(minok);
			}
			else
			{
				s.freePlaces.set(minok, s.freePlaces.get(minok) - entry.count);
			}
			
			s.totalPlaces -= entry.count;
		}
		
		return result;
	}
	
	public void accept(ToengaInstance instance, MatchmakingEntry entry)
	{
		
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			InstanceStorageCluster c = Toenga.instance.getStorage().getCluster(cluster);

			Queue<MatchmakingEntry> entries = c.getQueue(model);
			ToengaInstance[] instances = c.getInstances(model).toArray(new ToengaInstance[0]);
			
			if(entries.isEmpty() || instances.length == 0)
			{
				try
				{
					this.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
			
			int count = 0;
			
			List<MatchmakingEntry> unused = new LinkedList<>();
			
			while(!entries.isEmpty())
			{
				MatchmakingEntry entry = entries.poll();
				
				if(entry == null)
					continue;
				
				ToengaInstance best = bestInstance(instances, entry);
				
				if(best == null)
				{
					unused.add(entry);
				}
				else
				{
					count++;
					accept(best, entry);
				}
			}
			
			for(MatchmakingEntry entry : unused)
			{
				entries.add(entry);
			}
			
			if(count == 0)
				try
				{
					this.wait();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
		}
	}
}
