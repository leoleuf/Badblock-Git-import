package com.lelann.bungee.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import com.google.common.collect.ImmutableSet;
import com.lelann.bungee.party.PartiesManager;
import com.lelann.bungee.party.Party;
import com.lelann.bungee.utils.ChatUtils;

public class PartyCommand extends Command implements TabExecutor{
	public PartyCommand() {
		super("party", "party.play");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Party party = PartiesManager.getInstance().getParty(player);

		if(args.length == 0){
			PartiesManager.getInstance().sendHelp(player);
		} else if(args[0].equalsIgnoreCase("create")){
			if(party == null){
				PartiesManager.getInstance().create(player);
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] Partie crée avec succès !");
			} else {
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous avez déjà une partie en cours !");
			}
		} else if(args[0].equalsIgnoreCase("disband")){
			if(party == null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'avez pas de partie en cours !");
			} else if(!party.isLeader(player)){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous devez être leader de votre partie pour faire ça !");
			} else {
				party.disband();
			}
		} else if(args[0].equalsIgnoreCase("invite")){
			if(party == null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'avez pas de partie en cours !");
			} else if(!party.isLeader(player)){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous devez être leader de votre partie pour faire ça !");
			} else if(args.length == 1){
				sendMessage(player, "%aqua%/%yellow%party invite %red%player %aqua%> %yellow%invite un joueur à la partie");
			} else {
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(args[1]);
				if(invitedPlayer == null){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur '" + args[1] + "' est introuvable.");
				} else if(party.isInvited(invitedPlayer)){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur est déjà invité ! Ne le spammez pas.");
				} else if(party.isMember(invitedPlayer)){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur est déjà dans la partie !");
				} else {
					party.invite(invitedPlayer, player);
				}
			}
		} else if(args[0].equalsIgnoreCase("kick")){
			if(party == null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'avez pas de partie en cours !");
			} else if(!party.isLeader(player)){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous devez être leader de votre partie pour faire ça !");
			} else if(args.length == 1){
				sendMessage(player, "%aqua%/%yellow%party kick %red%player %aqua%> %yellow%éjecte un joueur de la partie");
			} else {
				ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(args[1]);
				if(invitedPlayer == null){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur '" + args[1] + "' est introuvable.");
				} else if(!party.isMember(invitedPlayer)){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur n'est pas dans la partie.");
				} else {
					party.kick(invitedPlayer);
				}
			}
		} else if(args[0].equalsIgnoreCase("join")){
			if(party != null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous avez déjà une partie en cours.");
			} else if(args.length == 1){
				sendMessage(player, "%aqua%/%yellow%party join %red%party %aqua%> %yellow%rejoindre une partie");
			} else {
				ProxiedPlayer concernedPlayer = ProxyServer.getInstance().getPlayer(args[1]);
				if(concernedPlayer == null){
					sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur '" + args[1] + "' est introuvable.");
				} else {
					Party theParty = PartiesManager.getInstance().getParty(concernedPlayer);
					if(theParty == null){
						sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Le joueur '" + args[1] + "' n'a pas de partie en cours.");
					} else if(!theParty.isInvited(player)){
						sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Ce joueur ne vous a pas invité dans sa partie.");
					} else {
						theParty.enter(player);
					}
				}
			}
		} else if(args[0].equalsIgnoreCase("leave")){
			if(party == null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'avez pas de partie en cours !");
			} else {
				party.quit(player);
			}
		} else if(args[0].equalsIgnoreCase("list")){
			if(party == null){
				sendMessage(player, "%yellow%[%aqua%BadBlock-Party%yellow%] %red%Vous n'avez pas de partie en cours !");
			} else {
				List<String> result = new ArrayList<String>();
				result.add("%yellow%[%aqua%BadBlock-Party%yellow%] %red%Joueurs");
				for(UUID member : party.getMembers()){
					ProxiedPlayer theMember = ProxyServer.getInstance().getPlayer(member);
					if(theMember != null){
						result.add("  %red%> %yellow%" + theMember.getName());
					}
				}
				ChatUtils.sendMessagePlayer(player, result.toArray(new String[0]));
			}
		} else if(args[0].equalsIgnoreCase("info")){
			ChatUtils.sendMessagePlayer(player, PartiesManager.getInstance().count() + " parties");
		} else PartiesManager.getInstance().sendHelp(player);
	}

	public void sendMessage(ProxiedPlayer player, String message){
		ChatUtils.sendMessagePlayer(player, message);
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args){
		if(args.length == 0){
			return ImmutableSet.of();
		}

		Set<String> matches = new HashSet<>();
		String search = args[args.length - 1].toLowerCase();
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
		{
			if (player.getName().toLowerCase().startsWith(search)) {
				matches.add( player.getName() );
			}
		}

		return matches;
	}
}
