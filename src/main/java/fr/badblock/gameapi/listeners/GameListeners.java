package fr.badblock.gameapi.listeners;

import fr.badblock.gameapi.AbstractGameHandler;
import fr.badblock.gameapi.Game;
import fr.badblock.gameapi.GameAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListeners implements Listener {

    private GameAPI instance;

    public GameListeners(GameAPI instance)
    {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event)
    {
        Game game = instance.getGame();
        Player player = event.getPlayer();
        game.getCache().add(player.getUniqueId());
        event.setJoinMessage(null);
        AbstractGameHandler handler = game.getHandler();
        handler.onPlayerJoin(player);
        handler.startGame();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event)
    {
        Game game = instance.getGame();
        Player player = event.getPlayer();
        game.getCache().remove(player.getUniqueId());
        event.setQuitMessage(null);
        game.getHandler().onPlayerQuit(player);
    }

}
