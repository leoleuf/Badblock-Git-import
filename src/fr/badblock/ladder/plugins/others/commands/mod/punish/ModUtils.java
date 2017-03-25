package fr.badblock.ladder.plugins.others.commands.mod.punish;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.entities.OfflinePlayer;
import fr.badblock.ladder.entities.LadderOfflinePlayer;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.Permission;
import fr.badblock.permissions.PermissionManager;

public class ModUtils {

	public static int getLevel(OfflinePlayer player) {
		boolean console = false;
		if (Ladder.getInstance().getPlayer(player.getName()) != null)
			console = FriendPlayer.get(Ladder.getInstance().getPlayer(player.getName())).tail;
		if (player.hasPermission("ladder.command.punish.hierarchybypass") || console)
			return 100;
		LadderOfflinePlayer ladderOfflinePlayer = (LadderOfflinePlayer) player;
		PermissiblePlayer permissiblePlayer = ladderOfflinePlayer.getPermissions();
		int level = 0;
		for (Permission permission : permissiblePlayer.getPermissions())
			if (permission.getPermission().startsWith("ladder.command.level.")) {
				int tempLevel = Integer.parseInt(permission.getPermission().replace("ladder.command.level.", ""));
				if (tempLevel > level)
					level = tempLevel;
			}
		for (Permission permission : ((PermissibleGroup) permissiblePlayer.getParent()).getPermissions())
			if (permission.getPermission().startsWith("ladder.command.level.")) {
				int tempLevel = Integer.parseInt(permission.getPermission().replace("ladder.command.level.", ""));
				if (tempLevel > level)
					level = tempLevel;
			}
		for (String group : permissiblePlayer.getAlternateGroups().keySet()) {
			for (Permission permission : PermissionManager.getInstance().getGroup(group).getPermissions())
				if (permission.getPermission().startsWith("ladder.command.level.")) {
					int tempLevel = Integer.parseInt(permission.getPermission().replace("ladder.command.level.", ""));
					if (tempLevel > level)
						level = tempLevel;
				}
		}
		return level;
	}

}
