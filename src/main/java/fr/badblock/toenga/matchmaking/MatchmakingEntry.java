package fr.badblock.toenga.matchmaking;

import java.util.UUID;

public class MatchmakingEntry
{
	public UUID entryId;
	public int count;
	public int priority;
	public transient long createdAt;
	
	@Override
	public boolean equals(Object b)
	{
		if(b == null)
			return false;
		
		if(this == b)
			return true;
		
		if(this.getClass() != b.getClass())
			return false;
		
		return this.entryId.equals(((MatchmakingEntry) b).entryId);
	}
	
	@Override
	public int hashCode()
	{
		return entryId.hashCode();
	}
}
