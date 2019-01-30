package fr.badblock.game.core112R1.listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import fr.badblock.gameapi.GameAPI;
import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.NetworkManager;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class CrashBookFix
{

    public static void addListener(final Plugin plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(plugin, ListenerPriority.LOWEST, new PacketType[] { PacketType.Play.Client.BLOCK_PLACE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    event.setCancelled(true);
                    return;
                }
                final ItemStack itemUse = (ItemStack)event.getPacket().getItemModifier().readSafely(0);
                if (itemUse == null || itemUse.getType() != Material.WRITTEN_BOOK) {
                    return;
                }
                final ItemStack itemOnHand = player.getItemInHand();
                if (itemOnHand == null || !itemOnHand.isSimilar(itemUse)) {
                    kick(player);
                    forceKick(player);
                    this.plugin.getLogger().log(Level.INFO, "§cAction suspecte bloquée. §7(ID #NBK-92DS0)", player.getName());
                    event.setCancelled(true);
                }
            }
        });
    }
    
    private static void forceKick(final Player player) {
        Bukkit.getScheduler().runTaskLater(GameAPI.getAPI(), () -> {
        	PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
            if (connection != null) {
            	NetworkManager manager = connection.networkManager;
                if (manager != null) {
                	Channel ch = manager.channel;
                    if (ch != null) {
                        ch.close();
                    }
                }
            }
        }, 1000L);
    }
    
    private static void kick(final Player player) {
        Bukkit.getScheduler().runTask(GameAPI.getAPI(), () -> player.kickPlayer("§cAction suspecte bloquée. §7(ID #NBK-92DS0)"));
    }
	
}
