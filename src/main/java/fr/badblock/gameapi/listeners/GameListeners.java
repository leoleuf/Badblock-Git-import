package fr.badblock.gameapi.listeners;

import fr.badblock.gameapi.Game;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.GameState;
import fr.badblock.gameapi.tasks.StartingTask;
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
        if (game.isState(GameState.WAITING) && game.getCache().size() >= game.getRequiredPlayers()) {
            game.setState(GameState.STARTING);
            game.setTask(new StartingTask(game, 30).runTaskTimer(instance.getPlugin(), 0, 20));
        }
        game.getHandler().onPlayerJoin(player); // Calling onPlayerJoin method with player
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event)
    {
        Game game = instance.getGame();
        Player player = event.getPlayer();
        game.getCache().remove(player.getUniqueId());
        event.setQuitMessage(null);
        game.getHandler().onPlayerQuit(player); // Calling onPlayerQuit method with player
    }

}
