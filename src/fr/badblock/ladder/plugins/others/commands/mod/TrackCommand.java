package fr.badblock.ladder.plugins.others.commands.mod;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.badblock.ladder.api.Ladder;
import fr.badblock.ladder.api.chat.RawMessage;
import fr.badblock.ladder.api.chat.RawMessage.ClickEventType;
import fr.badblock.ladder.api.chat.RawMessage.HoverEventType;
import fr.badblock.ladder.api.commands.Command;
import fr.badblock.ladder.api.entities.CommandSender;
import fr.badblock.ladder.api.entities.Player;
import fr.badblock.ladder.plugins.others.commands.PartyCommand;
import fr.badblock.ladder.plugins.others.database.BadblockDatabase;
import fr.badblock.ladder.plugins.others.database.Request;
import fr.badblock.ladder.plugins.others.database.Request.RequestType;
import fr.badblock.ladder.plugins.others.friends.FriendPlayer;
import fr.badblock.ladder.plugins.others.mp.AcceptType;
import fr.badblock.ladder.plugins.others.utils.I18N;
import fr.badblock.permissions.PermissiblePlayer;

public class TrackCommand extends Command {

	public TrackCommand() {
		super("track", "others.mod.track", "tk");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] args) {
		/*
		 * Commande : => de voir le nom du serveur & proxy où est le joueur =>
		 * dans le groupe où il est => temps de connexion => grades
		 */
		if (args.length != 1) {
			sender.sendMessages(I18N.getTranslatedMessages("commands.track.usage"));
			return;
		}
		String playerName = args[0];
		Ladder ladder = Ladder.getInstance();
		Player otherPlayer = ladder.getPlayer(playerName);
		if (otherPlayer == null) {
			sender.sendMessages(I18N.getTranslatedMessages("commands.track.disconnectedplayer", playerName));
			return;
		}
		FriendPlayer friendPlayer = FriendPlayer.get(otherPlayer);
		BadblockDatabase.getInstance().addRequest(new Request("SELECT nick FROM nick WHERE playerName = '" + BadblockDatabase.getInstance().mysql_real_escape_string(otherPlayer.getName()) + "'", RequestType.GETTER)
		{
			@Override
			public void done(ResultSet resultSet)
			{
				try
				{
					List<String> groups = new ArrayList<>(((PermissiblePlayer) otherPlayer.getAsPermissible()).getAlternateGroups().keySet());
					groups.add(otherPlayer.getAsPermissible().getParent().getName());
					String permissions = "";
					Iterator<String> iterator = groups.iterator();
					while (iterator.hasNext()) {
						String o = iterator.next();
						permissions += o + (iterator.hasNext() ? "&7, &b" : "");
					}
					String language = otherPlayer.getData().get("game").getAsJsonObject().has("locale") ? otherPlayer.getData().get("game").getAsJsonObject().get("locale").getAsString() : "§cInconnue"; 
					boolean party = friendPlayer.getParty() != null;
					String nickname = resultSet.next() ? resultSet.getString("nick") : "§cAucun";
					String[] messages = I18N.getTranslatedMessages("commands.track.message", otherPlayer.getName(),
							otherPlayer.getBungeeServer().getName(), otherPlayer.getBukkitServer().getName(), "@3",
							(otherPlayer.getAsPunished().isMute() ? "§aOui" : "§cNon"), (friendPlayer.hasAcceptRequests() ? "§aTout le monde" : "§cPersonne"), 
							(friendPlayer.hasAcceptGroups().equals(AcceptType.ALL_PEOPLE) ? "§aTout le monde" : 
								friendPlayer.hasAcceptGroups().equals(AcceptType.ONLY_FRIENDS) ? "§6Seulement ses amis" :
									friendPlayer.hasAcceptGroups().equals(AcceptType.NO_ONE) ? "§cPersonne" : "§cErreur"), 
							(friendPlayer.hasAcceptMPs().equals(AcceptType.ALL_PEOPLE) ? "§aTout le monde" : 
								friendPlayer.hasAcceptMPs().equals(AcceptType.ONLY_FRIENDS) ? "§6Seulement ses amis" :
									friendPlayer.hasAcceptMPs().equals(AcceptType.NO_ONE) ? "§cPersonne" : "§cErreur"), permissions,
							nickname,
							(otherPlayer.getRequestedGame() == null || otherPlayer.getRequestedGame().isEmpty() ? "§cNon" : otherPlayer.getRequestedGame()),
							language, (!party ? "§cAucun groupe" : ""));
					new Thread() {
						@Override
						public void run() {
							for (String string : messages) {
								if (string.equals("@3")) {
									if (!(sender instanceof Player))
										continue;
									Player player = (Player) sender;
									RawMessage rawMessage = Ladder.getInstance().createRawMessage(
											I18N.getTranslatedMessage("commands.track.cj", otherPlayer.getName()));
									rawMessage.setHoverEvent(HoverEventType.SHOW_TEXT, false,
											I18N.getTranslatedMessages("commands.track.hovertojoin", otherPlayer.getName()));
									rawMessage.setClickEvent(ClickEventType.RUN_COMMAND, false,
											"/gconnect player:" + otherPlayer.getName());
									rawMessage.send(player);
								} else if (string.equals("{group}")) {
									if (party)
										PartyCommand.list(sender, friendPlayer.getParty());
								} else
									sender.sendMessage(string);
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
				catch(Exception error)
				{
					sender.sendMessage("§cUne erreur est survenue lors de la récupération des informations du joueur en question. Nous vous invitons à contacter un administrateur de BadBlock.");
					error.printStackTrace();
				}
			}
		});
	}

}
