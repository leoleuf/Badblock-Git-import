package fr.badblock.bungee.players.layer;

import fr.badblock.bungee._plugins.objects.friendlist.FriendListable;
import fr.badblock.bungee._plugins.objects.party.Partyable;
import lombok.Data;

@Data
public class BadPlayerSettings
{
	
	// Is partyable by who?
	public Partyable	partyable;
    // Is FriendListable by who ?
    public FriendListable friendListable;

	/**
	 * Default values for each player
	 */
	public BadPlayerSettings()
	{
		partyable = Partyable.WITH_EVERYONE;
        friendListable = FriendListable.YES;
	}
	
}
