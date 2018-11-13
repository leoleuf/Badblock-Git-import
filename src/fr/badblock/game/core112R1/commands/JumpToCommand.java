package fr.badblock.game.core112R1.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Commande de j
 * @author LeLanN
 */
public class JumpToCommand extends AbstractCommand {
	
	public JumpToCommand() {
		super("jumpto", new TranslatableString("commands.jumpto.usage"), GamePermission.ADMIN, "j");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Block blockTarget = null;
		boolean first 	  = true;
		boolean can   	  = false;
		
		int maxBlock = 40;
		int block	 = 0;
		BadblockPlayer player = (BadblockPlayer) sender;
		Location location  = player.getEyeLocation().clone();
		Vector   direction = player.getEyeLocation().getDirection();
		
		Block 	   previous = location.getBlock();
		
		while(true){
			location = location.add(direction);
			Block b = location.getBlock();
			
			if(previous.equals(b)){
				continue;
			}
			
			previous = b;
			
			if(!b.getType().equals(Material.AIR)) {
				if(first){
					first = false;
				} else if(can){
					blockTarget = b;
					break;
				}
			} else if(!first){
				can = true;
			}
			
			block++;
			
			if(block > maxBlock)
				break;
		}
		
		if(blockTarget == null){
			player.sendTranslatedMessage("game.compass.tp.nothingpassthrough");
		} else {
			Location to = blockTarget.getLocation().add(0.5d, 1d, 0.5d);
			to.setYaw(player.getLocation().getYaw());
			to.setPitch(player.getLocation().getPitch());
			
			player.teleport(to);
		}
		return true;
	}
	
}
